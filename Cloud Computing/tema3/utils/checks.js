const {isEmpty} = require('lodash');
const {StatusCodes} = require('http-status-codes');
const {validationResult} = require('express-validator');

const checkAuthorizationHeader = function (req, res, next) {
    if (req.header.authorization === undefined) {
        const response = {
            status: 'failed',
            message: 'Authorization not set.'
        };
        res.status(StatusCodes.UNAUTHORIZED);
        res.end(JSON.stringify(response));
    } else {
        next();
    }
}

const checkQuery = function (req, res, next) {
    console.log(req.query);
    if (isEmpty(req.query)) {
        const response = {
            status: 'failed',
            message: 'Query can not be empty.'
        };
        res.status(StatusCodes.UNPROCESSABLE_ENTITY);
        res.end(JSON.stringify(response));
    } else {
        const errors = validationResult(req);
        if (!errors.isEmpty()) {
            const errorsArray = errors.array();
            const response = {
                status: 'failed',
                message: errorsArray[0]['msg']
            };
            res.status(StatusCodes.BAD_REQUEST);
            
            res.end(JSON.stringify(response));
        } else {
            next();
        }
    }
}

const checkJSONContentTypeHeader = function (req, res, next) {
    if (!req.is('application/json')) {
        const response = {
            status: 'failed',
            message: 'Content-Type not set to application/json.'
        };
        res.status(StatusCodes.UNSUPPORTED_MEDIA_TYPE);
        res.end(JSON.stringify(response));
    } else {
        next();
    }
}

const checkBody = function (req, res, next) {
    console.log(req.body);
    if (isEmpty(req.body)) {
        const response = {
            status: 'failed',
            message: 'Body can not be empty.'
        };
        res.status(StatusCodes.UNPROCESSABLE_ENTITY);
        res.end(JSON.stringify(response));
    } else {
        const errors = validationResult(req);
        if (!errors.isEmpty()) {
            const errorsArray = errors.array();
            const response = {
                status: 'failed',
                message: errorsArray[0]['msg']
            };
            res.status(StatusCodes.BAD_REQUEST);
            res.end(JSON.stringify(response));
        } else {
            next();
        }
    }
}

module.exports = {
    checkAuthorizationHeader,
    checkQuery,
    checkJSONContentTypeHeader,
    checkBody
};