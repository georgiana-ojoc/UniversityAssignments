const {Datastore} = require('@google-cloud/datastore');
const {logFailedResponse} = require('../utils/logger');
const {StatusCodes} = require('http-status-codes');

const datastore = new Datastore();
const kind = 'OAuths';

const getTokens = async function (user, scope) {
    const getTokensByUser = await datastore.createQuery(kind).filter('user', '=', user);
    const [tokens] = await datastore.runQuery(getTokensByUser);
    for (let item of tokens) {
        const result = item.tokens;
        if (result.scope === scope) {
            return result;
        }
    }
    return null;
}

const runScopedCallback = async function (req, res, user, scope, callback, ...parameters) {
    const tokens = await getTokens(user, scope);
    if (tokens === null) {
        const response = {
            status: 'failed',
            message: 'Authorize scope.'
        };
        res.status(StatusCodes.UNAUTHORIZED);
        res.location("/oauth");
        res.end(JSON.stringify(response));
        logFailedResponse(req, response);
        return null;
    } else {
        global.oAuth2Client.setCredentials(tokens);
        return await callback(req, res, ...parameters);
    }
}

module.exports = {
    runScopedCallback
};