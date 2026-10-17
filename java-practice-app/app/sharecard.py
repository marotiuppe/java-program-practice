"""Shareable achievement cards (roadmap 4.2).

A card is a small dict describing one milestone. The server signs it into a token, and the public URLs
    /s/<token>            page with Open Graph tags (what WhatsApp, X and LinkedIn preview)
    /s/<token>/og.png     1200x630 link-preview image
    /s/<token>/card.png   1080x1350 portrait card to send as a picture
render it. The token is signed with SECRET_KEY, so only cards the server issued for real progress can be shown,
and nothing in it identifies the learner (no name, no email, no user id).

Card dict: {"i": icon, "t": title, "s": subtitle, "st": [[value, label], ...up to 3], "p": [fraction, label] | None}
icon is one of: check, target, flame, trophy, star."""
import functools
import io
import json
import os
import re

from itsdangerous import BadSignature, URLSafeSerializer
from PIL import Image, ImageDraw, ImageFont

FONT_DIR = os.path.join(os.path.dirname(__file__), "fonts")
ICONS = ("check", "target", "flame", "trophy", "star")
INDIGO, PURPLE, AMBER = (79, 70, 229), (147, 51, 234), (251, 191, 36)
INK = (31, 36, 51)
WHITE = (255, 255, 255)


def _signer() -> URLSafeSerializer:
    return URLSafeSerializer(os.environ.get("SECRET_KEY", "dev-only-change-me"), salt="share-card")


def make_token(card: dict) -> str:
    return _signer().dumps(card)


def read_token(token: str):
    """The validated card dict, or None for anything that was not issued by this server or is malformed."""
    try:
        card = _signer().loads(token)
    except (BadSignature, ValueError, TypeError):
        return None
    if not isinstance(card, dict) or card.get("i") not in ICONS or not isinstance(card.get("t"), str):
        return None
    if "u" in card and not (isinstance(card["u"], str) and len(card["u"]) <= 25):
        return None
    if "r" in card and not (isinstance(card["r"], str) and re.fullmatch(r"[a-z0-9]{6,12}", card["r"])):
        return None
    stats = card.get("st")
    if not isinstance(stats, list) or len(stats) > 3 or not all(
            isinstance(x, list) and len(x) == 2 and all(isinstance(v, str) for v in x) for x in stats):
        return None
    return card


@functools.lru_cache(maxsize=16)
def _font(name: str, size: int):
    return ImageFont.truetype(os.path.join(FONT_DIR, f"Poppins-{name}.ttf"), size)


