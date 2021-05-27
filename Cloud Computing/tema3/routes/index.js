const routerAPI = require('express').Router();

const account = require('./account');
const classroom = require('./classroom');
const schedule = require('./schedule');
const oauth = require('./oAuth');
const oAuthCallback = require('./oAuthCallback');
const calendar = require('./calendar');

routerAPI.use('', account);
routerAPI.use('/classrooms', classroom);
routerAPI.use('/classrooms', schedule);
routerAPI.use('/oauth', oauth);
routerAPI.use('/oauth-callback', oAuthCallback);
routerAPI.use('/calendar', calendar);

module.exports = {
    routerAPI
};