const crypto = require("crypto");
const fs = require("fs");

let keys = fs.readFileSync("./keys.json");
keys = JSON.parse(keys.toString());

const getOpenAuthorization = function (service, method, url, parameters) {
    let apiKeys = {};
    if (service === "flickr") {
        apiKeys = keys["flickr"];
    } else if (service === "twitter") {
        apiKeys = keys["twitter"];
    } else {
        return null;
    }

    const consumerKey = apiKeys["consumer_key"];
    const consumerSecret = apiKeys["consumer_secret"];
    const accessToken = apiKeys["access_token"];
    const tokenSecret = apiKeys["token_secret"];

    const timestamp = Math.round(Date.now() / 1000);
    const nonce = Buffer.from(consumerKey + ':' + timestamp).toString("base64");

    let baseParameters = {};
    Object.assign(baseParameters, parameters);
    baseParameters["oauth_consumer_key"] = consumerKey;
    baseParameters["oauth_token"] = accessToken;
    baseParameters["oauth_timestamp"] = timestamp;
    baseParameters["oauth_nonce"] = nonce;
    baseParameters["oauth_signature_method"] = "HMAC-SHA1";
    baseParameters["oauth_version"] = "1.0";

    let encodedBaseParameters = {};
    for (let key in baseParameters) {
        encodedBaseParameters[encodeURIComponent(key)] = encodeURIComponent(baseParameters[key])
            .replace(/!/g, "%21");
    }

    let orderedParameters = {};
    Object.keys(encodedBaseParameters).sort().forEach(function (key) {
        orderedParameters[key] = encodedBaseParameters[key];
    });

    let encodedParameters = "";
    for (let key in orderedParameters) {
        if (encodedParameters === "") {
            encodedParameters += `${key}=${orderedParameters[key]}`;
        } else {
            encodedParameters += `&${key}=${orderedParameters[key]}`;
        }
    }

    if (!(method === "GET" || method === "POST")) {
        return null;
    }
    url = encodeURIComponent(url);
    encodedParameters = encodeURIComponent(encodedParameters);
    const base = `${method}&${url}&${encodedParameters}`;

    const key = encodeURIComponent(consumerSecret) + '&' + encodeURIComponent(tokenSecret);

    let signature = crypto.createHmac("sha1", key).update(base).digest()
        .toString("base64");
    signature = encodeURIComponent(signature);

    return "OAuth " +
        'oauth_consumer_key="' + consumerKey + '", ' +
        'oauth_nonce="' + nonce + '", ' +
        'oauth_signature="' + signature + '", ' +
        'oauth_signature_method="HMAC-SHA1", ' +
        'oauth_timestamp="' + timestamp + '", ' +
        'oauth_token="' + accessToken + '", ' +
        'oauth_version="1.0"'
}

function getBasicAuthorization() {
    return "Basic " + keys["imagga"];
}

module.exports = {
    getOpenAuthorization: getOpenAuthorization,
    getBasicAuthorization: getBasicAuthorization
};