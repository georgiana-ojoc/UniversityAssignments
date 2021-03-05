const debug = true;

const timeout = 8000;

const server = "http://localhost:3000"

const searchPhotoEndpoint = new URL(server + "/photo");
const generateHashtagsEndpoint = new URL(server + "/hashtags");
const tweetEndpoint = new URL(server + "/tweet");
const getMetricsEndpoint = new URL(server + "/metrics");

const keywordsElement = document.getElementById("keywords");
const searchPhotoElement = document.getElementById("search-photo");

const photoMessageElement = document.getElementById("photo-message");
const photoElement = document.getElementById("photo");

const generateHashtagsElement = document.getElementById("generate-hashtags");

const hashtagsMessageElement = document.getElementById("hashtags-message");
const hashtagElements = document.getElementsByClassName("hashtag");

const tweetElement = document.getElementById("tweet");

const tweetMessageElement = document.getElementById("tweet-message");
const tweetLinkElement = document.getElementById("tweet-link");

const parallelElement = document.getElementById("parallel");

const batchElements = document.getElementsByClassName("batch");
const tweetsMessageElement = document.getElementById("tweets-message");
const tweetsLinkElement = document.getElementById("tweets-link");

const getMetricsElement = document.getElementById("get_metrics");

const metricsMessageElement = document.getElementById("metrics-message");
const metricElements = document.getElementsByClassName("metric");

const log = {
    information: function (information) {
        console.log("Information: " + information + '\n')
    },
    warning: function (warning) {
        console.warn("Warning: " + warning + '\n')
    },
    error: function (error) {
        console.error("Error: " + error + '\n')
    },
}

function display(elements, value) {
    for (let element of elements) {
        element.style.display = value;
    }
}

