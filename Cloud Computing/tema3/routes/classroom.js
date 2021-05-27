const router = require('express').Router();
const {classroomController} = require('../controllers');

const {checkQuery, checkJSONContentTypeHeader, checkBody} = require('../utils/checks');
const {get, create} = require('../utils/rules/classroom');
const {logRequest} = require('../utils/logger');

const getMiddlewares = [
    checkQuery,
    get,
    logRequest
];

const createMiddlewares = [
    checkJSONContentTypeHeader,
    checkBody,
    logRequest
];

router.get('', getMiddlewares, classroomController.getClassrooms);
router.post('', createMiddlewares, create(), classroomController.create);
router.get('/:classroomId', classroomController.getClassroom);
router.delete('/:classroomId', classroomController.deleteClassroom);

router.get('/:classroomId/professor', classroomController.getProfessor);

router.post('/:classroomId/students', classroomController.addStudent);
router.get('/:classroomId/students', classroomController.getStudents);
router.get('/:classroomId/students/:studentId', classroomController.getStudent);
router.delete('/:classroomId/students/:studentId', classroomController.removeStudent);

module.exports = router;