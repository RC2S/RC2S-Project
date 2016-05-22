var WorkspaceController	= require('./WorkspaceController');
var config				= require('../utils/config');

function PluginsController() {};

PluginsController.prototype.getAllPlugins = function(callback) {
	WorkspaceController.findByName(config.che.workspace, function(workspace, errors) {
		var projects = [];

		if (errors != undefined)
			return callback(undefined, errors);
		else if (workspace.message)
			return callback(undefined, workspace.message);
		else {
			if (workspace.config.projects)
				workspace.config.projects.forEach(function(project) {
					projects.push({
						name 		: project.name,
						description : project.description,
						path 		: 'http://' + config.che.host + ':' + config.che.port + '/ide/' + workspace.config.name + project.path
					});
				});

			if(workspace.status == 'STOPPED')
				WorkspaceController.startWorkspaceByName(workspace.config.name, function(wsState, errStart) {
					if (errStart != undefined)
						return callback(undefined, errStart);
					
					return callback({ status : wsState.status, projects : projects }, undefined);
				});
			else
				return callback({ status : workspace.status, projects : projects }, undefined);
		}
	});
};

PluginsController.prototype.addPlugin = function(req, callback) {
	
	req.checkBody('pluginName', 'Invalid Plugin Name').notEmpty().len(3, 20);
	req.checkBody('pluginDesc', 'Invalid Plugin Description').notEmpty().len(3, 20);

	var errors = req.validationErrors();

	if (errors)
		return callback(false, errors);

	var plugin = {
		name 		: req.body.pluginName,
		description : req.body.pluginDesc
	};

	WorkspaceController.findByName(config.che.workspace, function(workspace, errWs) {
		if (errWs)
			return callback(false, errWs);

		WorkspaceController.addProjectToWS(workspace.id, plugin, function(res, errPj) {
			if (errPj)
				return callback(false, errPj);

			callback(true, undefined);
		});
	});
};

PluginsController.prototype.removePlugin = function(pluginName, callback) {
	WorkspaceController.findByName(config.che.workspace, function(workspace, errWs) {
		if (errWs)
			return callback(false, errWs);

		WorkspaceController.removeProjectFromWS(workspace.id, pluginName, function(res, errPj) {
			if (errPj)
				return callback(false, errPj);

			callback(true, undefined);
		});
	});
};

module.exports = new PluginsController();