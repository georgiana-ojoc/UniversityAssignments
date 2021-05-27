const jwt = require('jsonwebtoken');
const {StatusCodes} = require('http-status-codes');

require('dotenv').config();

const signToken = function (payload) {
    return jwt.sign(payload, process.env.JWT_SECRET, {
        algorithm: 'HS256',
        expiresIn: 86400
    });
}

const verifyToken = function (req, res) {
    try {
        const token = req.headers.authorization.split(' ')[1];
        return jwt.verify(token, process.env.JWT_SECRET);
    } catch (err) {
        const response = {
            status: 'failed',
            message: 'Authorization not valid.'
        };
        res.status(StatusCodes.UNAUTHORIZED);
        res.end(JSON.stringify(response));
        return null;
    }
}

module.exports = {
    signToken,
    verifyToken
};