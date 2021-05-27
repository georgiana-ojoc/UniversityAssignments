const calendar = require('../utils/calendar');
const config = require('config');
const {logSuccessResponse} = require('../utils/logger');
const {StatusCodes} = require('http-status-codes');
const {verifyToken} = require('../utils/jwt');
const {runScopedCallback} = require('../utils/oAuth');

const scope = config.get('calendar').scope;

const createEvent = async function (req, res) {
    const payload = verifyToken(req, res);
    if (payload === null) {
        return;
    }

    const title = req.body.title;
    const day = req.body.day;
    const start = req.body.start;
    const end = req.body.end;

    const event = await runScopedCallback(req, res, payload.uuid, scope, calendar.createEvent, global.oAuth2Client,
        title, day, start, end);
    if (event !== null) {
        const response = {
            status: 'success',
            event: event
        };
        res.status(StatusCodes.OK);
        res.end(JSON.stringify(response));
        logSuccessResponse(req, response);
    }
}

const getEvents = async function (req, res) {
    const payload = verifyToken(req, res);
    if (payload === null) {
        return;
    }

    const events = await runScopedCallback(req, res, payload.uuid, scope, calendar.getEvents, global.oAuth2Client);
    if (events !== null) {
        const response = {
            status: 'success',
            events: events
        };
        res.status(StatusCodes.OK);
        res.end(JSON.stringify(response));
        logSuccessResponse(req, response);
    }
}

const getEvent = async function (req, res) {
    const payload = verifyToken(req, res);
    if (payload === null) {
        return;
    }

    const id = req.params.eventId;
    const event = await runScopedCallback(req, res, payload.uuid, scope, calendar.getEvent, global.oAuth2Client, id);
    if (event !== null) {
        const response = {
            status: 'success',
            event: event
        };
        res.status(StatusCodes.OK);
        res.end(JSON.stringify(response));
        logSuccessResponse(req, response);
    }
}

const deleteEvent = async function (req, res) {
    const payload = verifyToken(req, res);
    if (payload === null) {
        return;
    }

    const id = req.params.eventId;
    const event = await runScopedCallback(req, res, payload.uuid, scope, calendar.deleteEvent, global.oAuth2Client,
        id);
    if (event !== null) {
        const response = {
            status: 'success',
            message: 'Event deleted successfully.'
        };
        res.status(StatusCodes.OK);
        res.end(JSON.stringify(response));
        logSuccessResponse(req, response);
    }
}

module.exports = {
    createEvent,
    getEvents,
    getEvent,
    deleteEvent
};