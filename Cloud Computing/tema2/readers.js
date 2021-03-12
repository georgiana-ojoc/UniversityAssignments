const database = require("./database");
const {isEmpty} = require("./helper");
const {ObjectId} = require("mongodb");
const responder = require("./responder");

function isReader(object) {
    if (isEmpty(object)) {
        return false;
    }
    let hasName = false;
    let hasAge = false;
    for (const [key, value] of Object.entries(object)) {
        if (key === "name") {
            hasName = true;
            continue;
        }
        if (key === "age") {
            if (typeof value === "number") {
                if (Number.isInteger(value) && value >= 0 && value <= 150) {
                    hasAge = true;
                    continue;
                }
            }
        }
        return false;
    }
    return hasName && hasAge;
}

function getAll(collection, response, parameters) {
    let query = {};
    let options = {};
    for (const [key, value] of parameters.entries()) {
        switch (key) {
            case "id":
                if (!database.isValid(value)) {
                    responder.respond(response, 400, null, "Invalid identifier", null);
                    return;
                }
                query._id = new ObjectId(value);
                break;
            case "name":
                query.name = value;
                break;
            case "age":
                const age = Number(value);
                if (Number.isInteger(age) && age >= 0 && age <= 150) {
                    query.age = age;
                } else {
                    responder.respond(response, 400, null, "Age must be integer in [0, 150]).",
                        null);
                    return;
                }
                break;
            case "sort_by":
                options.sort = {};
                switch (value) {
                    case "id":
                        options.sort._id = 1;
                        break;
                    case "-id":
                        options.sort._id = -1;
                        break;
                    case "name":
                        options.sort.name = 1;
                        break;
                    case "-name":
                        options.sort.name = -1;
                        break;
                    case "age":
                        options.sort.age = 1;
                        break;
                    case "-age":
                        options.sort.age = -1;
                        break;
                    default:
                        delete options.sort;
                        responder.respond(response, 400, null, "Invalid sort option", null);
                        return;
                }
                break;
            case "skip":
                const skip = Number(value);
                if (Number.isInteger(skip) && skip >= 0) {
                    options.skip = skip;
                } else {
                    responder.respond(response, 400, null, "Invalid skip value", null);
                    return;
                }
                break;
            case "limit":
                const limit = Number(value);
                if (Number.isInteger(limit)) {
                    options.limit = limit;
                } else {
                    responder.respond(response, 400, null, "Invalid limit value", null);
                    return;
                }
                break;
            default:
                responder.respond(response, 400, null, "Invalid parameter", null);
                return;
        }
    }
    database.find(collection, query, options).then(content => {
        if (content === null) {
            responder.respond(response, 500, null, "Database error", null);
        } else if (Array.isArray(content) && content.length) {
            responder.respond(response, 200, null, "Here are the readers.", content);
        } else {
            responder.respond(response, 404, null, "No reader found", null);
        }
    });
}

function getOne(collection, identifier, response) {
    database.findOne(collection, identifier).then(reader => {
        if (reader === null) {
            responder.respond(response, 404, null, "Reader not found", null);
        } else {
            responder.respond(response, 200, null, "Here is the reader.", reader);
        }
    });
}

function post(collection, identifier = null, request, response) {
    let body = [];
    request.on("data", (chunk) => body.push(chunk))
        .on("end", function () {
            body = Buffer.concat(body).toString();
            try {
                body = JSON.parse(body);
            } catch (error) {
                responder.respond(response, 400, null,
                    "Invalid body. Must contain name (string) and age (integer in [0, 150]).", null);
                return;
            }
            if (!isReader(body)) {
                responder.respond(response, 400, null,
                    "Invalid body. Must contain name (string) and age (integer in [0, 150]).", null);
                return;
            }
            if (identifier) {
                database.exists(collection, identifier).then(value => {
                    if (value) {
                        responder.respond(response, 409, null, "Reader already exists",
                            null);
                        return;
                    }
                    body = {
                        _id: identifier,
                        ...body
                    };
                    responder.post(collection, body, response, "readers");
                });
            } else {
                responder.post(collection, body, response, "readers");
            }
        });
}

function put(collection, identifier, request, response) {
    let body = [];
    request.on("data", (chunk) => body.push(chunk))
        .on("end", function () {
            body = Buffer.concat(body).toString();
            try {
                body = JSON.parse(body);
            } catch (error) {
                responder.respond(response, 400, null,
                    "Invalid body. Must contain name (string) and age (integer in [0, 150]).", null);
                return;
            }
            if (!isReader(body)) {
                responder.respond(response, 400, null,
                    "Invalid body. Must contain name (string) and age (integer in [0, 150]).", null);
                return;
            }
            database.exists(collection, identifier).then(value => {
                if (!value) {
                    responder.respond(response, 404, null, "Reader not found", null);
                    return;
                }
                responder.update(collection, identifier, body, response);
            });
        });
}

function patch(collection, identifier, request, response) {
    let body = [];
    request.on("data", (chunk) => body.push(chunk))
        .on("end", function () {
            body = Buffer.concat(body).toString();
            try {
                body = JSON.parse(body);
            } catch (error) {
                responder.respond(response, 400, null,
                    "Invalid body. Must contain name (string) or age (integer in [0, 150]).", null);
                return;
            }
            for (const [key, value] of Object.entries(body)) {
                if (key === "name") {
                    continue;
                }
                if (key === "age") {
                    if (typeof value === "number") {
                        if (Number.isInteger(value) && value >= 0 && value <= 150) {
                            continue;
                        }
                    }
                }
                responder.respond(response, 400, null,
                    "Invalid body. Must contain name (string) or age (integer in [0, 150]).", null);
                return;
            }
            database.exists(collection, identifier).then(value => {
                if (!value) {
                    responder.respond(response, 404, null, "Reader not found", null);
                    return;
                }
                responder.update(collection, identifier, body, response);
            });
        });
}

function deleteReader(collection, identifier, response) {
    database.exists(collection, identifier).then(value => {
        if (!value) {
            responder.respond(response, 404, null, "Reader not found", null);
            return;
        }
        responder.deleteResource(collection, identifier, response);
    });
}

module.exports = {
    getAll,
    getOne,
    post,
    put,
    patch,
    deleteReader
}