const database = require("./database");
const {isEmpty} = require("./helper");
const {ObjectId} = require("mongodb");
const responder = require("./responder");

function isIdentifier(object, type) {
    if (isEmpty(object)) {
        return false;
    }
    let hasIdentifier = false;
    for (const [key, value] of Object.entries(object)) {
        if (key === type + "_id" && database.isValid(value)) {
            hasIdentifier = true;
            continue;
        }
        return false;
    }
    return hasIdentifier;
}

async function getBooks(collection, content) {
    let books = [];
    for (const record of content) {
        await database.findOne(collection, new ObjectId(record.book_id)).then(book => {
            if (book && !isEmpty(book)) {
                books.push(book);
            }
        });
    }
    return books;
}

async function getReaders(collection, content) {
    let readers = [];
    for (const record of content) {
        await database.findOne(collection, new ObjectId(record.reader_id)).then(reader => {
            if (reader && !isEmpty(reader)) {
                readers.push(reader);
            }
        });
    }
    return readers;
}

function get(readerCollection, bookCollection, registerCollection, identifier, request, response, type) {
    let registerQuery = {};
    if (type === "book") {
        registerQuery.reader_id = identifier;
    } else {
        registerQuery.book_id = identifier;
    }
    database.find(registerCollection, registerQuery, {}).then(content => {
        if (content === null) {
            responder.respond(response, 500, null, "Database error", null);
        } else if (Array.isArray(content) && content.length) {
            if (type === "book") {
                getBooks(bookCollection, content).then(books =>
                    responder.respond(response, 200, null, "Here are the " + type + "s.", books));
            } else {
                getReaders(readerCollection, content).then(readers =>
                    responder.respond(response, 200, null, "Here are the " + type + "s.", readers));
            }
        } else {
            responder.respond(response, 404, null, "No " + type + " found", null);
        }
    });
}

function post(readerCollection, bookCollection, registerCollection, identifier, request, response, type) {
    let body = [];
    request.on("data", (chunk) => body.push(chunk))
        .on("end", function () {
            body = Buffer.concat(body).toString();
            try {
                body = JSON.parse(body);
            } catch (error) {
                responder.respond(response, 400, null, "Invalid body. Must contain " + type +
                    " identifier (string).", null);
                return;
            }
            if (!isIdentifier(body, type)) {
                responder.respond(response, 400, null, "Invalid body. Must contain " + type +
                    " identifier (string).", null);
                return;
            }
            let readerIdentifier = identifier;
            let bookIdentifier = new ObjectId(body.book_id);
            if (type === "reader") {
                readerIdentifier = new ObjectId(body.reader_id);
                bookIdentifier = identifier;
            }
            database.exists(readerCollection, readerIdentifier).then(value => {
                if (!value) {
                    responder.respond(response, 404, null, "Reader not found", null);
                    return;
                }
                database.exists(bookCollection, bookIdentifier).then(value => {
                    if (!value) {
                        responder.respond(response, 404, null, "Book not found", null);
                        return;
                    }
                    database.existsPair(registerCollection, readerIdentifier, bookIdentifier).then(value => {
                        if (value) {
                            responder.respond(response, 409, null, type[0].toUpperCase() +
                                type.slice(1) + " already exists", null);
                            return;
                        }
                        body = {
                            reader_id: readerIdentifier,
                            book_id: bookIdentifier
                        };
                        responder.post(registerCollection, body, response, "register");
                    });
                });
            });
        });
}

function deletePair(registerCollection, identifier, request, response, type) {
    let body = [];
    request.on("data", (chunk) => body.push(chunk))
        .on("end", function () {
            body = Buffer.concat(body).toString();
            try {
                body = JSON.parse(body);
            } catch (error) {
                responder.respond(response, 400, null, "Invalid body. Must contain " + type +
                    " identifier (string).", null);
                return;
            }
            if (!isIdentifier(body, type)) {
                responder.respond(response, 400, null, "Invalid body. Must contain " + type +
                    " identifier (string).", null);
                return;
            }
            let readerIdentifier = identifier;
            let bookIdentifier = new ObjectId(body.book_id);
            if (type === "reader") {
                readerIdentifier = new ObjectId(body.reader_id);
                bookIdentifier = identifier;
            }
            database.existsPair(registerCollection, readerIdentifier, bookIdentifier).then(value => {
                if (!value) {
                    responder.respond(response, 404, null, type[0].toUpperCase() +
                        type.slice(1) + " not found", null);
                    return;
                }
                database.deleteOnePair(registerCollection, readerIdentifier, bookIdentifier).then(number => {
                    if (number === null) {
                        responder.respond(response, 500, null, "Database error", null);
                    } else if (number === 0) {
                        responder.respond(response, 204, null, null, null);
                    } else {
                        responder.respond(response, 200, null, null, null);
                    }
                });
            });
        });
}

module.exports = {
    get,
    post,
    deletePair
};