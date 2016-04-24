var method = 'POST';

var buildRequest = require("../utils/httpUtils").buildRequestFromParams;

var WorkspacePostUtils = function() {};

WorkspacePostUtils.prototype.StartWorkspace = (wsID, callback) => {

	var errorsMapSerial = 'POSTSWS1';

	var apiPath = '/api/workspace/' + wsID + '/runtime';

	buildRequest(errorsMapSerial, apiPath, method, callback);
};

WorkspacePostUtils.prototype.StartWorkspaceByName = (wsName, callback) => {

	var errorsMapSerial = 'POSTSWSBN1';

	var apiPath = '/api/workspace/name/' + wsName + '/runtime';

	buildRequest(errorsMapSerial, apiPath, method, callback);
};

WorkspacePostUtils.prototype.CreateSnapshot = (wsID, callback) => {

	var errorsMapSerial = 'POSTCS1';

	var apiPath = '/api/workspace/' + wsID + '/snapshot';

	buildRequest(errorsMapSerial, apiPath, method, callback);
};

module.exports = WorkspacePostUtils;