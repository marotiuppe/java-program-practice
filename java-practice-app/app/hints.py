"""Plain-language explanations for common javac errors (rule based, no outside services).

explain(javac_output) -> [{"line": 5, "message": "<javac's wording>", "help": "<what it usually means and how to fix it>"}]
Only errors we have a rule for are returned; the learner still sees javac's own output as well."""
import re

HEADER = re.compile(r"^(?:.*[\\/])?Main\.java:(\d+): error: (.*)$")
SYMBOL = re.compile(r"^\s*symbol:\s+(\w+)\s+(.*)$")

# (regex on javac's message, help text). Groups from the regex can be used as {1}, {2}, ... in the text.
RULES = [
    (r"^';' expected$",
     "A statement is missing its closing semicolon. Look at the end of this line (and the one before it) "
     "and add ';'."),
    (r"^'\)' expected$|^'\)' or ',' expected$",
     "A closing parenthesis ')' is missing. Count the '(' and ')' on this line; they must match."),
    (r"^'\(' expected$",
     "A '(' is missing, for example after if, while, for or a method name."),
    (r"^'\]' expected$",
     "A closing bracket ']' is missing. Check array indexes and array declarations."),
    (r"^illegal start of expression$",
     "Java found something that cannot begin an expression here. Usually a ')' or '}' is missing or extra on an "
     "earlier line, or a statement was written outside a method."),
    (r"^illegal start of statement$",
     "A statement is incomplete or a '}' is misplaced. Check the line before this one, such as an else or if with "
     "no body."),
    (r"^not a statement$",
     "This line is not something Java can execute on its own (for example a lone value, or a typo like "
     "'x + 1;'). Did you mean an assignment such as 'x = x + 1;'?"),
    (r"^reached end of file while parsing$",
     "A closing brace '}' is missing. Every '{' needs a matching '}'. Check that each method and the class are "
     "closed."),
    (r"^class, interface, enum, or record expected$",
     "There is code outside the class, often caused by an extra '}' earlier. Check that your braces pair up."),
    (r"^unclosed string literal$",
     "A string starts with a quote \" but never ends on the same line. Add the closing quote."),
    (r"^unclosed character literal$|^empty character literal$",
     "Character literals use single quotes and hold exactly one character, like 'a'. Use double quotes for text."),
    (r"^cannot find symbol$", None),  # handled with the symbol: line below
    (r"^incompatible types: possible lossy conversion from (\w+) to (\w+)$",
     "You are storing a value of type {1} in a variable of type {2}, which could lose information. Change "
     "the variable's type to {1}, or cast on purpose, for example ({2}) value."),
    (r"^incompatible types: (\w+) cannot be converted to (\w+)$",
     "The value has type {1} but this place expects type {2}. Check the variable's declared type, the "
     "method's return type, or convert the value (for example Integer.parseInt turns a String into an int)."),
    (r"^incompatible types: (.+)$",
     "The value's type does not match what is expected here ({1}). Compare the declared type with what you assign "
     "or return."),
    (r"^missing return statement$",
     "A method that promises to return a value can reach its end without returning one. Make sure every path, "
     "including every if/else branch, ends with a return."),
    (r"^variable (\w+) might not have been initialized$",
     "'{1}' is used before it has a value. Give it a starting value when you declare it, for example int {1} = 0;"),
    (r"^variable (\w+) is already defined in (.*)$",
     "You declared '{1}' twice in the same scope. Use a different name, or drop the type to reuse the variable "
     "({1} = ...;)."),
    (r"^non-static (method|variable) (.*) cannot be referenced from a static context$",
     "main is static, but '{2}' belongs to an object. Create an object first (for example new Main().{2}), or "
     "mark that {1} static."),
    (r"^unreported exception (\w+); must be caught or declared to be thrown$",
     "This code can throw {1}, and Java makes you handle it. Wrap the call in try/catch, or add 'throws {1}' to "
     "the method signature."),
    (r"^(\w+) cannot be dereferenced$",
     "'{1}' is a primitive type, and primitives have no methods or fields. Use a wrapper (Integer, Double, ...) or "
     "String.valueOf(...) if you need to call a method."),
    (r"^bad operand types for binary operator '(.+)'$",
     "The operator {1} cannot be used with these two types. Check the types in the 'first type / second type' "
     "lines and convert one side."),
    (r"^'else' without 'if'$|^illegal start of type$",
     "An else has no matching if, usually because of a misplaced ';' or '}'. Check the braces around the if "
     "block."),
    (r"^unreachable statement$",
     "This line can never run because something above it always returns, breaks or loops forever. Remove it or "
     "move it."),
    (r"^array required, but (\w+) found$",
     "You used [] on something that is not an array ({1}). For a String use charAt(i); for a list use get(i)."),
    (r"^class (\w+) is public, should be declared in a file named",
     "The program must use the class name Main. Rename your public class to Main."),
    (r"^method (\w+) in class (\w+) cannot be applied to given types;$",
     "'{1}' was called with the wrong number or types of arguments. Compare the call with the method's "
     "declaration; the 'required / found' lines below show the difference."),
    (r"^no suitable (method|constructor) found for (.*)$",
     "None of the versions of {2} accepts the arguments you passed. Check how many arguments you give and "
     "their types."),
    (r"^(\w[\w.<>\[\]]*) is abstract; cannot be instantiated$",
     "{1} is abstract or an interface, so you cannot create it with new. Create one of its concrete subclasses "
     "instead."),
    (r"^(.*) has private access in (\w+)$",
     "{1} is private to {2}. Use a public getter/setter, or make it accessible if that is appropriate."),
]
RULES = [(re.compile(rx), text) for rx, text in RULES]


def _symbol_help(kind: str, name: str) -> str:
    base = name.split("(")[0].strip()
    if kind == "variable":
        return (f"Java does not know a variable called '{base}'. Check the spelling and capital letters, make sure "
                f"it is declared before this line, and that it is declared in a scope that includes this line "
                f"(not inside a different loop or block).")
    if kind == "method":
        return (f"There is no method '{base}' with these arguments. Check the spelling (Java is case-sensitive), "
                f"the number and types of arguments, and that you are calling it on the right object.")
    if kind == "class":
        return (f"Java does not know a type called '{base}'. Check the spelling and capital letters (it is "
                f"String, not string). If it comes from java.util, add an import such as import java.util.*; at "
                f"the top.")
    return f"Java cannot find '{base}'. Check the spelling and that it is declared and imported."


def explain(output: str) -> list:
    """Explain each javac error in `output` that matches one of our rules (at most 5)."""
    lines = (output or "").splitlines()
    results = []
    for i, line in enumerate(lines):
        m = HEADER.match(line)
        if not m:
            continue
        lineno, message = int(m.group(1)), m.group(2).strip()
        text = None
        for rx, template in RULES:
            hit = rx.search(message)
            if not hit:
                continue
            if template is None:  # cannot find symbol: read the 'symbol:' line that follows
                for nxt in lines[i + 1:i + 6]:
                    if HEADER.match(nxt):
                        break
                    sm = SYMBOL.match(nxt)
                    if sm:
                        text = _symbol_help(sm.group(1), sm.group(2))
                        break
                text = text or "Java cannot find a name used on this line. Check spelling, capital letters, " \
                               "declarations and imports."
            else:
                groups = {str(n): g for n, g in enumerate(hit.groups(), start=1)}
                text = template
                for key, val in groups.items():
                    text = text.replace("{" + key + "}", val or "")
            break
        if text:
            results.append({"line": lineno, "message": message, "help": text})
        if len(results) >= 5:
            break
    return results
