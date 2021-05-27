const {Datastore} = require('@google-cloud/datastore');
const {getEmailByUuid} = require('../models/user');
const {isEmpty} = require('lodash');
const {logSuccessResponse, logFailedResponse} = require('../utils/logger');
const {StatusCodes} = require('http-status-codes');
const {verifyToken} = require('../utils/jwt');

const datastore = new Datastore();
const kind = 'Classrooms';

const getClassrooms = async function(req, res) {
    const payload = verifyToken(req, res);
    if (payload === null) {
        return;
    }

    const role = req.query.role;
    const uuid = payload.uuid;

    if (role === 'professor') {
        const getClassroomsByProfessor = await datastore.createQuery(kind)
            .filter('professor', '=', uuid);
        const [classrooms] = await datastore.runQuery(getClassroomsByProfessor);
        for (const classroom of classrooms) {
            classroom.professor = {email: await getEmailByUuid(classroom.professor)};
            let students = [];
            for (let student of classroom.students) {
                students.push({email: await getEmailByUuid(student)});
            }
            classroom.students = students;
        }
        const response = {
            status: 'success',
            classrooms: classrooms
        };
        res.status(StatusCodes.OK);
        res.end(JSON.stringify(response));
        logSuccessResponse(req, response);
    } else {
        const getClassrooms = await datastore.createQuery(kind);
        const [classrooms] = await datastore.runQuery(getClassrooms);
        let classroomsArray = [];
        for (const classroom of classrooms) {
            if (classroom.students.includes(uuid)) {
                classroom.professor = {email: await getEmailByUuid(classroom.professor)};
                let students = [];
                for (let student of classroom.students) {
                    students.push({email: await getEmailByUuid(student)});
                }
                classroom.students = students;
                classroomsArray.push(classroom);
            }
        }
        const response = {
            status: 'success',
            classrooms: classroomsArray
        };
        res.status(StatusCodes.OK);
        res.end(JSON.stringify(response));
        logSuccessResponse(req, response);
    }
}

const create = async function (req, res) {
    const payload = verifyToken(req, res);
    if (payload === null) {
        return;
    }

    const name = req.body.name;
    const subject = req.body.subject;
    const professor = payload.uuid;

    const getClassroomsByName = await datastore.createQuery(kind).filter('name', '=', name)
        .filter('subject', '=', subject).filter('professor', '=', professor);
    const [classrooms] = await datastore.runQuery(getClassroomsByName);

    if (isEmpty(classrooms)) {
        const classroom = {
            name: name,
            subject: subject,
            professor: professor,
            students: [],
            schedules: []
        }
        const entity = {
            key: datastore.key(kind),
            data: classroom
        };
        const id = (await datastore.save(entity))[0].mutationResults[0].key.path[0].id;

        const response = {
            status: 'success',
            message: 'Classroom created successfully.'
        };
        res.status(StatusCodes.CREATED);
        res.location("/classrooms/" + id);
        res.end(JSON.stringify(response));
        logSuccessResponse(req, response);
    } else {
        const response = {
            status: 'failed',
            message: 'Classroom with specified name, subject and professor already exists.'
        };
        res.status(StatusCodes.CONFLICT);
        res.end(JSON.stringify(response));
        logFailedResponse(req, response);
    }
};

const getClassroom = async function (req, res) {
    const payload = verifyToken(req, res);
    if (payload === null) {
        return;
    }

    const id = parseInt(req.params.classroomId);
    if (isNaN(id)) {
        const response = {
            status: 'failed',
            message: 'Classroom not found.'
        };
        res.status(StatusCodes.NOT_FOUND);
        res.end(JSON.stringify(response));
        logFailedResponse(req, response);
    } else {
        const key = datastore.key([kind, id]);
        const [classroom] = await datastore.get(key);
        if (isEmpty(classroom)) {
            const response = {
                status: 'failed',
                message: 'Classroom not found.'
            };
            res.status(StatusCodes.NOT_FOUND);
            res.end(JSON.stringify(response));
            logFailedResponse(req, response);
        } else {
            classroom.professor = {email: await getEmailByUuid(classroom.professor)};
            let students = [];
            for (let student of classroom.students) {
                students.push({email: await getEmailByUuid(student)});
            }
            classroom.students = students;
            const response = {
                status: 'success',
                classroom: classroom
            };
            res.status(StatusCodes.OK);
            res.end(JSON.stringify(response));
            logSuccessResponse(req, response);
        }
    }
};

