const {Datastore} = require('@google-cloud/datastore');
const {logSuccessResponse, logFailedResponse} = require('../utils/logger');
const {StatusCodes} = require('http-status-codes');

const datastore = new Datastore();
const kind = 'OAuths';

const oAuthCallback = async function (req, res) {
    const uuid = req.query.state;
    const code = req.query.code;
    if (uuid === undefined || uuid === null || uuid === "" || code === undefined || code === null || code === "") {
        const response = {
            status: 'failed',
            message: 'State (uuid) and code (string) are required.'
        };
        res.status(StatusCodes.BAD_REQUEST);
        res.location("/oauth");
        res.end(JSON.stringify(response));
        logFailedResponse(req, response);
    } else {
        const {tokens} = await global.oAuth2Client.getToken(code);
        if (tokens.refresh_token === undefined) {
            const response = {
                status: 'failed',
                message: 'Authorize the scope.'
            };
            res.status(StatusCodes.BAD_REQUEST);
            res.location("/oauth");
            res.end(JSON.stringify(response));
            logFailedResponse(req, response);
        } else {
            const token = {
                user: uuid,
                tokens
            };
            const entity = {
                key: datastore.key(kind),
                data: token
            };
            await datastore.save(entity);

            const response = {
                status: 'success',
                message: 'Tokens added successfully.'
            };
            res.status(StatusCodes.CREATED);
            res.end(JSON.stringify(response));
            logSuccessResponse(req, response);
        }
    }
}

module.exports = {
    oAuthCallback
};