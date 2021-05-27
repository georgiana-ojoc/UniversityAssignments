const config = require('config');
const {google} = require('googleapis');
const {logFailedResponse} = require('../utils/logger');
const {StatusCodes} = require('http-status-codes');

const description = config.get('calendar').description;
const weekDays = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday'];
const weekDayNumbers = {
    Monday: 1,
    Tuesday: 2,
    Wednesday: 3,
    Thursday: 4,
    Friday: 5,
};

function getNextWeek(today) {
    return new Date(today.getFullYear(), today.getMonth(), today.getDate() + 7);
}

function getDate(day, hours, minutes) {
    let date = new Date();
    let today = date.getDay();
    date.setDate(date.getDate() + day - today);
    date.setHours(hours);
    date.setMinutes(minutes);
    return date;
}

createEvent = async function (req, res, authorization, title, day, start, end) {
    const startDay = weekDayNumbers[day];
    let startHours, startMinutes;
    [startHours, startMinutes] = start.split(':');
    const startDate = getDate(startDay, startHours, startMinutes);

    let endDay = startDay;
    let endHours, endMinutes;
    [endHours, endMinutes] = end.split(':');
    if (endHours < startHours || (startHours === endHours && endMinutes < startMinutes))
        endDay = (endDay + 1) % 7;
    const endDate = getDate(endDay, endHours, endMinutes);

    const event = {
        summary: title,
        start: {
            dateTime: startDate.toISOString(),
            timeZone: 'Europe/Bucharest'
        },
        end: {
            dateTime: endDate.toISOString(),
            timeZone: 'Europe/Bucharest'
        },
        description: description,
        recurrence: [
            'RRULE:FREQ=WEEKLY;UNTIL=20210630'
        ],
        reminders: {
            useDefault: false,
            overrides: [
                {
                    method: 'popup',
                    minutes: 15
                }
            ]
        }
    };

    try {
        const calendar = google.calendar({
            version: 'v3',
            auth: authorization
        });
        const result = await calendar.events.insert({
            auth: authorization,
            calendarId: 'primary',
            resource: event
        });
        return {
            link: result.data.htmlLink,
            id: result.data.id
        };
    } catch (error) {
        const response = {
            status: 'failed',
            message: error.message
        };
        res.status(StatusCodes.INTERNAL_SERVER_ERROR);
        res.end(JSON.stringify(response));
        logFailedResponse(req, response);
        return null;
    }
}

getEvents = async function (req, res, authorization) {
    try {
        const calendar = google.calendar({
            version: 'v3',
            auth: authorization
        });
        const today = new Date();
        const result = await calendar.events.list({
            calendarId: 'primary',
            timeMin: today.toISOString(),
            timeMax: getNextWeek(today),
            maxResults: 100,
            singleEvents: true,
            orderBy: 'startTime'
        });
        const events = result.data.items;
        const eventsList = [];
        if (events.length) {
            for (let event of events) {
                if (event.description !== undefined && event.description === description) {
                    eventsList.push(getEventObject(event));
                }
            }
        }
        return eventsList;
    } catch (error) {
        const response = {
            status: 'failed',
            message: error.message
        };
        res.status(StatusCodes.INTERNAL_SERVER_ERROR);
        res.end(JSON.stringify(response));
        logFailedResponse(req, response);
        return null;
    }
}

getEvent = async function (req, res, authorization, id) {
    try {
        const calendar = google.calendar({
            version: 'v3',
            auth: authorization
        });
        const result = await calendar.events.get({
            auth: authorization,
            calendarId: 'primary',
            eventId: id
        });
        return getEventObject(result.data);
    } catch (error) {
        const response = {
            status: 'failed',
            message: error.message
        };
        res.status(StatusCodes.INTERNAL_SERVER_ERROR);
        res.end(JSON.stringify(response));
        logFailedResponse(req, response);
        return null;
    }
}

getEventObject = function (event) {
    const day = weekDays[new Date(event.start.dateTime).getDay() - 1];
    const startDate = new Date(event.start.dateTime);
    let start;
    let hours = startDate.getHours();
    if (hours < 10) {
        start = '0' + hours;
    } else {
        start = hours;
    }
    start += ':';
    let minutes = startDate.getMinutes();
    if (minutes < 10) {
        start += '0' + minutes;
    } else {
        start += minutes;
    }
    const endDate = new Date(event.end.dateTime);
    let end;
    hours = endDate.getHours();
    if (hours < 10) {
        end = '0' + hours;
    } else {
        end = hours;
    }
    end += ':';
    minutes = endDate.getMinutes();
    if (minutes < 10) {
        end += '0' + minutes;
    } else {
        end += minutes;
    }
    const summary = event.summary;
    return {
        link: event.htmlLink,
        id: event.recurringEventId,
        title: summary,
        day: day,
        start: start,
        end: end
    };
}

deleteEvent = async function (req, res, authorization, id) {
    try {
        const calendar = google.calendar({
            version: 'v3',
            auth: authorization
        });
        const result = await calendar.events.delete({
            auth: authorization,
            calendarId: 'primary',
            eventId: id
        });
        return result.data;
    } catch (error) {
        const response = {
            status: 'failed',
            message: error.message
        };
        res.status(StatusCodes.INTERNAL_SERVER_ERROR);
        res.end(JSON.stringify(response));
        logFailedResponse(req, response);
        return null;
    }
}

module.exports = {
    getEvents,
    createEvent,
    getEvent,
    deleteEvent
}