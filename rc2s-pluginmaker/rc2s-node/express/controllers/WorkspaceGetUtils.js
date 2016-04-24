var method = 'GET';

var buildRequest = require("../utils/httpUtils").buildRequestFromParams;

var WorkspaceGetUtils = function() {};

WorkspaceGetUtils.prototype.FindAll = (callback) => {

	var errorsMapSerial = 'GETFA1';

	var apiPath = '/api/workspace?skipCount=0&maxItems=30';

	buildRequest(errorsMapSerial, apiPath, method, callback);
};

WorkspaceGetUtils.prototype.FindByName = (wsName, callback) => {

	var errorsMapSerial = 'GETFBN1';

	var apiPath = '/api/workspace/name/' + wsName;

	buildRequest(errorsMapSerial, apiPath, method, callback);
};

WorkspaceGetUtils.prototype.FindByID = (wsID, callback) => {

	var errorsMapSerial = 'GETFBID1';

	var apiPath = '/api/workspace/' + wsID;

	buildRequest(errorsMapSerial, apiPath, method, callback);
};

WorkspaceGetUtils.prototype.FindAllRuntime = (callback) => {

	var errorsMapSerial = 'GETFAR1';

	var apiPath = '/api/workspace/runtime?skipCount=0&maxItems=30';

	buildRequest(errorsMapSerial, apiPath, method, callback);
};

WorkspaceGetUtils.prototype.FindRuntimeByID = (wsID, callback) => {

	var errorsMapSerial = 'GETFRBID1';

	var apiPath = '/api/workspace/' + wsID + '/runtime';

	buildRequest(errorsMapSerial, apiPath, method, callback);
};

module.exports = WorkspaceGetUtils;