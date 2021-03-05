const AbortController = require("node-abort-controller");
const fetch = require("node-fetch");

async function timeoutFetch(resource, options) {
    const {timeout = 8000} = options;

    const controller = new AbortController();
    const id = setTimeout(() => controller.abort(), timeout);

    const response = await fetch(resource, {
        ...options,
        signal: controller.signal
    });
    clearTimeout(id);

    return response;
}

module.exports = {
    timeoutFetch: timeoutFetch
};