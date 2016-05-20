var WorkspaceController	= require('./WorkspaceController');
var config				= require('../utils/config');

function PluginsController() {};

PluginsController.prototype.getAllPlugins = function(callback) {
	var projects = [];

	WorkspaceController.FindByName(config.che.workspace, function(workspace) {
		if (!workspace)
			return callback(undefined, 'Internal Error');
		else if (workspace.message)
			return callback(undefined, workspace.message);
		else {
			workspace.config.projects.forEach(function(project) {
				projects.push({
					name 		: project.name,
					description : project.description,
					path 		: 'http://' + config.che.host + ':' + config.che.port + '/ide/' + workspace.config.name + project.path
				});
			});

			return callback(projects, undefined);
		}
	});
};

PluginsController.prototype.addPlugin = function(plugin, callback) {

};

PluginsController.prototype.removePlugin = function(plugin, callback) {

};

PluginsController.prototype.viewPlugin = function(plugin, callback) {

};

module.exports = new PluginsController();