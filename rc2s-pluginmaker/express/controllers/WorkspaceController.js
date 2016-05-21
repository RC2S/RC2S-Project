var requestApi = require('../utils/HttpUtils');

function WorkspaceController() {};

WorkspaceController.prototype.findAll = function(callback) {
	requestApi('GETFA1', 'workspace?skipCount=0&maxItems=30', 'GET', undefined, callback);
};

WorkspaceController.prototype.findByName = function(wsName, callback) {
	requestApi('GETFBN1', 'workspace/name/' + wsName, 'GET', undefined, function(res) {
		if (res.statusCode == 200)
			callback(res.content, undefined);
		else
			callback(undefined, res.content);
	});
};

WorkspaceController.prototype.findByID = function(wsID, callback) {
	requestApi('GETFBID1', 'workspace/' + wsID, 'GET', undefined, callback);
};

WorkspaceController.prototype.findAllRuntime = function(callback) {
	requestApi('GETFAR1', 'workspace/runtime?skipCount=0&maxItems=30', 'GET', undefined, callback);
};

WorkspaceController.prototype.findRuntimeByID = function(wsID, callback) {
	requestApi('GETFRBID1', 'workspace/' + wsID + '/runtime', 'GET', undefined, callback);
};

WorkspaceController.prototype.findSnapshotByID = function(wsID, callback) {
	requestApi('GETFSBID1', 'workspace/' + wsID + '/snapshot', 'GET', undefined, callback);
};

WorkspaceController.prototype.StartWorkspace = function(wsID, callback) {
	requestApi('POSTSWS1',' workspace/' + wsID + '/runtime', 'POST', undefined, callback);
};

WorkspaceController.prototype.startWorkspaceByName = function(wsName, callback) {
	requestApi('POSTSWSBN1', 'workspace/name/' + wsName + '/runtime', 'POST', undefined, callback);
};

WorkspaceController.prototype.createSnapshot = function(wsID, callback) {
	requestApi('POSTCS1', 'workspace/' + wsID + '/snapshot', 'POST', undefined, callback);
};

WorkspaceController.prototype.deleteWSByID = function(wsID, callback) {
	requestApi('DELWSBID1', 'workspace/' + wsID, 'DELETE', undefined, callback);
};

WorkspaceController.prototype.addProjectToWS = function(wsID, plugin, callback) {
	var data = {
		name 		: plugin.name,
		description : plugin.description,
		type 		: 'java',
		attributes 	: {},
		links 		: [],	
		source 	: {
			location 	: null,
			type 		: null,
			parameters 	: {}
		},
		path 		: '/' + plugin.name,
		problems 	: [],
		mixins 		: []
	};

	requestApi('POSTAPFWS', 'workspace/' + wsID + '/project/', 'POST', data, function(res) {
		if (res.statusCode == 200)
			callback(res.content, undefined);
		else
			callback(undefined, res.content);
	});
};

WorkspaceController.prototype.removeProjectFromWS = function(wsID, pjName, callback) {
	requestApi('DELRPFWS', 'workspace/' + wsID + '/project/' + pjName, 'DELETE', undefined, function(res) {
		if (res.statusCode == 200)
			callback(res.content, undefined);
		else
			callback(undefined, res.content);
	});
};

WorkspaceController.prototype.stopWorkspace = function(wsID, callback) {
	requestApi('DELSWS1', 'workspace/' + wsID + '/runtime', 'DELETE', undefined, callback);
};

module.exports = new WorkspaceController();