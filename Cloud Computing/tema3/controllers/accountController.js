const {Datastore} = require('@google-cloud/datastore');
const bcrypt = require('bcrypt');
const {StatusCodes} = require('http-status-codes');
const {isEmpty} = require('lodash');
const {signToken} = require('../utils/jwt');
const {logSuccessResponse, logFailedResponse, logNewAccount} = require('../utils/logger');
const {v4: uuidv4} = require('uuid');

const datastore = new Datastore();
const kind = 'Users';
const saltRounds = 8;

const register = async function (req, res) {
    const email = req.body.email;
    const password = req.body.password;

    const getUserByEmail = await datastore.createQuery('Users').filter('email', '=', email);
    const [users] = await datastore.runQuery(getUserByEmail);

    if (isEmpty(users)) {
        bcrypt.hash(password, saltRounds, async (err, hashed) => {
            if (err) {
                const response = {
                    status: 'failed',
                    message: 'Encryption error.'
                };
                res.status(StatusCodes.INTERNAL_SERVER_ERROR);

                res.end(JSON.stringify(response));
                logFailedResponse(req, response);

                throw err;
            } else {
                const user = {
                    uuid: uuidv4(),
                    email: email,
                    password: hashed
                }
                const entity = {
                    key: datastore.key(kind),
                    data: user
                };
                await datastore.save(entity);

                const response = {
                    status: 'success',
                    message: 'User created successfully.'
                };
                res.status(StatusCodes.CREATED);
                res.end(JSON.stringify(response));
                logSuccessResponse(req, response);
                logNewAccount(email);
            }
        });
    } else {
        const response = {
            status: 'failed',
            message: 'User already exists.'
        };
        res.status(StatusCodes.CONFLICT);
        res.end(JSON.stringify(response));
        logFailedResponse(req, response);
    }
};

const login = async function (req, res) {
    const email = req.body.email;
    const password = req.body.password;

    const getUsersByEmail = await datastore.createQuery(kind).filter('email', '=', email);
    const [users] = await datastore.runQuery(getUsersByEmail);

    if (!isEmpty(users)) {
        const storedPassword = users[0]['password'];
        const uuid = users[0]['uuid'];
        bcrypt.compare(password, storedPassword, function (err, result) {
            if (err) {
                const response = {
                    status: 'failed',
                    message: 'Decryption error.'
                };
                res.status(StatusCodes.INTERNAL_SERVER_ERROR);
                res.end(JSON.stringify(response));
                logFailedResponse(req, response);

                throw err;
            } else {
                if (result) {
                    const accessToken = signToken({uuid: uuid});
                    const response = {
                        status: 'success',
                        message: 'User authenticated and authorized successfully.',
                        accessToken: accessToken
                    };
                    res.status(StatusCodes.OK);
                    res.end(JSON.stringify(response));
                    logSuccessResponse(req, response);
                } else {
                    const response = {
                        status: 'failed',
                        message: 'Password is incorrect.'
                    };
                    res.status(StatusCodes.FORBIDDEN);
                    res.end(JSON.stringify(response));
                    logFailedResponse(req, response);
                }
            }
        });
    } else {
        const response = {
            status: 'failed',
            message: 'Email is incorrect.'
        };
        res.status(StatusCodes.FORBIDDEN);
        res.end(JSON.stringify(response));
        logFailedResponse(req, response);
    }
};

module.exports = {
    register,
    login
};