class _Canvas:
    """Draws at 2x and shrinks at the end, which gives smooth edges on every shape."""

    S = 2

    def __init__(self, w: int, h: int, plain=None):
        self.w, self.h = w, h
        if plain:
            self.im = Image.new("RGB", (w * self.S, h * self.S), plain)
        else:
            corners = Image.new("RGB", (2, 2))
            mid = tuple((a + b) // 2 for a, b in zip(INDIGO, PURPLE))
            corners.putdata([INDIGO, mid, mid, PURPLE])
            self.im = corners.resize((w * self.S, h * self.S), Image.BICUBIC)
        self.d = ImageDraw.Draw(self.im, "RGBA")

    def _s(self, *v):
        return [x * self.S for x in v]

    def circle(self, cx, cy, r, fill):
        self.d.ellipse(self._s(cx - r, cy - r, cx + r, cy + r), fill=fill)

    def rrect(self, x, y, w, h, r, fill):
        self.d.rounded_rectangle(self._s(x, y, x + w, y + h), radius=r * self.S, fill=fill)

    def poly(self, pts, fill):
        self.d.polygon([(x * self.S, y * self.S) for x, y in pts], fill=fill)

    def line(self, pts, width, fill):
        sp = [(x * self.S, y * self.S) for x, y in pts]
        self.d.line(sp, fill=fill, width=int(width * self.S), joint="curve")
        for x, y in sp:  # round caps and joints
            r = width * self.S / 2
            self.d.ellipse((x - r, y - r, x + r, y + r), fill=fill)

    def fit(self, text, weight, size, max_w, min_size=24):
        while size > min_size and self.d.textlength(text, font=_font(weight, size * self.S)) > max_w * self.S:
            size -= 2
        return size

    def text(self, x, y, text, weight, size, fill, anchor="ms", max_w=None):
        if max_w:
            size = self.fit(text, weight, size, max_w)
        self.d.text((x * self.S, y * self.S), text, font=_font(weight, size * self.S), fill=fill, anchor=anchor)

    def width(self, text, weight, size):
        return self.d.textlength(text, font=_font(weight, size * self.S)) / self.S

    def image(self):
        return self.im.resize((self.w, self.h), Image.LANCZOS)

    def png(self) -> bytes:
        out = self.im.resize((self.w, self.h), Image.LANCZOS)
        buf = io.BytesIO()
        out.save(buf, "PNG", optimize=True)
        return buf.getvalue()


def _bezier(path, steps=18):
    """path: start point then cubic segments [(c1, c2, end), ...] -> list of points."""
    pts = [path[0]]
    cur = path[0]
    for c1, c2, end in path[1:]:
        for i in range(1, steps + 1):
            t = i / steps
            u = 1 - t
            pts.append((u ** 3 * cur[0] + 3 * u * u * t * c1[0] + 3 * u * t * t * c2[0] + t ** 3 * end[0],
                        u ** 3 * cur[1] + 3 * u * u * t * c1[1] + 3 * u * t * t * c2[1] + t ** 3 * end[1]))
        cur = end
    return pts


_FLAME = [(50, 6), ((58, 28), (82, 40), (82, 64)), ((82, 82), (68, 95), (50, 95)), ((32, 95), (18, 82), (18, 64)),
          ((18, 50), (26, 40), (34, 32)), ((36, 44), (42, 48), (46, 48)), ((42, 32), (44, 18), (50, 6))]


def _icon(c: _Canvas, kind: str, cx: float, cy: float, size: float):
    """White rounded tile with a coloured glyph, centred on (cx, cy)."""
    x0, y0 = cx - size / 2, cy - size / 2
    c.rrect(x0 + 0, y0 + size * 0.04, size, size, size * 0.25, (0, 0, 0, 45))  # soft shadow
    c.rrect(x0, y0, size, size, size * 0.25, WHITE)
    k = size / 100

    def P(px, py):  # glyph coordinates (0-100) -> canvas
        return x0 + px * k, y0 + py * k

    if kind == "check":
        c.circle(*P(50, 50), 32 * k, (22, 163, 74))
        c.line([P(36, 51), P(46, 61), P(65, 40)], 7 * k, WHITE)
    elif kind == "target":
        c.circle(*P(50, 50), 34 * k, INDIGO)
        c.circle(*P(50, 50), 24 * k, WHITE)
        c.circle(*P(50, 50), 15 * k, PURPLE)
        c.circle(*P(50, 50), 6 * k, AMBER)
    elif kind == "flame":
        c.poly([P(x, y) for x, y in _bezier(_FLAME)], (249, 115, 22))
        inner = [(50 + (x - 50) * 0.5, 95 + (y - 95) * 0.5) for x, y in _bezier(_FLAME)]
        c.poly([P(x, y) for x, y in inner], AMBER)
    elif kind == "trophy":
        gold = (245, 158, 11)
        c.line([P(30, 28), P(22, 28), P(22, 38), P(34, 48)], 5 * k, gold)
        c.line([P(70, 28), P(78, 28), P(78, 38), P(66, 48)], 5 * k, gold)
        c.poly([P(30, 22), P(70, 22), P(66, 50), P(58, 60), P(42, 60), P(34, 50)], gold)
        c.rrect(*P(45, 58), 10 * k, 16 * k, 2 * k, gold)
        c.rrect(*P(32, 74), 36 * k, 9 * k, 4 * k, gold)
    else:  # star
        import math
        pts = []
        for i in range(10):
            ang = -math.pi / 2 + i * math.pi / 5
            r = 36 if i % 2 == 0 else 15
            pts.append(P(50 + r * math.cos(ang), 54 + r * math.sin(ang)))
        c.poly(pts, AMBER)


def _decor(c: _Canvas, shapes):
    for cx, cy, r in shapes:
        c.circle(cx, cy, r, (255, 255, 255, 26))


def _brand(c: _Canvas, x: float, y: float, scale: float):
    """The app logo (same shapes as static/logo.svg) with the name beside it."""
    s = 64 * scale
    c.rrect(x, y, s, s, 15 * scale, WHITE)
    c.line([(x + 30 * scale, y + 16 * scale), (x + 46 * scale, y + 16 * scale)], 6 * scale, INDIGO)
    c.line([(x + 39 * scale, y + 16 * scale), (x + 39 * scale, y + 37 * scale)], 6 * scale, INDIGO)
    c.line(_bezier([(x + 39 * scale, y + 37 * scale), ((x + 39 * scale, y + 49 * scale), (x + 27 * scale, y + 49 * scale),
                                                       (x + 27 * scale, y + 49 * scale)),
                    ((x + 19 * scale, y + 49 * scale), (x + 16 * scale, y + 43 * scale), (x + 16 * scale, y + 43 * scale))]),
           6 * scale, INDIGO)
    c.rrect(x + 42 * scale, y + 44 * scale, 11 * scale, 5 * scale, 2.5 * scale, AMBER)
    c.text(x + s + 22 * scale, y + s * 0.70, "Java Practice", "Bold", int(34 * scale), WHITE, anchor="ls")


def _tiles(c: _Canvas, stats, x0, y, total_w, h, gap, value_size, label_size):
    n = len(stats)
    tw = min((total_w - gap * (n - 1)) / n, 300 if total_w >= 900 else 240)
    if n < 3 and total_w >= 900:  # centre a short row on the portrait card
        x0 += (total_w - (tw * n + gap * (n - 1))) / 2
    for i, (value, label) in enumerate(stats):
        x = x0 + i * (tw + gap)
        c.rrect(x, y, tw, h, 30, (255, 255, 255, 42))
        c.text(x + tw / 2, y + h * 0.52, value, "Bold", value_size, WHITE, max_w=tw - 24)
        c.text(x + tw / 2, y + h * 0.82, label, "Medium", label_size, (255, 255, 255, 225), max_w=tw - 24)


CHALLENGE = {"check": "Can you beat it?", "flame": "Can you beat my streak?", "target": "Can you beat it?",
             "trophy": "Ready to start yours?", "star": "Start your own streak"}


def _avatar_row(c: _Canvas, cx: float, cy: float, who: str, max_w: float):
    """Round initial badge followed by the learner's @name, centred as a pair on (cx, cy)."""
    r = 36
    size = c.fit(who, "Bold", 42, max_w - 2 * r - 40)
    total = 2 * r + 20 + c.width(who, "Bold", size)
    x0 = cx - total / 2
    c.circle(x0 + r, cy, r, WHITE)
    initial = (who.lstrip("@")[:1] or "?").upper()
    c.text(x0 + r, cy + 15, initial, "Bold", 44, INDIGO)
    c.text(x0 + 2 * r + 20, cy + size * 0.36, who, "Bold", size, WHITE, anchor="ls")


def _qr(c: _Canvas, url: str, x: float, y: float, size: float):
    """Black-on-white QR code for `url` in a white square at (x, y), `size` px wide, with a one-module quiet zone."""
    import segno
    m = segno.make(url, error="m", micro=False).matrix
    n = len(m) + 2
    cell = size / n
    c.rrect(x, y, size, size, 14, WHITE)
    for r, row in enumerate(m):
        for q, v in enumerate(row):
            if v:
                c.d.rectangle([(x + (q + 1) * cell) * c.S, (y + (r + 1) * cell) * c.S,
                               (x + (q + 2) * cell) * c.S - 1, (y + (r + 2) * cell) * c.S - 1], fill=INK)


def _host(site: str) -> str:
    return site.replace("https://", "").replace("http://", "").rstrip("/")


def render_card(card: dict, site: str) -> bytes:
    W, H = 1080, 1350
    host = _host(site)
    c = _Canvas(W, H)
    _decor(c, [(-120, -100, 420), (1000, 330, 260), (120, 1180, 220), (930, 1250, 170)])
    _brand(c, 70, 62, 1.25)
    chip = "30-DAY JAVA CHALLENGE"
    cw = c.width(chip, "Bold", 24) + 44
    c.rrect(W - 70 - cw, 78, cw, 52, 26, (255, 255, 255, 56))
    c.text(W - 70 - cw / 2, 113, chip, "Bold", 24, WHITE)
    who = card.get("u")
    prog = card.get("p")
    sh = 0 if prog else 40  # no progress bar: spread the middle out so the card stays balanced
    if who:
        _avatar_row(c, W / 2, 232 + sh / 2, who, W - 160)
    _icon(c, card["i"], W / 2, (420 if who else 360) + sh, 210)
    c.text(W / 2, 640 + sh + 10, card["t"], "Bold", 90, WHITE, max_w=W - 110)
    if card.get("s"):
        c.text(W / 2, 698 + sh + 10, card["s"], "Medium", 36, (255, 255, 255, 225), max_w=W - 140)
    stats = card.get("st") or []
    if stats:
        _tiles(c, stats, 90, 748 + sh + 14, W - 180, 190 if not prog else 180, 30, 70, 26)
    if prog:
        bx, bw, by = 150, W - 300, 962
        c.rrect(bx, by, bw, 22, 11, (255, 255, 255, 64))
        c.rrect(bx, by, max(22, bw * max(0, min(1, float(prog[0])))), 22, 11, AMBER)
        c.text(W / 2, by + 62, str(prog[1]), "Medium", 28, (255, 255, 255, 225), max_w=W - 200)
    # call to action: invite, with a QR code for screenshots that cannot carry a link
    py, ph = 1075, 225
    c.rrect(60, py, W - 120, ph, 40, WHITE)
    qs = 170
    invite = f"{site.rstrip('/')}/join/{card['r']}" if card.get("r") else f"{site.rstrip('/')}/?utm_source=share_card"
    _qr(c, invite, W - 60 - 30 - qs, py + (ph - qs) / 2, qs)
    c.d.rounded_rectangle([(W - 60 - 30 - qs) * c.S, (py + (ph - qs) / 2) * c.S, (W - 60 - 30) * c.S, (py + (ph + qs) / 2) * c.S],
                          radius=14 * c.S, outline=(220, 224, 240), width=2 * c.S)
    tx, tw = 105, W - 60 - 30 - qs - 105 - 24
    c.text(tx, py + 78, CHALLENGE.get(card["i"], "Join me!"), "Bold", 46, INK, anchor="ls", max_w=tw)
    c.text(tx, py + 124, "Learn Java in 30 days, free.", "Medium", 30, (88, 97, 116), anchor="ls", max_w=tw)
    c.text(tx, py + 176, host, "Bold", 32, INDIGO, anchor="ls", max_w=tw)
    return c.png()


def render_og(card: dict, site: str) -> bytes:
    W, H = 1200, 630
    host = _host(site)
    c = _Canvas(W, H)
    _decor(c, [(-100, -80, 330), (1130, 150, 250), (1010, 640, 200)])
    _brand(c, 70, 52, 1.1)
    _icon(c, card["i"], 210, 330, 230)
    tx, mw = 370, W - 440
    who = card.get("u")
    y = 0 if who else -24
    if who:
        size = c.fit(who, "Bold", 32, mw - 70)
        w, h = c.width(who, "Bold", size) + 56, size * 1.9
        c.rrect(tx, 150, w, h, h / 2, (255, 255, 255, 56))
        c.text(tx + 28, 150 + h / 2 + size * 0.36, who, "Bold", size, WHITE, anchor="ls")
    c.text(tx, 318 + y, card["t"], "Bold", 70, WHITE, anchor="ls", max_w=mw)
    if card.get("s"):
        c.text(tx, 372 + y, card["s"], "Medium", 31, (255, 255, 255, 225), anchor="ls", max_w=mw)
    stats = card.get("st") or []
    if stats:
        _tiles(c, stats, tx, 410 + y, mw, 112, 20, 46, 20)
    c.text(70, 585, f"{CHALLENGE.get(card['i'], 'Join me!')}  Join free at {host}", "Bold", 30, WHITE, anchor="ls", max_w=W - 140)
    return c.png()


@functools.lru_cache(maxsize=64)
def render_cached(token: str, kind: str, site: str):
    """PNG bytes for a token ("card" or "og"), or None when the token is invalid. Cached per process."""
    card = read_token(token)
    if card is None:
        return None
    return render_card(card, site) if kind == "card" else render_og(card, site)


# ---------------------------------------------------------------- certificates (roadmap 4.3)
CERT_W, CERT_H = 1600, 1131  # A4 landscape proportions
CREAM = (255, 253, 248)
GOLD = (217, 160, 20)
MUTED = (88, 97, 116)


def cert_display_id(cid: str) -> str:
    """JP-ABCD-EFGH-JKLM from the 12-character id."""
    return "JP-" + "-".join(cid[i:i + 4] for i in range(0, 12, 4))


def _spaced(c: _Canvas, x: float, y: float, text: str, weight: str, size: int, fill, gap: float):
    """Centred text with extra letter spacing (small caps look)."""
    widths = [c.width(ch, weight, size) for ch in text]
    total = sum(widths) + gap * (len(text) - 1)
    cx = x - total / 2
    for ch, w in zip(text, widths):
        c.text(cx, y, ch, weight, size, fill, anchor="ls")
        cx += w + gap


def render_certificate(cert: dict, site: str) -> "Image.Image":
    """cert: id, name, level (label), solved, days, issued (e.g. "17 October 2026")."""
    W, H = CERT_W, CERT_H
    c = _Canvas(W, H, plain=CREAM)
    # brand band
    band = Image.new("RGB", (2, 2))
    mid = tuple((a + b) // 2 for a, b in zip(INDIGO, PURPLE))
    band.putdata([INDIGO, mid, mid, PURPLE])
    c.im.paste(band.resize((W * c.S, 190 * c.S), Image.BICUBIC), (0, 0))
    _brand(c, 90, 62, 1.4)
    _spaced(c, W - 330, 118, "CERTIFICATE OF COMPLETION", "Bold", 26, WHITE, 3)
    c.d.rectangle([(0, 190 * c.S), (W * c.S, 198 * c.S)], fill=GOLD)
    # frame
    c.d.rounded_rectangle([(40 * c.S, 230 * c.S), ((W - 40) * c.S, (H - 40) * c.S)], radius=24 * c.S,
                          outline=(222, 226, 240), width=3 * c.S)
    c.text(W / 2, 330, "This certifies that", "Medium", 34, MUTED)
    c.text(W / 2, 460, cert["name"], "Bold", 104, INK, max_w=W - 260)
    c.d.rectangle([((W / 2 - 330) * c.S, 492 * c.S), ((W / 2 + 330) * c.S, 495 * c.S)], fill=GOLD)
    c.text(W / 2, 566, "has successfully completed the", "Medium", 34, MUTED)
    c.text(W / 2, 674, f"{cert['level']} level", "Bold", 84, INDIGO, max_w=W - 260)
    c.text(W / 2, 742, "of the 30-day Java Practice plan", "Medium", 34, MUTED)
    # stats
    for i, (v, label) in enumerate([(str(cert["solved"]), "questions solved"), (str(cert["days"]), "days completed")]):
        x = W / 2 + (-170 if i == 0 else 170)
        c.text(x, 842, v, "Bold", 58, INK)
        c.text(x, 884, label, "Medium", 26, MUTED)
    # footer: date left, verification right
    url = f"{site.rstrip('/')}/cert/{cert['id']}"
    c.text(110, H - 180, "ISSUED", "Bold", 22, MUTED, anchor="ls")
    c.text(110, H - 138, cert["issued"], "Bold", 38, INK, anchor="ls")
    c.text(110, H - 96, "Certificate of completion of a self-paced practice plan.", "Medium", 20, MUTED, anchor="ls")
    c.text(110, H - 68, "Not an accredited qualification.", "Medium", 20, MUTED, anchor="ls")
    qs = 190
    qx, qy = W - 110 - qs, H - 70 - qs
    _qr(c, url, qx, qy, qs)
    c.d.rounded_rectangle([qx * c.S, qy * c.S, (qx + qs) * c.S, (qy + qs) * c.S], radius=14 * c.S,
                          outline=(220, 224, 240), width=2 * c.S)
    c.text(qx - 30, H - 180, "VERIFY", "Bold", 22, MUTED, anchor="rs")
    c.text(qx - 30, H - 138, cert_display_id(cert["id"]), "Bold", 38, INK, anchor="rs")
    c.text(qx - 30, H - 96, _host(site) + "/cert/" + cert["id"], "Medium", 20, MUTED, anchor="rs", max_w=520)
    return c.image()


def certificate_png(cert: dict, site: str) -> bytes:
    buf = io.BytesIO()
    render_certificate(cert, site).save(buf, "PNG", optimize=True)
    return buf.getvalue()


def certificate_pdf(cert: dict, site: str) -> bytes:
    buf = io.BytesIO()
    render_certificate(cert, site).save(buf, "PDF", resolution=150.0, title=f"Java Practice certificate: {cert['name']}")
    return buf.getvalue()


def certificate_og(cert: dict, site: str) -> bytes:
    """1200x630 link preview: the certificate on the brand background."""
    c = _Canvas(1200, 630)
    _decor(c, [(-100, -80, 330), (1130, 150, 250), (1010, 640, 200)])
    img = render_certificate(cert, site).resize((792, 560), Image.LANCZOS)
    c.d.rounded_rectangle([(204 * c.S, 40 * c.S), (996 * c.S, 612 * c.S)], radius=18 * c.S, fill=(0, 0, 0, 60))
    c.im.paste(img.resize((792 * c.S, 560 * c.S), Image.LANCZOS), (204 * c.S, 36 * c.S))
    return c.png()


@functools.lru_cache(maxsize=32)
def certificate_cached(kind: str, cid: str, name: str, level: str, solved: int, days: int, issued: str, site: str) -> bytes:
    cert = {"id": cid, "name": name, "level": level, "solved": solved, "days": days, "issued": issued}
    return {"png": certificate_png, "pdf": certificate_pdf, "og": certificate_og}[kind](cert, site)
