const fs = require("fs");
const log = require("../helpers/log");

let index = 0;

function getFormattedTimestamp(timestamp) {
    function pad(number, length) {
        return ('0'.repeat(length - 1) + number).slice(-length);
    }

    const date = new Date(timestamp);

    const day = pad(date.getDate(), 2);
    const month = pad(date.getMonth() + 1, 2);
    const year = date.getFullYear();

    const hours = pad(date.getHours(), 2);
    const minutes = pad(date.getMinutes(), 2);
    const seconds = pad(date.getSeconds(), 2);
    const milliseconds = pad(date.getMilliseconds(), 4);

    return day + '.' + month + '.' + year + ' ' + hours + ':' + minutes + ':' + seconds + ':' + milliseconds;
}

function callback(error) {
    if (error) {
        log.error(error.message);
    }
}

function start() {
    const line = "Start: " + getFormattedTimestamp(Date.now()) + '\n';
    fs.appendFile("../information/logs.txt", line, (error) => callback(error));
}

async function information(timestamp, request, response, requestBody = null, responseBody = null,
                           requestError = null, responseError = null) {
    response.on("finish", () => {
        const latency = (Date.now() - timestamp) / 1000;

        const {rawHeaders, httpVersion, method, socket, url} = request;
        const {remoteAddress, remoteFamily} = socket;

        const {statusCode, statusMessage} = response;
        const headers = response.getHeaders();

        const requestInformation = {
            rawHeaders,
            httpVersion,
            method,
            remoteAddress,
            remoteFamily,
            url,
            body: requestBody,
            error: requestError
        }

        const responseInformation = {
            statusCode,
            statusMessage,
            headers,
            body: responseBody,
            error: responseError
        }

        const information = {
            timestamp: getFormattedTimestamp(timestamp),
            latency,
            request: requestInformation,
            response: responseInformation
        }

        let line = ++index + ". " + JSON.stringify(information) + '\n';
        fs.appendFile("../information/logs.txt", line, (error) => callback(error));

        if (method !== "OPTIONS") {
            line = timestamp + '\t' + latency + '\t' + method + '\t' + statusCode + '\n';
            fs.appendFile("../information/metrics.txt", line, (error) => callback(error));
        }
    });
}

module.exports = {
    start: start,
    information: information
}