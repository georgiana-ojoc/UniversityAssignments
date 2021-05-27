const router = require('express').Router();
const {calendarController} = require('../controllers');

const {checkJSONContentTypeHeader, checkBody} = require('../utils/checks');
const {create} = require('../utils/rules/calendar');
const {logRequest} = require('../utils/logger');

const addMiddlewares = [
    checkJSONContentTypeHeader,
    checkBody,
    logRequest
];

router.post('', addMiddlewares, create(), calendarController.createEvent);
router.get('', calendarController.getEvents);

router.get('/:eventId', calendarController.getEvent);
router.delete('/:eventId', calendarController.deleteEvent);

module.exports = router;