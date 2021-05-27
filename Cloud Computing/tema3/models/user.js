const {Datastore} = require('@google-cloud/datastore');
const {isEmpty} = require('lodash');

const datastore = new Datastore();
const kind = 'Users';

const getEmailByUuid = async function (uuid) {
    const getUsersByUuid = await datastore.createQuery(kind).filter('uuid', '=', uuid);
    const [users] = await datastore.runQuery(getUsersByUuid);
    if (isEmpty(users)) {
        return null;
    }
    return users[0].email;
}

module.exports = {
    getEmailByUuid
};