function getKeywords() {
    let keywords = keywordsElement.value;
    keywords = keywords.replace(/[`~!@#$%^&*()\-_=+\[{\]};:'"\\|,<.>/?]/g, ' ');
    keywords = keywords.replace(/\s+/g, ", ");
    if (keywords === "") {
        keywords = "Bucharest, night";
    }
    return keywords;
}

function getPhoto() {
    return photoElement.getAttribute("src");
}

function getHashtags() {
    let hashtags = [];
    for (let index = 0; index < hashtagElements.length; index++) {
        hashtags.push(hashtagElements[index].innerText);
    }
    return hashtags;
}

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

async function searchPhoto(keywords) {
    searchPhotoEndpoint.searchParams.set("keywords", keywords);
    if (debug) {
        log.information("GET " + searchPhotoEndpoint.href);
    }

    const options = {
        method: "GET",
        headers: {
            "Accept": "application/json"
        },
        timeout: timeout
    };

    try {
        const response = await timeoutFetch(searchPhotoEndpoint.href, options);
        return await response.json();
    } catch (error) {
        if (debug) {
            log.error(error.message);
        }
        return null;
    }
}

async function generateHashtags(photo) {
    generateHashtagsEndpoint.searchParams.set("photo_url", photo);
    if (debug) {
        log.information("GET " + generateHashtagsEndpoint.href);
    }

    const options = {
        method: "GET",
        headers: {
            "Accept": "application/json"
        },
        timeout: timeout
    };

    try {
        const response = await timeoutFetch(generateHashtagsEndpoint.href, options);
        return await response.json();
    } catch (error) {
        if (debug) {
            log.error(error.message);
        }
        return null;
    }
}

async function tweet(photo, hashtags) {
    if (debug) {
        log.information("POST " + tweetEndpoint.href);
    }

    let body = {
        photo_url: photo,
        hashtags: hashtags
    };

    const options = {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(body),
        timeout: timeout
    };

    try {
        const response = await timeoutFetch(tweetEndpoint.href, options);
        return await response.json();
    } catch (error) {
        if (debug) {
            log.error(error.message);
        }
        return null;
    }
}

async function parallel(photo, hashtags) {
    if (debug) {
        log.information("POST " + tweetEndpoint.href);
    }

    let body = {
        photo_url: photo,
        hashtags: hashtags
    };

    const options = {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(body),
        timeout: timeout
    };

    try {
        let promises = [];
        for (let index = 0; index < 5; index++) {
            promises.push(timeoutFetch(tweetEndpoint.href, options));
        }
        const responses = await Promise.all(promises);
        let data = [];
        for (let response of responses) {
            data.push(await response.json());
        }
        return data;
    } catch (error) {
        if (debug) {
            log.error(error.message);
        }
        return null;
    }
}

async function getMetrics() {
    if (debug) {
        log.information("GET " + getMetricsEndpoint.href);
    }

    const options = {
        method: "GET",
        headers: {
            "Accept": "application/json"
        },
        timeout: timeout
    };

    try {
        const response = await timeoutFetch(getMetricsEndpoint.href, options);
        return await response.json();
    } catch (error) {
        if (debug) {
            log.error(error.message);
        }
        return null;
    }
}

async function photoListener() {
    photoMessageElement.innerText = "";
    photoElement.style.display = "none";

    const keywords = getKeywords();
    const data = await searchPhoto(keywords);

    if (data === null) {
        photoMessageElement.innerText = "Could not search photo.";
    } else {
        if (debug) {
            log.information(JSON.stringify(data));
        }

        photoMessageElement.innerText = data.message;
        if (data.hasOwnProperty("link")) {
            photoElement.setAttribute("src", data.link);
            photoElement.style.display = "flex";
        }
    }
}

async function hashtagsListener() {
    hashtagsMessageElement.innerText = "";
    display(hashtagElements, "none");

    const photo = getPhoto();
    const data = await generateHashtags(photo);

    if (data === null) {
        hashtagsMessageElement.innerText = "Could not generate hashtags.";
    } else {
        if (debug) {
            log.information(JSON.stringify(data));
        }

        hashtagsMessageElement.innerText = data.message;
        if (data.hasOwnProperty("hashtags")) {
            for (let index = 0; index < data.hashtags.length; index++) {
                hashtagElements[index].innerText = data.hashtags[index];
            }
            display(hashtagElements, "flex");
        }
    }
}

async function tweetListener() {
    tweetMessageElement.innerText = "";
    tweetLinkElement.style.display = "none";

    const photo = getPhoto();
    const hashtags = getHashtags();
    const data = await tweet(photo, hashtags);

    if (data === null) {
        tweetMessageElement.innerText = "Could not tweet.";
    } else {
        if (debug) {
            log.information(JSON.stringify(data));
        }

        tweetMessageElement.innerText = data.message;
        if (data.hasOwnProperty("link")) {
            tweetLinkElement.setAttribute("href", data.link);
            tweetLinkElement.style.display = "flex";
        }
    }
}

async function parallelListener() {
    display(batchElements, "none");
    tweetsMessageElement.innerText = "";
    tweetsLinkElement.style.display = "none";

    const photo = getPhoto();
    const hashtags = getHashtags();

    for (let index = 0; index < 5; index++) {
        const start = Date.now();
        const data = await parallel(photo, hashtags);
        const end = Date.now();

        if (data === null) {
            batchElements[index].innerText = "Could not tweet 5 times.";
        } else {
            if (debug) {
                log.information(JSON.stringify(data));
            }

            batchElements[index].innerText = "Latency (in seconds): " + ((end - start) / 1000).toFixed(3);
        }
        batchElements[index].style.display = "flex";
    }
    tweetsMessageElement.innerText = "Here are the tweets.";
    tweetsLinkElement.style.display = "flex";
}

async function metricsListener() {
    metricsMessageElement.innerText = "";
    display(metricElements, "none");

    const data = await getMetrics();
    if (data === null) {
        metricsMessageElement.innerText = "Could not get metrics.";
    } else {
        if (debug) {
            log.information(JSON.stringify(data));
        }

        metricsMessageElement.innerText = data.message;
        metricElements[0].innerText = "Requests per minute: " + data.requestsPerMinute;
        metricElements[1].innerText = "Minimum latency (in seconds): " + data.minimumLatency;
        metricElements[2].innerText = "Maximum latency (in seconds): " + data.maximumLatency;
        metricElements[3].innerText = "Average latency (in seconds): " + data.averageLatency;
        metricElements[4].innerText = "GET requests: " + data.getRequests;
        metricElements[5].innerText = "POST requests: " + data.postRequests;
        metricElements[6].innerText = "200 responses: " + data.okResponses;
        metricElements[7].innerText = "201 responses: " + data.createdResponses;
        metricElements[8].innerText = "400 responses: " + data.badResponses;
        metricElements[9].innerText = "500 responses: " + data.errorResponses;
        display(metricElements, "flex");
    }
}

photoListener().then(_ => hashtagsListener().then(_ => tweetListener().then()));

searchPhotoElement.addEventListener("click", photoListener);
generateHashtagsElement.addEventListener("click", hashtagsListener);
tweetElement.addEventListener("click", tweetListener);
parallelElement.addEventListener("click", parallelListener);
getMetricsElement.addEventListener("click", metricsListener);