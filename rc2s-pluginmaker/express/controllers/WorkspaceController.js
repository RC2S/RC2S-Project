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

WorkspaceController.prototype.startWorkspace = function(wsID, callback) {
	requestApi('POSTSWS1',' workspace/' + wsID + '/runtime', 'POST', undefined, callback);
};

WorkspaceController.prototype.startWorkspaceByName = function(wsName, callback) {
	requestApi('POSTSWSBN1', 'workspace/name/' + wsName + '/runtime', 'POST', undefined, function(res) {
		if (res.statusCode == 200)
			callback(res.content, undefined);
		else
			callback(undefined, res.content);
	});
};

WorkspaceController.prototype.createSnapshot = function(wsID, callback) {
	requestApi('POSTCS1', 'workspace/' + wsID + '/snapshot', 'POST', undefined, callback);
};

WorkspaceController.prototype.deleteWSByID = function(wsID, callback) {
	requestApi('DELWSBID1', 'workspace/' + wsID, 'DELETE', undefined, callback);
};

WorkspaceController.prototype.addProjectToWS = function(wsID, plugin, callback) {
	var data = {
		name		: plugin.name,
		description : plugin.description,
		type		: 'java',
		attributes	: {},
		links		: [],	
		source	: {
			location	: null,
			type		: null,
			parameters	: {}
		},
		path		: '/' + plugin.name,
		contentRoot	: null,
		problems	: [],
		mixins		: []
	};

	requestApi('POSTAPFWS', 'ext/project/' + wsID + '?name=' + plugin.name, 'POST', data, function(res) {
		if (res.statusCode == 200)
			callback(res.content, undefined);
		else
			callback(undefined, res.content);
	});
};

WorkspaceController.prototype.removeProjectFromWS = function(wsID, pjName, callback) {
	requestApi('DELRPFWS', 'ext/project/' + wsID + '/' + pjName, 'DELETE', undefined, function(res) {
		if (res.statusCode == 200)
			callback(res.content, undefined);
		else
			callback(undefined, res.content);
	});
};

WorkspaceController.prototype.stopWorkspace = function(wsID, callback) {
	requestApi('DELSWS1', 'workspace/' + wsID + '/runtime', 'DELETE', undefined, callback);
};

WorkspaceController.prototype.addFolderToProject = function(wsID, prjName, folderPath, callback) {
	requestApi('POSTAFTP', 'ext/project/' + wsID + '/folder/' + prjName + '/' + folderPath, 'POST', undefined, function(res) {
		if (res.statusCode == 201)
			callback(res.content, undefined);
		else
			callback(undefined, res.content);
	});
};

WorkspaceController.prototype.addFileToProject = function(wsID, prjName, folderPath, fileName, content, callback) {
	requestApi('POSTAFITP', 'ext/project/' + wsID + '/file/' + prjName  + '/' + folderPath + '?name=' + fileName, 'POST', content, function(res) {
		if (res.statusCode == 201)
			callback(res.content, undefined);
		else
			callback(undefined, res.content);
	}, 'application/x-www-form-urlencoded');
};

module.exports = new WorkspaceController();