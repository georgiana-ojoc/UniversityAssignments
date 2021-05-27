const router = require('express').Router();
const {accountController} = require('../controllers');

const {checkJSONContentTypeHeader, checkBody} = require('../utils/checks');
const {logRequest} = require('../utils/logger');
const {register, login} = require('../utils/rules/account');

const registerMiddlewares = [
    checkJSONContentTypeHeader,
    checkBody,
    logRequest
];

const loginMiddlewares = [
    checkJSONContentTypeHeader,
    checkBody,
    logRequest
];

router.post('/register', registerMiddlewares, register(), accountController.register);
router.post('/login', loginMiddlewares, login(), accountController.login);

module.exports = router;