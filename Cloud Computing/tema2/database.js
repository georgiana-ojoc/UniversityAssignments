const log = require("./log");
const {ObjectId} = require("mongodb");

const READERS = "readers";
const BOOKS = "books";
const REGISTER = "register";

const readerSchema = {
    bsonType: "object",
    required: ["name", "age"],
    properties: {
        name: {
            bsonType: "string",
            description: "must be a string and is required"
        },
        age: {
            bsonType: "int",
            minimum: 0,
            maximum: 150,
            description: "must be an integer in [0, 150] and is required"
        }
    }
};

const bookSchema = {
    bsonType: "object",
    required: ["title", "author", "year"],
    properties: {
        title: {
            bsonType: "string",
            description: "must be a string and is required"
        },
        author: {
            bsonType: "string",
            description: "must be a string and is required"
        },
        year: {
            bsonType: "int",
            minimum: 0,
            maximum: 2021,
            description: "must be an integer in [0, 2021] and is required"
        }
    }
};

const registerSchema = {
    bsonType: "object",
    required: ["reader_id", "book_id"],
    properties: {
        reader_id: {
            bsonType: "objectId",
            description: "must be an object identifier and is required"
        },
        book_id: {
            bsonType: "objectId",
            description: "must be an object identifier and is required"
        }
    }
};

async function getDatabase() {
    const {MongoClient} = require("mongodb");
    const url = `mongodb+srv://${process.env.ATLAS_USER}:${process.env.ATLAS_PASSWORD}@books.dghrd.mongodb.net/books?` +
        "retryWrites=true&w=majority";
    const client = new MongoClient(url, {useNewUrlParser: true, useUnifiedTopology: true});
    try {
        await client.connect();
        log.information("Connected to database server");
        return client.db("books");
    } catch (error) {
        log.error(error.message);
        return null;
    }
}

async function createCollection(database, name, schema) {
    try {
        const collection = await database.createCollection(name, {
            validator: {
                $jsonSchema: schema
            }
        });
        log.information(`Created collection "${name}"`);
        return collection;
    } catch (error) {
        log.error(error.message);
        return null;
    }
}

function getCollection(database, name) {
    return database.collection(name);
}

function initialize() {
    getDatabase().then(database => {
        createCollection(database, READERS, readerSchema).then();
        createCollection(database, BOOKS, bookSchema).then();
        createCollection(database, REGISTER, registerSchema).then();
    });
}

function isValid(identifier) {
    return ObjectId.isValid(identifier);
}

async function exists(collection, identifier) {
    try {
        const query = {
            "_id": identifier
        };
        const number = await collection.countDocuments(query);
        return number > 0;
    } catch {
        log.error(error.message);
        return null;
    }
}

async function existsPair(collection, readerIdentifier, bookIdentifier) {
    try {
        const query = {
            "reader_id": readerIdentifier,
            "book_id": bookIdentifier
        };
        const number = await collection.countDocuments(query);
        return number > 0;
    } catch {
        log.error(error.message);
        return null;
    }
}

async function find(collection, query, options) {
    try {
        const cursor = collection.find(query, options);
        return await cursor.toArray();
    } catch (error) {
        log.error(error.message);
        return null;
    }
}

async function findOne(collection, identifier) {
    try {
        return await collection.findOne(identifier);
    } catch (error) {
        log.error(error.message);
        return null;
    }
}

async function insertOne(collection, document) {
    try {
        const result = await collection.insertOne(document);
        return result.insertedId;
    } catch (error) {
        log.error(error.message);
        return null;
    }
}

async function updateOne(collection, identifier, document) {
    try {
        const query = {
            "_id": identifier
        };
        const newDocument = {
            $set: document
        };
        const result = await collection.updateOne(query, newDocument);
        return result.modifiedCount;
    } catch (error) {
        log.error(error.message);
        return null;
    }
}

async function deleteOne(collection, identifier) {
    try {
        const query = {
            "_id": identifier
        };
        const result = await collection.deleteOne(query);
        return result.deletedCount;
    } catch (error) {
        log.error(error.message);
        return null;
    }
}

async function deleteOnePair(collection, readerIdentifier, bookIdentifier) {
    try {
        const query = {
            "reader_id": readerIdentifier,
            "book_id": bookIdentifier
        };
        const result = await collection.deleteOne(query);
        return result.deletedCount;
    } catch (error) {
        log.error(error.message);
        return null;
    }
}

module.exports = {
    getDatabase,
    getCollection,
    isValid,
    exists,
    existsPair,
    find,
    findOne,
    insertOne,
    updateOne,
    deleteOne,
    deleteOnePair,
    READERS,
    BOOKS,
    REGISTER
};