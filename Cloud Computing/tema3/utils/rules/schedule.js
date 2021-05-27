const {body} = require('express-validator');
const {StatusCodes} = require('http-status-codes');

const add = function () {
    return [
        body('day', 'Day (Monday, Tuesday, Wednesday, Thursday or Friday) is required.').exists()
            .notEmpty().isString()
            .custom(value => ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday'].includes(value)),
        body('start', 'Start time (hh:mm) is required.').exists().notEmpty().isString()
            .custom(value => /^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$/gm.test(value)),
        body('end', 'End time (hh:mm) is required.').exists().notEmpty().isString()
            .custom(value => /^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$/gm.test(value))
    ];
}

const remove = function (req, res, next) {
    const day = req.query.day;
    if (!['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday'].includes(day)) {
        const response = {
            status: 'failed',
            message: 'Day (Monday, Tuesday, Wednesday, Thursday or Friday) is required.'
        };
        res.status(StatusCodes.BAD_REQUEST);
        res.end(JSON.stringify(response));
        return;
    }
    const start = req.query.start;
    if (!/^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$/gm.test(start)) {
        const response = {
            status: 'failed',
            message: 'Start time (hh:mm) is required.'
        };
        res.status(StatusCodes.BAD_REQUEST);
        res.end(JSON.stringify(response));
        return;
    }
    const end = req.query.end;
    if (!/^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$/gm.test(end)) {
        const response = {
            status: 'failed',
            message: 'End time (hh:mm) is required.'
        };
        res.status(StatusCodes.BAD_REQUEST);
        res.end(JSON.stringify(response));
        return;
    }
    if (start >= end) {
        const response = {
            status: 'failed',
            message: 'End time should be after start time.'
        };
        res.status(StatusCodes.BAD_REQUEST);
        res.end(JSON.stringify(response));
        return;
    }
    next();
}

module.exports = {
    add,
    remove
};