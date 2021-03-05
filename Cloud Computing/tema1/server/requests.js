const authorization = require("./authorization");
const fetch = require("node-fetch");
const FormData = require("form-data");
const log = require("../helpers/log");
const myFetch = require("../helpers/fetch");

const basicAuthorization = authorization.getBasicAuthorization();

const timeout = 8000;

const searchPhotoEndpoint = new URL("https://www.flickr.com/services/rest/");
let searchPhotoParameters = {
    method: "flickr.photos.search",
    format: "json",
    nojsoncallback: 1,
    tag_mode: "any",
    sort: "relevance",
    privacy_filter: 1,
    safe_search: 1,
    content_type: 1,
    media: "photos",
    has_geo: 1,
    geo_context: 0,
    in_gallery: true,
    is_getty: true,
    extras: "url_c, date_taken, geo, description, views",
    page: 1,
    per_page: 1
};

const generateHashtagEndpoint = new URL("https://api.imagga.com/v2/tags");
let generateHashtagParameters = {
    limit: 10
};

const uploadPhotoEndpoint = new URL("https://upload.twitter.com/1.1/media/upload.json");

const updateStatusEndpoint = new URL("https://api.twitter.com/1.1/statuses/update.json");

async function searchPhoto(keywords) {
    searchPhotoParameters.tags = keywords;
    for (let parameter in searchPhotoParameters) {
        searchPhotoEndpoint.searchParams.set(parameter, searchPhotoParameters[parameter]);
    }

    const openAuthorization = authorization.getOpenAuthorization("flickr", "GET",
        searchPhotoEndpoint.href, searchPhotoParameters);

    const options = {
        method: "GET",
        headers: {
            "Accept": "application/json",
            "Authorization": openAuthorization
        },
        timeout: timeout
    };

    try {
        const response = await myFetch.timeoutFetch(searchPhotoEndpoint.href, options);

        if (!response.ok) {
            const message = `(${response.status}) ${response.statusText}`;
            log.error(message);
            return null;
        }

        const data = await response.json();
        const photo = data.photos.photo[0];
        return {
            link: photo.url_c,
            description: photo.description,
            date_taken: photo.date_taken,
            latitude: photo.latitude,
            longitude: photo.longitude,
            views: photo.views
        };
    } catch (error) {
        log.error(error.message);
        return null;
    }
}

async function generateHashtags(photo) {
    generateHashtagParameters.image_url = photo;
    for (let parameter in generateHashtagParameters) {
        generateHashtagEndpoint.searchParams.set(parameter, generateHashtagParameters[parameter]);
    }

    const options = {
        method: "GET",
        headers: {
            "Accept": "application/json",
            "Authorization": basicAuthorization
        },
        timeout: timeout
    };

    try {
        const response = await myFetch.timeoutFetch(generateHashtagEndpoint.href, options);

        if (!response.ok) {
            const message = `(${response.status}) ${response.statusText}`;
            log.error(message);
            return null;
        }

        const data = await response.json();
        const tags = data.result.tags;
        let hashtags = [];
        tags.forEach(tag => hashtags.push(tag.tag.en.replace(/ /g, '_')));
        return {
            hashtags: hashtags
        };
    } catch (error) {
        log.error(error.message);
        return null;
    }
}

async function downloadPhoto(photo) {
    const options = {
        method: "GET",
        headers: {
            "Accept": "application/json"
        },
        timeout: timeout
    };

    try {
        const response = await myFetch.timeoutFetch(photo, options);

        if (!response.ok) {
            const message = `(${response.status}) ${response.statusText}`;
            log.error(message);
            return null;
        }

        return await response.buffer();
    } catch (error) {
        log.error(error.message);
        return null;
    }
}

async function uploadPhoto(photo) {
    const openAuthorization = authorization.getOpenAuthorization("twitter", "POST",
        uploadPhotoEndpoint.href, {});

    const headers = new fetch.Headers();
    headers.append("Authorization", openAuthorization);

    const data = new FormData();
    data.append("media", photo);
    data.append("media_category", "tweet_image");

    const options = {
        method: "POST",
        headers: headers,
        body: data,
        timeout: timeout
    };

    try {
        const response = await myFetch.timeoutFetch(uploadPhotoEndpoint.href, options);

        if (!response.ok) {
            const message = `(${response.status}) ${response.statusText}`;
            log.error(message);
            return await response.json();
        }

        const data = await response.json();
        return data.media_id_string;
    } catch (error) {
        log.error(error.message);
        return null;
    }
}

async function updateStatus(photo, hashtags) {
    let content = "";
    hashtags.forEach(hashtag => content += `#${hashtag} `);
    const data = {
        status: content,
        media_ids: photo
    };
    const body = Object.keys(data).map(key => encodeURIComponent(key) + '=' + encodeURIComponent(data[key])).join('&');

    const openAuthorization = authorization.getOpenAuthorization("twitter", "POST",
        updateStatusEndpoint.href, data);

    const options = {
        method: "POST",
        headers: {
            "Authorization": openAuthorization,
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: body,
        timeout: timeout
    };

    try {
        const response = await myFetch.timeoutFetch(updateStatusEndpoint.href, options);

        if (!response.ok) {
            const message = `(${response.status}) ${response.statusText}`;
            log.error(message);
            return null;
        }

        const data = await response.json();
        return {
            link: data.entities.media[0].expanded_url
        };
    } catch (error) {
        log.error(error.message);
        return null;
    }
}

module.exports = {
    searchPhoto: searchPhoto,
    generateHashtags: generateHashtags,
    downloadPhoto: downloadPhoto,
    uploadPhoto: uploadPhoto,
    updateStatus: updateStatus
};