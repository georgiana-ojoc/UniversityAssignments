const express = require('express');
const swagger = require('./swagger.json');
const swaggerUi = require('swagger-ui-express');
const {routerAPI} = require('./routes');
const {StatusCodes} = require('http-status-codes');
const {google} = require('googleapis');
const config = require('config');

const {client_id, client_secret, redirect_uri} = config.get('oauth');
global.oAuth2Client = new google.auth.OAuth2(client_id, client_secret, redirect_uri);

const app = express();

let port = process.env.PORT;
if (port === undefined || port === null || port === "") {
    port = 8000;
}

function errorHandler(error, request, response, _) {
    const body = {
        status: 'failed',
        message: 'Request body is not valid.'
    };
    response.status(StatusCodes.BAD_REQUEST);
    response.end(JSON.stringify(body));
}

app.use(express.urlencoded({extended: true}));
app.use(express.json());
app.use(errorHandler);

app.use(function (req, res, next) {
    res.header('Access-Control-Allow-Origin', '*');
    res.header('Access-Control-Allow-Headers', '*');
    res.header('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE, OPTIONS');
    if (!req.url.includes('swagger')) {
        res.header('Content-Type', 'application/json');
    }
    next();
});

app.use('', routerAPI);
app.use('/swagger', swaggerUi.serve, swaggerUi.setup(swagger));

app.listen(port, () => {
    console.log(`listening on port ${port}...`);
});