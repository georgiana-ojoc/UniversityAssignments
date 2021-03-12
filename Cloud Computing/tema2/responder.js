const database = require("./database");
const {isEmpty} = require("./helper");

const scheme = "http://";
const host = "localhost";
const port = 3000;

const address = scheme + host + ':' + port;

function respond(response, code, location = null, message = null, content = null) {
    const body = {};
    if (message) {
        body.message = message;
    }
    if (content) {
        body.content = content;
    }

    response.statusCode = code;
    response.setHeader("Access-Control-Allow-Origin", '*');
    response.setHeader("Access-Control-Allow-Headers", '*');
    response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE");
    if (location) {
        response.setHeader("Location", location);
    }

    if (!isEmpty(body)) {
        response.write(JSON.stringify(body));
    }

    response.end();
}

function post(collection, body, response, name) {
    database.insertOne(collection, body).then(identifier => {
        if (identifier === null) {
            respond(response, 500, null, "Database error", null);
        } else {
            respond(response, 201, address + '/' + name + '/' + identifier, null, null);
        }
    });
}

function update(collection, identifier, body, response) {
    database.updateOne(collection, identifier, body).then(number => {
        if (number === null) {
            respond(response, 500, null, "Database error", null);
        } else if (number === 0) {
            respond(response, 204, null, null, null);
        } else {
            respond(response, 200, null, null, null);
        }
    });
}

function deleteResource(collection, identifier, response) {
    database.deleteOne(collection, identifier).then(number => {
        if (number === null) {
            respond(response, 500, null, "Database error", null);
        } else if (number === 0) {
            respond(response, 204, null, null, null);
        } else {
            respond(response, 200, null, null, null);
        }
    });
}

module.exports = {
    respond,
    post,
    update,
    deleteResource
};