const deleteClassroom = async function (req, res) {
    const payload = verifyToken(req, res);
    if (payload === null) {
        return;
    }

    const id = parseInt(req.params.classroomId);
    if (isNaN(id)) {
        const response = {
            status: 'failed',
            message: 'Classroom not found.'
        };
        res.status(StatusCodes.NOT_FOUND);
        res.end(JSON.stringify(response));
        logFailedResponse(req, response);
    } else {
        const key = datastore.key([kind, id]);
        const [classroom] = await datastore.get(key);
        if (isEmpty(classroom)) {
            const response = {
                status: 'failed',
                message: 'Classroom not found.'
            };
            res.status(StatusCodes.NOT_FOUND);
            res.end(JSON.stringify(response));
            logFailedResponse(req, response);
        } else {
            if (classroom.professor !== payload.uuid) {
                const response = {
                    status: 'failed',
                    message: 'User not professor of the specified classroom.'
                };
                res.status(StatusCodes.FORBIDDEN);
                res.end(JSON.stringify(response));
                logFailedResponse(req, response);
            } else {
                const key = datastore.key([kind, id]);
                await datastore.delete(key);
                const response = {
                    status: 'success',
                    message: 'Classroom deleted successfully.'
                };
                res.status(StatusCodes.OK);
                res.end(JSON.stringify(response));
                logSuccessResponse(req, response);
            }
        }
    }
};

const getProfessor = async function (req, res) {
    const payload = verifyToken(req, res);
    if (payload === null) {
        return;
    }

    const id = parseInt(req.params.classroomId);
    if (isNaN(id)) {
        const response = {
            status: 'failed',
            message: 'Classroom not found.'
        };
        res.status(StatusCodes.NOT_FOUND);
        res.end(JSON.stringify(response));
        logFailedResponse(req, response);
    } else {
        const key = datastore.key([kind, id]);
        const [classroom] = await datastore.get(key);
        if (isEmpty(classroom)) {
            const response = {
                status: 'failed',
                message: 'Classroom not found.'
            };
            res.status(StatusCodes.NOT_FOUND);
            res.end(JSON.stringify(response));
            logFailedResponse(req, response);
        } else {
            const response = {
                status: 'success',
                professor: {
                    email: await getEmailByUuid(classroom.professor)
                }
            };
            res.status(StatusCodes.OK);
            res.end(JSON.stringify(response));
            logSuccessResponse(req, response);
        }
    }
};

const addStudent = async function (req, res) {
    const payload = verifyToken(req, res);
    if (payload === null) {
        return;
    }

    const uuid = payload.uuid;
    const id = parseInt(req.params.classroomId);
    if (isNaN(id)) {
        const response = {
            status: 'failed',
            message: 'Classroom not found.'
        };
        res.status(StatusCodes.NOT_FOUND);
        res.end(JSON.stringify(response));
        logFailedResponse(req, response);
    } else {
        const transaction = datastore.transaction();
        const key = datastore.key([kind, id]);
        try {
            await transaction.run();
            const [classroom] = await transaction.get(key);
            if (isEmpty(classroom)) {
                const response = {
                    status: 'failed',
                    message: 'Classroom not found.'
                };
                res.status(StatusCodes.NOT_FOUND);
                res.end(JSON.stringify(response));
                logFailedResponse(req, response);
            } else {
                if (classroom.professor === uuid) {
                    await transaction.rollback();
                    const response = {
                        status: 'failed',
                        message: 'User already professor of the specified classroom.'
                    };
                    res.status(StatusCodes.CONFLICT);
                    
                    res.end(JSON.stringify(response));
                    logFailedResponse(req, response);
                } else {
                    classroom.students.push(uuid);
                    const entity = {
                        key: key,
                        data: classroom
                    };
                    transaction.save(entity);
                    await transaction.commit();

                    const response = {
                        status: 'success',
                        message: 'Student added successfully.'
                    };
                    res.status(StatusCodes.OK);
                    res.end(JSON.stringify(response));
                    logSuccessResponse(req, response);
                }
            }
        } catch (err) {
            await transaction.rollback();

            const response = {
                status: 'failed',
                message: 'Database error.'
            };
            res.status(StatusCodes.INTERNAL_SERVER_ERROR);
            res.end(JSON.stringify(response));
            logFailedResponse(req, response);

            throw err;
        }
    }
};

