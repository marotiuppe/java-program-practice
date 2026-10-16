"""Output comparison used by sample tests and the predict-the-output check."""


def normalize(text: str) -> str:
    """Ignore CRLF vs LF, trailing spaces on each line, and blank lines at the end."""
    lines = [line.rstrip() for line in text.replace("\r\n", "\n").replace("\r", "\n").split("\n")]
    while lines and lines[-1] == "":
        lines.pop()
    while lines and lines[0] == "":
        lines.pop(0)
    return "\n".join(lines)


def same(actual: str, expected: str) -> bool:
    return normalize(actual) == normalize(expected)
