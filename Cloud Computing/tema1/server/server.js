const fs = require("fs");
const http = require("http");
const log = require("../helpers/log");
const logger = require("./logger");
const requests = require("./requests");

const scheme = "http://";
const host = "localhost";
const port = 3000;

const address = scheme + host + ':' + port;

let templateKeywords = fs.readFileSync("../data/keywords.txt");
templateKeywords = templateKeywords.toString();

let templatePhoto = fs.readFileSync("../data/photo.json");
templatePhoto = JSON.parse(templatePhoto.toString());

let templateHashtags = fs.readFileSync("../data/hashtags.json");
templateHashtags = JSON.parse(templateHashtags.toString());

let templateTweet = fs.readFileSync("../data/tweet.json");
templateTweet = JSON.parse(templateTweet.toString());

const pattern = new RegExp("^[0-9a-zA-Z]+(, [0-9a-zA-Z]+)*$");

logger.start();

function respond(timestamp, request, response, requestBody, code, message, content, requestError) {
    const responseBody = {
        message: message,
        ...content
    }

    let responseError = null;
    response.on("error", (error) => {
        responseError = error.message;
        log.error(responseError);
    });

    response.statusCode = code;
    response.setHeader("Access-Control-Allow-Origin", '*');
    response.setHeader("Access-Control-Allow-Headers", '*');
    response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
    response.write(JSON.stringify(responseBody));

    logger.information(timestamp, request, response, requestBody, responseBody, requestError, responseError).then();

    response.end();
}

function searchPhotoResolver(timestamp, request, response, requestBody, data, requestError) {
    if (data === null) {
        respond(timestamp, request, response, requestBody, 500, "Could not search photo on Flickr.",
            {}, requestError);
    } else {
        respond(timestamp, request, response, requestBody, 200, "Here is the photo.", data, requestError);
    }
}

function generateHashtagsResolver(timestamp, request, response, requestBody, data, requestError) {
    if (data === null) {
        respond(timestamp, request, response, requestBody, 500, "Could not generate tags on Imagga.",
            {}, requestError);
    } else {
        respond(timestamp, request, response, requestBody, 200, "Here are the hashtags.", data,
            requestError);
    }
}

function updateStatusResolver(timestamp, request, response, requestBody, data, requestError) {
    if (data === null) {
        respond(timestamp, request, response, requestBody, 500, "Could not update status on Twitter.",
            {}, requestError);
    } else {
        respond(timestamp, request, response, requestBody, 201, "Here is the tweet.", data, requestError);
    }
}

function uploadPhotoResolver(timestamp, request, response, requestBody, data, hashtags, requestError) {
    if (data === null) {
        respond(timestamp, request, response, requestBody, 500, "Could not upload photo on Twitter.",
            {}, requestError);
    } else {
        requests.updateStatus(data, hashtags).then(data => updateStatusResolver(timestamp, request, response,
            requestBody, data, requestError));
    }
}

function downloadPhotoResolver(timestamp, request, response, requestBody, data, hashtags, requestError) {
    if (data === null) {
        respond(timestamp, request, response, requestBody, 500, "Could not download photo.", {},
            requestError);
    } else {
        requests.uploadPhoto(data).then(data => uploadPhotoResolver(timestamp, request, response, requestBody, data,
            hashtags, requestError));
    }
}

function getMetrics(data) {
    let lines = data.split('\n');
    const total = lines.length - 1;

    if (total === 0) {
        return {
            requestsPerMinute: 0,
            minimumLatency: 0,
            maximumLatency: 0,
            averageLatency: 0,
            getRequests: 0,
            postRequests: 0,
            okResponses: 0,
            createdResponses: 0,
            badResponses: 0,
            errorResponses: 0
        };
    }

    const timestamps = [];
    const latencies = [];
    const methods = [];
    const codes = [];

    for (let index = 0; index < total; index++) {
        const line = lines[index].split("\t");
        timestamps.push(parseInt(line[0]));
        latencies.push(parseFloat(line[1]));
        methods.push(line[2]);
        codes.push(parseInt(line[3]));
    }

    const interval = ((timestamps[total - 1] - timestamps[0]) / 1000 + latencies[total - 1]) / 60;
    const requestsPerMinute = (total / interval).toFixed(3);

    const minimumLatency = Math.min(...latencies).toFixed(3);
    const maximumLatency = Math.max(...latencies).toFixed(3);

    let sum = 0;
    latencies.forEach(latency => sum += latency);
    const averageLatency = (sum / total).toFixed(3);

    let getRequests = 0;
    let postRequests = 0;
    methods.forEach(method => {
        if (method === "GET") {
            getRequests++;
        } else if (method === "POST") {
            postRequests++;
        }
    });

    let okResponses = 0;
    let createdResponses = 0;
    let badResponses = 0;
    let errorResponses = 0;
    codes.forEach(code => {
        if (code === 200) {
            okResponses++;
        } else if (code === 201) {
            createdResponses++;
        } else if (code === 400) {
            badResponses++;
        } else if (code === 500) {
            errorResponses++;
        }
    });

    return {
        requestsPerMinute,
        minimumLatency,
        maximumLatency,
        averageLatency,
        getRequests,
        postRequests,
        okResponses,
        createdResponses,
        badResponses,
        errorResponses
    };
}

