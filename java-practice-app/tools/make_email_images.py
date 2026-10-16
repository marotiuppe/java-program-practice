"""Generates the images used in emails (app/static/email/*). Run it only when the artwork changes; the results are
committed. Needs: pip install playwright pillow, and Google Chrome installed.

    python tools/make_email_images.py

Outputs: logo.png (transparent, 120px), hero-<kind>.png (1200x400, shown at 600x200 for sharp screens) and an
animated hero-streak.gif (600x200). Emails reference them as <APP_BASE_URL>/static/email/<file>."""
import io
import os

from playwright.sync_api import sync_playwright
from PIL import Image

HERE = os.path.dirname(os.path.abspath(__file__))
OUT = os.path.join(HERE, "..", "app", "static", "email")
LOGO_SVG = open(os.path.join(HERE, "..", "app", "static", "logo.svg"), encoding="utf-8").read()
os.makedirs(OUT, exist_ok=True)

# kind -> (emoji, gradient start, gradient end)
HEROES = {
    "verify": ("✉️", "#4f46e5", "#7c3aed"),
    "reset": ("\U0001F511", "#4f46e5", "#7c3aed"),
    "security": ("\U0001F6E1️", "#0f766e", "#4f46e5"),
    "lapsed": ("\U0001F44B", "#6366f1", "#a855f7"),
    "weekly": ("\U0001F4C8", "#4f46e5", "#9333ea"),
    "test": ("✅", "#16a34a", "#4f46e5"),
    "streak": ("\U0001F525", "#f97316", "#9333ea"),
}


def hero_html(emoji, c1, c2, w, h, scale=1.0, rotate=0.0, bob=0.0, shadow=True):
    tile = int(h * 0.56)
    return f"""<!doctype html><meta charset="utf-8"><style>
html,body{{margin:0}}
body{{width:{w}px;height:{h}px;position:relative;overflow:hidden;background:linear-gradient(135deg,{c1} 0%,{c2} 100%);
font-family:"Segoe UI Emoji","Apple Color Emoji","Noto Color Emoji",sans-serif}}
.dot{{position:absolute;border-radius:50%;background:rgba(255,255,255,.12)}}
.tile{{position:absolute;left:50%;top:50%;width:{tile}px;height:{tile}px;margin:-{tile // 2}px 0 0 -{tile // 2}px;
background:#fff;border-radius:{tile // 4}px;box-shadow:{f"0 {h // 20}px {h // 8}px rgba(0,0,0,.25)" if shadow else "none"};display:grid;place-items:center;
transform:translateY({bob}px)}}
.e{{font-size:{int(tile * 0.62)}px;line-height:1;transform:scale({scale}) rotate({rotate}deg)}}
</style>
<div class="dot" style="width:{h * .9}px;height:{h * .9}px;left:-{h * .25}px;top:-{h * .3}px"></div>
<div class="dot" style="width:{h * .6}px;height:{h * .6}px;right:{w * .06}px;bottom:-{h * .25}px"></div>
<div class="dot" style="width:{h * .18}px;height:{h * .18}px;left:{w * .22}px;bottom:{h * .18}px"></div>
<div class="dot" style="width:{h * .12}px;height:{h * .12}px;right:{w * .24}px;top:{h * .2}px"></div>
<div class="tile"><div class="e">{emoji}</div></div>"""


with sync_playwright() as p:
    b = p.chromium.launch(channel="chrome")

    # logo: transparent PNG
    pg = b.new_page(viewport={"width": 120, "height": 120})
    pg.set_content(f'<body style="margin:0;background:transparent"><div style="width:120px;height:120px">{LOGO_SVG}</div></body>'
                   "<style>svg{width:100%;height:100%}</style>")
    pg.screenshot(path=os.path.join(OUT, "logo.png"), omit_background=True)

    # static heroes
    for kind, (emoji, c1, c2) in HEROES.items():
        if kind == "streak":  # the streak banner is the animated GIF below
            continue
        pg = b.new_page(viewport={"width": 1200, "height": 400})
        pg.set_content(hero_html(emoji, c1, c2, 1200, 400))
        pg.wait_for_timeout(150)
        pg.screenshot(path=os.path.join(OUT, f"hero-{kind}.png"))
        pg.close()

    # animated streak flame: the first frame is the pretty one, because Outlook shows only that
    emoji, c1, c2 = HEROES["streak"]
    frames = []
    pg = b.new_page(viewport={"width": 600, "height": 200})
    for scale, rotate, bob in [(1.0, 0, 0), (1.06, -3, -3), (1.12, 0, -5), (1.06, 3, -3), (1.0, 0, 0), (0.95, -2, 2), (0.92, 0, 3), (0.95, 2, 2)]:
        pg.set_content(hero_html(emoji, c1, c2, 600, 200, scale, rotate, bob, shadow=False))  # a soft shadow bands badly in a GIF
        pg.wait_for_timeout(80)
        frames.append(Image.open(io.BytesIO(pg.screenshot())).convert("RGB"))
    b.close()

pal = [f.quantize(colors=128, method=Image.Quantize.MEDIANCUT, dither=Image.Dither.NONE) for f in frames]
pal[0].save(os.path.join(OUT, "hero-streak.gif"), save_all=True, append_images=pal[1:], duration=140, loop=0, optimize=True)
for name in sorted(os.listdir(OUT)):
    print(f"{name:20} {os.path.getsize(os.path.join(OUT, name)) / 1024:6.0f} KB")
