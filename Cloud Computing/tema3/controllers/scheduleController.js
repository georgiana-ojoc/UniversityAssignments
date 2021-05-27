const {Datastore} = require('@google-cloud/datastore');
const {find, isEmpty} = require('lodash');
const {logSuccessResponse, logFailedResponse} = require('../utils/logger');
const {StatusCodes} = require('http-status-codes');
const {verifyToken} = require('../utils/jwt');

const datastore = new Datastore();
const kind = 'Classrooms';

const add = async function (req, res) {
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
                if (classroom.professor !== uuid) {
                    await transaction.rollback();
                    const response = {
                        status: 'failed',
                        message: 'User not professor of the specified classroom.'
                    };
                    res.status(StatusCodes.FORBIDDEN);
                    res.end(JSON.stringify(response));
                    logFailedResponse(req, response);
                } else {
                    const day = req.body.day;
                    const start = req.body.start;
                    const end = req.body.end;

                    if (start >= end) {
                        const response = {
                            status: 'failed',
                            message: 'End time should be after start time.'
                        };
                        res.status(StatusCodes.BAD_REQUEST);
                        res.end(JSON.stringify(response));
                        logFailedResponse(req, response);
                    } else {
                        const schedule = {
                            day: day,
                            start: start,
                            end: end
                        };
                        if (find(classroom.schedules, schedule) !== undefined) {
                            const response = {
                                status: 'success',
                                message: 'Schedule already exists.'
                            };
                            res.status(StatusCodes.CONFLICT);
                            res.end(JSON.stringify(response));
                            logSuccessResponse(req, response);
                        } else {
                            classroom.schedules.push(schedule);
                            const entity = {
                                key: key,
                                data: classroom
                            };
                            transaction.save(entity);
                            await transaction.commit();

                            const response = {
                                status: 'success',
                                message: 'Schedule added successfully.'
                            };
                            res.status(StatusCodes.OK);
                            res.end(JSON.stringify(response));
                            logSuccessResponse(req, response);
                        }
                    }
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

const get = async function (req, res) {
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
                schedules: classroom.schedules
            };
            res.status(StatusCodes.OK);
            res.end(JSON.stringify(response));
            logSuccessResponse(req, response);
        }
    }
}

const deleteSchedule = async function (req, res) {
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
                if (classroom.professor === uuid) {
                    await transaction.rollback();
                    const response = {
                        status: 'failed',
                        message: 'User not professor of the specified classroom.'
                    };
                    res.status(StatusCodes.FORBIDDEN);
                    res.end(JSON.stringify(response));
                    logFailedResponse(req, response);
                } else {
                    const day = req.query.day;
                    const start = req.query.start;
                    const end = req.query.end;

                    if (start >= end) {
                        const response = {
                            status: 'failed',
                            message: 'End time should be after start time.'
                        };
                        res.status(StatusCodes.BAD_REQUEST);
                        res.end(JSON.stringify(response));
                        logFailedResponse(req, response);
                    } else {
                        let schedules = classroom.schedules;
                        for (let index = 0; index < schedules.length; index++) {
                            if (schedules[index].day === day && schedules[index].start === start &&
                                schedules[index].end === end) {
                                schedules.splice(index, 1);
                                classroom.schedules = schedules;
                                break;
                            }
                        }
                        const entity = {
                            key: key,
                            data: classroom
                        };
                        transaction.save(entity);
                        await transaction.commit();

                        const response = {
                            status: 'success',
                            message: 'Schedule removed successfully.'
                        };
                        res.status(StatusCodes.OK);
                        res.end(JSON.stringify(response));
                        logSuccessResponse(req, response);
                    }
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

module.exports = {
    add,
    get,
    deleteSchedule
};