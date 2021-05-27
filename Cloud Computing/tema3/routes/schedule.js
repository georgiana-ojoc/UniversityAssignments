const router = require('express').Router();
const {scheduleController} = require('../controllers');

const {logRequest} = require('../utils/logger');
const {checkQuery, checkJSONContentTypeHeader, checkBody} = require('../utils/checks');
const {add, remove} = require('../utils/rules/schedule');

const addMiddlewares = [
    checkJSONContentTypeHeader,
    checkBody,
    logRequest
];

const removeMiddlewares = [
    checkQuery,
    remove,
    logRequest
];

router.post('/:classroomId/schedules', addMiddlewares, add(), scheduleController.add);
router.get('/:classroomId/schedules', scheduleController.get);
router.delete('/:classroomId/schedules', removeMiddlewares, scheduleController.deleteSchedule);

module.exports = router;