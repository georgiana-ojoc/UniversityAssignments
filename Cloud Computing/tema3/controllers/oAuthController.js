const config = require('config');
const {logSuccessResponse, logFailedResponse} = require('../utils/logger');
const {StatusCodes} = require('http-status-codes');
const {verifyToken} = require('../utils/jwt');

const {redirect_uri} = config.get('oauth');

const oAuth = async function (req, res) {
    const payload = verifyToken(req, res);
    if (payload === null) {
        return;
    }

    let scope = req.query.scope;
    if (scope === undefined || scope === null || scope === '') {
        const response = {
            status: 'failed',
            message: 'Scope (URL) is required.'
        };
        res.status(StatusCodes.NOT_FOUND);
        res.end(JSON.stringify(response));
        logFailedResponse(req, response);
    } else {
        const response = {
            status: 'success',
            message: 'Redirection URL generated successfully.'
        };
        res.status(StatusCodes.OK);
        res.location(global.oAuth2Client.generateAuthUrl({
            prompt: 'consent',
            access_type: 'offline',
            redirect_uri: redirect_uri,
            scope: scope,
            state: payload.uuid
        }));
        res.end(JSON.stringify(response));
        logSuccessResponse(req, response);
    }
}

module.exports = {
    oAuth
};