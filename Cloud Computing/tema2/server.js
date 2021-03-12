const books = require("./books");
const database = require("./database");
const http = require("http");
const log = require("./log");
const {ObjectId} = require("mongodb");
const readers = require("./readers");
const register = require("./register");
const responder = require("./responder");

const scheme = "http://";
const host = "localhost";
const port = 3000;

const address = scheme + host + ':' + port;

function listener(readerCollection, bookCollection, registerCollection, request, response) {
    request.on("error", (error) => {
        log.error(error.message);
    });

    response.on("error", (error) => {
        log.error(error.message);
    });

    log.information(`${request.method} ${address}${request.url}`);
    const url = new URL(address + request.url);

    if (url.pathname === "/readers") {
        switch (request.method) {
            case "GET":
                readers.getAll(readerCollection, response, url.searchParams);
                break;
            case "POST":
                readers.post(readerCollection, null, request, response);
                break;
            default:
                responder.respond(response, 405, null, null, null);
                break;
        }
    } else if (url.pathname === "/books") {
        switch (request.method) {
            case "GET":
                books.getAll(bookCollection, response, url.searchParams);
                break;
            case "POST":
                books.post(bookCollection, null, request, response);
                break;
            default:
                responder.respond(response, 405, null, null, null);
                break;
        }
    } else {
        const path = url.pathname.slice(1).split('/');
        if (path.length === 2) {
            if (path[0] === "readers") {
                let identifier = path[1];
                if (!database.isValid(identifier)) {
                    responder.respond(response, 400, null, "Invalid identifier", null);
                    return;
                }
                identifier = new ObjectId(identifier);
                switch (request.method) {
                    case "GET":
                        readers.getOne(readerCollection, identifier, response);
                        break;
                    case "POST":
                        readers.post(readerCollection, identifier, request, response);
                        break;
                    case "PUT":
                        readers.put(readerCollection, identifier, request, response);
                        break;
                    case "PATCH":
                        readers.patch(readerCollection, identifier, request, response);
                        break;
                    case "DELETE":
                        readers.deleteReader(readerCollection, identifier, response);
                        break;
                    default:
                        responder.respond(response, 405, null, null, null);
                        break;
                }
            } else if (path[0] === "books") {
                let identifier = path[1];
                if (!database.isValid(identifier)) {
                    responder.respond(response, 400, null, "Invalid identifier", null);
                    return;
                }
                identifier = new ObjectId(identifier);
                switch (request.method) {
                    case "GET":
                        books.getOne(bookCollection, identifier, response);
                        break;
                    case "POST":
                        books.post(bookCollection, identifier, request, response);
                        break;
                    case "PUT":
                        books.put(bookCollection, identifier, request, response);
                        break;
                    case "PATCH":
                        books.patch(bookCollection, identifier, request, response);
                        break;
                    case "DELETE":
                        books.deleteBook(bookCollection, identifier, response);
                        break;
                    default:
                        responder.respond(response, 405, null, null, null);
                        break;
                }
            } else {
                responder.respond(response, 404, null, null, null);
            }
        } else if (path.length === 3) {
            if (path[0] === "readers") {
                let identifier = path[1];
                if (!database.isValid(identifier)) {
                    responder.respond(response, 400, null, "Invalid identifier", null);
                    return;
                }
                identifier = new ObjectId(identifier);
                if (path[2] === "books") {
                    switch (request.method) {
                        case "GET":
                            register.get(readerCollection, bookCollection, registerCollection, identifier, request,
                                response, "book");
                            break;
                        case "POST":
                            register.post(readerCollection, bookCollection, registerCollection, identifier, request,
                                response, "book");
                            break;
                        case "DELETE":
                            register.deletePair(registerCollection, identifier, request, response, "book");
                            break;
                        default:
                            responder.respond(response, 405, null, null, null);
                            break;
                    }
                } else {
                    responder.respond(response, 404, null, null, null);
                }
            } else if (path[0] === "books") {
                let identifier = path[1];
                if (!database.isValid(identifier)) {
                    responder.respond(response, 400, null, "Invalid identifier", null);
                    return;
                }
                identifier = new ObjectId(identifier);
                if (path[2] === "readers") {
                    switch (request.method) {
                        case "GET":
                            register.get(readerCollection, bookCollection, registerCollection, identifier, request,
                                response, "reader");
                            break;
                        case "POST":
                            register.post(readerCollection, bookCollection, registerCollection, identifier, request,
                                response, "reader");
                            break;
                        case "DELETE":
                            register.deletePair(registerCollection, identifier, request, response, "reader");
                            break;
                        default:
                            responder.respond(response, 405, null, null, null);
                            break;
                    }
                } else {
                    responder.respond(response, 404, null, null, null);
                }
            } else {
                responder.respond(response, 404, null, null, null);
            }
        } else {
            responder.respond(response, 404, null, null, null);
        }
    }
}

database.getDatabase().then(noSqlDatabase => {
    const readerCollection = database.getCollection(noSqlDatabase, database.READERS);
    const bookCollection = database.getCollection(noSqlDatabase, database.BOOKS);
    const registerCollection = database.getCollection(noSqlDatabase, database.REGISTER);
    const server = http.createServer((request, response) =>
        listener(readerCollection, bookCollection, registerCollection, request, response));
    server.listen(port, host, () => {
        log.information(`HTTP server running at ${address}`);
    });
});