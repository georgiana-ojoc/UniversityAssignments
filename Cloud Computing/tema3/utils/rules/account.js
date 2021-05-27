const {body} = require('express-validator');

const register = function () {
    return [
        body('email', 'Email (string) is required.').exists().notEmpty().isString(),
        body('email', 'Email type not supported.').isEmail()
            .normalizeEmail({gmail_remove_dots: false}),
        body('password', 'Password (string) is required.').exists().notEmpty().isString()
    ];
}

const login = function () {
    return [
        body('email', 'Email (string) is required.').exists().notEmpty().isString(),
        body('email', 'Email type not supported.').isEmail()
            .normalizeEmail({gmail_remove_dots: false}),
        body('password', 'Password (string) is required.').exists().notEmpty().isString()
    ];
}

module.exports = {
    register,
    login
};