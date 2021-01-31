export function getEl(selector, parent = document) {
    return parent.querySelector(selector);
}

export function getEls(selector, parent = document) {
    return Array.from(parent.querySelectorAll(selector));
}

export function groupElemBy(attr) {
    return (parent) => {
        const bind = getEls(`[${attr}]`, parent).reduce((acc, curr) => {
            const key = pascalToCamel(curr.getAttribute(attr));
            acc[key] = acc[key] ? acc[key] : {};
            acc[key] = curr;
            return acc;
        }, {})
        bind._this = parent;
        return bind;
    }
}

export function stringifyStyle(style) {
    return Object.entries(style).reduce(
        (inline, [key, value]) => (inline += `${camelToPascal(key)}:${value}; `),
        ""
    );
}

export function appendElem(elem, children) {
    return Array.from(Array.isArray(children) ? children : [children]).reduce((elem, child) => {
        elem.appendChild(child);
        return elem;
    }, elem);
}

export function pascalToCamel(str) {
    return str.toLowerCase()
        .replace(/-[a-z]/g, (s) => s.toUpperCase()[1])
}

export function camelToPascal(str) {
    return str.replace(/[A-Z]/g, (s) => `-${s.toLowerCase()}`);
}

export function createElem(tag, attrs, init) {
    let elem = document.createElement(tag);
    elem = attrs ? setAttr(elem, attrs) : elem;
    if (init) init(elem);
    return elem;
}

export function setAttr(elem, attrs) {
    return Object.entries(attrs).reduce((elem, [key, value], i) => {
        const _key = camelToPascal(key);
        if (Array.isArray(value)) {
            elem.setAttribute(_key, value.join(" "));
        } else if (typeof value === "object") {
            elem.setAttribute(_key, stringifyStyle(value));
        } else if (key === 'text') {
            elem.textContent = value;
        } else {
            elem.setAttribute(_key, value);
        }
        return elem;
    }, elem);
}