const getStudents = async function (req, res) {
    const payload = verifyToken(req, res);
    if (payload === null) {
        return;
    }

    const id = parseInt(req.params.classroomId);
    if (isNaN(id)) {
        const response = {
            status: 'failed',
            message: 'Classroom not found.'
        };
        res.status(StatusCodes.NOT_FOUND);
        res.end(JSON.stringify(response));
        logFailedResponse(req, response);
    } else {
        const key = datastore.key([kind, id]);
        const [classroom] = await datastore.get(key);
        if (isEmpty(classroom)) {
            const response = {
                status: 'failed',
                message: 'Classroom not found.'
            };
            res.status(StatusCodes.NOT_FOUND);
            res.end(JSON.stringify(response));
            logFailedResponse(req, response);
        } else {
            const students = classroom.students;

            const response = {
                status: 'success',
                students: []
            };

            for (let student of students) {
                response.students.push({email: await getEmailByUuid(student)});
            }

            res.status(StatusCodes.OK);
            res.end(JSON.stringify(response));
            logSuccessResponse(req, response);
        }
    }
};

const getStudent = async function (req, res) {
    const payload = verifyToken(req, res);
    if (payload === null) {
        return;
    }

    const id = parseInt(req.params.classroomId);
    if (isNaN(id)) {
        const response = {
            status: 'failed',
            message: 'Classroom not found.'
        };
        res.status(StatusCodes.NOT_FOUND);
        res.end(JSON.stringify(response));
        logFailedResponse(req, response);
    } else {
        const key = datastore.key([kind, id]);
        const [classroom] = await datastore.get(key);
        if (isEmpty(classroom)) {
            const response = {
                status: 'failed',
                message: 'Classroom not found.'
            };
            res.status(StatusCodes.NOT_FOUND);
            res.end(JSON.stringify(response));
            logFailedResponse(req, response);
        } else {
            const id = req.params.studentId;
            if (!classroom.students.includes(id)) {
                const response = {
                    status: 'failed',
                    message: 'Student not found.'
                };
                res.status(StatusCodes.NOT_FOUND);
                
                res.end(JSON.stringify(response));
                logFailedResponse(req, response);
            } else {
                const response = {
                    status: 'success',
                    student: {
                        email: await getEmailByUuid(id)
                    }
                };
                res.status(StatusCodes.OK);
                res.end(JSON.stringify(response));
                logSuccessResponse(req, response);
            }
        }
    }
};

const removeStudent = async function (req, res) {
    const payload = verifyToken(req, res);
    if (payload === null) {
        return;
    }

    const id = parseInt(req.params.classroomId);
    if (isNaN(id)) {
        const response = {
            status: 'failed',
            message: 'Classroom not found.'
        };
        res.status(StatusCodes.NOT_FOUND);
        res.end(JSON.stringify(response));
        logFailedResponse(req, response);
    } else {
        const transaction = datastore.transaction();
        const key = datastore.key([kind, id]);
        try {
            await transaction.run();
            const [classroom] = await transaction.get(key);
            if (isEmpty(classroom)) {
                const response = {
                    status: 'failed',
                    message: 'Classroom not found.'
                };
                res.status(StatusCodes.NOT_FOUND);
                res.end(JSON.stringify(response));
                logFailedResponse(req, response);
            } else {
                const uuid = payload.uuid;
                let students = classroom.students;
                for (let index = 0; index < students.length; index++) {
                    if (students[index] === uuid) {
                        students.splice(index, 1);
                        classroom.students = students;
                        const entity = {
                            key: key,
                            data: classroom
                        };
                        transaction.save(entity);
                        await transaction.commit();

                        const response = {
                            status: 'success',
                            message: 'Student removed successfully.'
                        };
                        res.status(StatusCodes.OK);
                        res.end(JSON.stringify(response));
                        logSuccessResponse(req, response);
                        return;
                    }
                }
                await transaction.rollback();
                const response = {
                    status: 'failed',
                    message: 'User not the specified student of the specified classroom.'
                };
                res.status(StatusCodes.FORBIDDEN);
                res.end(JSON.stringify(response));
                logFailedResponse(req, response);
            }
        } catch (err) {
            await transaction.rollback();

            const response = {
                status: 'failed',
                message: 'Database error.'
            };
            res.status(StatusCodes.INTERNAL_SERVER_ERROR);
            res.end(JSON.stringify(response));
            logFailedResponse(req, response);

            throw err;
        }
    }
};

module.exports = {
    getClassrooms,
    create,
    getClassroom,
    deleteClassroom,

    getProfessor,

    addStudent,
    getStudents,
    getStudent,
    removeStudent
};