var method = 'DELETE';

var buildRequest = require("../utils/httpUtils").buildRequestFromParams;

var WorkspaceDeleteUtils = function() {};

WorkspaceDeleteUtils.prototype.DeleteWSByID = (wsID, callback) => {

	var errorsMapSerial = 'DELWSBID1';

	var apiPath = '/api/workspace/' + wsID;

	buildRequest(errorsMapSerial, apiPath, method, callback);
};

WorkspaceDeleteUtils.prototype.RemoveProjectFromWS = (wsID, pjName, callback) => {

	var errorsMapSerial = 'DELRPFWS';

	var apiPath = '/api/workspace/' + wsID + '/project/' + pjName;

	buildRequest(errorsMapSerial, apiPath, method, callback);
};

WorkspaceDeleteUtils.prototype.StopWorkspace = (wsID, callback) => {

	var errorsMapSerial = 'DELSWS1';

	var apiPath = '/api/workspace/' + wsID + '/runtime';

	buildRequest(errorsMapSerial, apiPath, method, callback);
};

module.exports = WorkspaceDeleteUtils;