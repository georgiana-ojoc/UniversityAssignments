const database = require("./database");
const {isEmpty} = require("./helper");
const {ObjectId} = require("mongodb");
const responder = require("./responder");

function isBook(object) {
    if (isEmpty(object)) {
        return false;
    }
    let hasTitle = false;
    let hasAuthor = false;
    let hasYear = false;
    for (const [key, value] of Object.entries(object)) {
        if (key === "title") {
            hasTitle = true;
            continue;
        }
        if (key === "author") {
            hasAuthor = true;
            continue;
        }
        if (key === "year") {
            if (typeof value === "number") {
                if (Number.isInteger(value) && value >= 0 && value <= 2021) {
                    hasYear = true;
                    continue;
                }
            }
        }
        return false;
    }
    return hasTitle && hasAuthor && hasYear;
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
            case "title":
                query.title = value;
                break;
            case "author":
                query.author = value;
                break;
            case "year":
                const year = Number(value);
                if (Number.isInteger(year) && year >= 0 && year <= 2021) {
                    query.year = year;
                } else {
                    responder.respond(response, 400, null, "Year must be integer in [0, 2021]).",
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
                    case "title":
                        options.sort.title = 1;
                        break;
                    case "-title":
                        options.sort.title = -1;
                        break;
                    case "author":
                        options.sort.author = 1;
                        break;
                    case "-author":
                        options.sort.author = -1;
                        break;
                    case "year":
                        options.sort.year = 1;
                        break;
                    case "-year":
                        options.sort.year = -1;
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
            responder.respond(response, 200, null, "Here are the books.", content);
        } else {
            responder.respond(response, 404, null, "No book found", null);
        }
    });
}

function getOne(collection, identifier, response) {
    database.findOne(collection, identifier).then(book => {
        if (book === null) {
            responder.respond(response, 404, null, "Book not found", null);
        } else {
            responder.respond(response, 200, null, "Here is the book.", book);
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
                responder.respond(response, 400, null, "Invalid body. Must contain title " +
                    "(string), author (string) and year (integer in [0, 2021]).", null);
                return;
            }
            if (!isBook(body)) {
                responder.respond(response, 400, null, "Invalid body. Must contain title " +
                    "(string), author (string) and year (integer in [0, 2021]).", null);
                return;
            }
            if (identifier) {
                database.exists(collection, identifier).then(value => {
                    if (value) {
                        responder.respond(response, 409, null, "Book already exists", null);
                        return;
                    }
                    body = {
                        _id: identifier,
                        ...body
                    };
                    responder.post(collection, body, response, "books");
                });
            } else {
                responder.post(collection, body, response, "books");
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
                responder.respond(response, 400, null, "Invalid body. Must contain title " +
                    "(string), author (string) and year (integer in [0, 2021]).", null);
                return;
            }
            if (!isBook(body)) {
                responder.respond(response, 400, null, "Invalid body. Must contain title " +
                    "(string), author (string) and year (integer in [0, 2021]).", null);
                return;
            }
            database.exists(collection, identifier).then(value => {
                if (!value) {
                    responder.respond(response, 404, null, "Book not found", null);
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
                responder.respond(response, 400, null, "Invalid body. Must contain title " +
                    "(string) or author (string) or year (integer in [0, 2021]).", null);
                return;
            }
            for (const [key, value] of Object.entries(body)) {
                if (key === "title") {
                    continue;
                }
                if (key === "author") {
                    continue;
                }
                if (key === "year") {
                    if (typeof value === "number") {
                        if (Number.isInteger(value) && value >= 0 && value <= 2021) {
                            continue;
                        }
                    }
                }
                responder.respond(response, 400, null, "Invalid body. Must contain title " +
                    "(string) or author (string) or year (integer in [0, 2021]).", null);
                return;
            }
            database.exists(collection, identifier).then(value => {
                if (!value) {
                    responder.respond(response, 404, null, "Book not found", null);
                    return;
                }
                responder.update(collection, identifier, body, response);
            });
        });
}

function deleteBook(collection, identifier, response) {
    database.exists(collection, identifier).then(value => {
        if (!value) {
            responder.respond(response, 404, null, "Book not found", null);
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
    deleteBook
}