function sendMetricsCallback(error, data, timestamp, request, response, requestError) {
    if (error) {
        log.error(error.message);
        return;
    }

    const metrics = getMetrics(data);
    respond(timestamp, request, response, null, 200, "Here are the metrics.", metrics,
        requestError);
}

function listener(request, response) {
    const timestamp = Date.now();

    let requestError = null;
    request.on("error", (error) => {
        requestError = error.message;
        log.error(requestError);
    });

    log.information(`${request.method} ${address}${request.url}`);
    const url = new URL(address + request.url);

    if (url.pathname === "/photo" && request.method === "GET") {
        if (!url.searchParams.has("keywords")) {
            respond(timestamp, request, response, null, 400,
                "Specify comma-separated list of keywords.", {}, requestError);
        } else {
            const keywords = url.searchParams.get("keywords");
            if (!pattern.test(keywords)) {
                respond(timestamp, request, response, null, 400,
                    "Specify comma-separated list of keywords.", {}, requestError);
            } else if (keywords === templateKeywords) {
                respond(timestamp, request, response, null, 200, "Here is the photo.",
                    templatePhoto, requestError);
            } else {
                requests.searchPhoto(keywords)
                    .then(data => searchPhotoResolver(timestamp, request, response, null, data,
                        requestError));
            }
        }
    } else if (url.pathname === "/hashtags" && request.method === "GET") {
        if (!url.searchParams.has("photo_url")) {
            respond(timestamp, request, response, null, 400, "Specify photo URL.", {},
                requestError);
        } else {
            const photo = url.searchParams.get("photo_url");
            if (photo === templatePhoto["link"]) {
                respond(timestamp, request, response, null, 200, "Here are the hashtags.",
                    templateHashtags, requestError);
            } else {
                requests.generateHashtags(photo)
                    .then(data => generateHashtagsResolver(timestamp, request, response, null, data, requestError));
            }
        }
    } else if (url.pathname === "/tweet" && request.method === "POST") {
        let body = [];
        request.on("data", (chunk) => body.push(chunk))
            .on("end", function () {
                body = Buffer.concat(body).toString();
                try {
                    body = JSON.parse(body);
                } catch (error) {
                    respond(timestamp, request, response, body, 400, "Invalid body.", {},
                        requestError);
                    return;
                }
                let photo = body.photo_url;
                if (!photo) {
                    respond(timestamp, request, response, body, 400, "Specify photo URL.", {},
                        requestError);
                } else {
                    let hashtags = body.hashtags;
                    if (!hashtags) {
                        respond(timestamp, request, response, body, 400, "Specify hashtags.", {},
                            requestError);
                    } else if (photo === templatePhoto["link"]
                        && JSON.stringify(hashtags) === JSON.stringify(templateHashtags["hashtags"])) {
                        respond(timestamp, request, response, body, 201, "Here is the tweet.",
                            templateTweet, requestError);
                    } else {
                        requests.downloadPhoto(photo).then(data => downloadPhotoResolver(timestamp, request, response,
                            body, data, hashtags, requestError));
                    }
                }
            });
    } else if (url.pathname === "/metrics" && request.method === "GET") {
        fs.readFile("../information/metrics.txt",
            (error, data) => sendMetricsCallback(error, data.toString(), timestamp,
                request, response, requestError));
    } else if (request.method === "OPTIONS") {
        respond(timestamp, request, response, null, 200, "Allow.", {}, requestError);
    } else {
        respond(timestamp, request, response, null, 400, "Invalid request.", {},
            requestError);
    }
}

const server = http.createServer(listener);
server.listen(port, host, () => {
    log.information(`Server running at ${address}.`);
});