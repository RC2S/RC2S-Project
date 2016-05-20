var requestApi = require('../utils/httpUtils');

function WorkspaceController() {};

WorkspaceController.prototype.findAll = function(callback) {
	requestApi('GETFA1', 'workspace?skipCount=0&maxItems=30', 'GET', undefined, callback);
};

WorkspaceController.prototype.FindByName = function(wsName, callback) {
	requestApi('GETFBN1', 'workspace/name/' + wsName, 'GET', undefined, callback);
};

WorkspaceController.prototype.FindByID = function(wsID, callback) {
	requestApi('GETFBID1', 'workspace/' + wsID, 'GET', undefined, callback);
};

WorkspaceController.prototype.FindAllRuntime = function(callback) {
	requestApi('GETFAR1', 'workspace/runtime?skipCount=0&maxItems=30', 'GET', undefined, callback);
};

WorkspaceController.prototype.FindRuntimeByID = function(wsID, callback) {
	requestApi('GETFRBID1', 'workspace/' + wsID + '/runtime', 'GET', undefined, callback);
};

WorkspaceController.prototype.FindSnapshotByID = function(wsID, callback) {
	requestApi('GETFSBID1', 'workspace/' + wsID + '/snapshot', 'GET', undefined, callback);
};

WorkspaceController.prototype.StartWorkspace = function(wsID, callback) {
	requestApi('POSTSWS1',' workspace/' + wsID + '/runtime', 'POST', undefined, callback);
};

WorkspaceController.prototype.StartWorkspaceByName = function(wsName, callback) {
	requestApi('POSTSWSBN1', 'workspace/name/' + wsName + '/runtime', 'POST', undefined, callback);
};

WorkspaceController.prototype.CreateSnapshot = function(wsID, callback) {
	requestApi('POSTCS1', 'workspace/' + wsID + '/snapshot', 'POST', undefined, callback);
};

WorkspaceController.prototype.DeleteWSByID = function(wsID, callback) {
	requestApi('DELWSBID1', 'workspace/' + wsID, 'DELETE', undefined, callback);
};

WorkspaceController.prototype.RemoveProjectFromWS = function(wsID, pjName, callback) {
	requestApi('DELRPFWS', 'workspace/' + wsID + '/project/' + pjName, 'DELETE', undefined, callback);
};

WorkspaceController.prototype.StopWorkspace = function(wsID, callback) {
	requestApi('DELSWS1', 'workspace/' + wsID + '/runtime', 'DELETE', undefined, callback);
};

module.exports = new WorkspaceController();