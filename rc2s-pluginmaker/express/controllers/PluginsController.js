var WorkspaceController	= require('./WorkspaceController');
var config				= require('../utils/config');
var recursive			= require('recursive-readdir');
var fs 					= require('fs');
var exec				= require('child_process').exec;

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
	req.checkBody('pluginDesc', 'Invalid Plugin Description').notEmpty().len(3, 100);

	var errors = req.validationErrors();

	if (errors)
		return callback(false, errors);

	var plugin = {
		name 		: req.body.pluginName,
		description : req.body.pluginDesc
	};

	var self = this;

	WorkspaceController.findByName(config.che.workspace, function(workspace, errWs) {
		if (errWs)
			return callback(false, errWs);

		WorkspaceController.addProjectToWS(workspace.id, plugin, function(res, errPj) {
			if (errPj)
				return callback(false, errPj);

			self.importTemplateToProject(workspace.id, plugin.name, function(res, errImport) {
				if (errImport)
					return callback(false, errImport);

				callback(true, undefined);
			});
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

PluginsController.prototype.importTemplateToProject = function(wsID, pluginName, callback) {
	
	recursive(config.che.template, function(err, files) {
		var folderAlreadyCreated;

		if (files) {
			files.forEach(function(absoluteFilePath) {
				var relativeFilePath 	= absoluteFilePath.substr(config.che.template.length, absoluteFilePath.length);
				var folders 			= relativeFilePath.substr(0, relativeFilePath.lastIndexOf('/'));
				var file 				= relativeFilePath.substr((folders ? relativeFilePath.lastIndexOf('/') : 0), relativeFilePath.length);

				console.log('--------------------- PATH : ' + relativeFilePath);

				if (folders && folders != folderAlreadyCreated) {
					console.log('----------------------- IN FOLDER CREATE');
					WorkspaceController.addFolderToProject(wsID, pluginName, folders, function(res, errFolder) {
						if (errFolder)
							return callback(false, errFolder);

						fs.readFile(absoluteFilePath, 'utf-8', function(err, data) {
							if (err)
								return callback(false, err);

							WorkspaceController.addFileToProject(wsID, pluginName, folders, file, data, function(res, errFile) {
								if (errFile)
									return callback(false, errFile);
							});
						});
					});
					folderAlreadyCreated = folders;
				}
				else {
					console.log('----------------------- NOT IN FOLDER CREATE');
					fs.readFile(absoluteFilePath, 'utf-8', function(err, data) {
						if (err)
							return callback(false, err);
						
						if (!folders)
							folders = '';

						WorkspaceController.addFileToProject(wsID, pluginName, folders, file, data, function(res, errFile) {
							if (errFile)
								return callback(false, errFile);
						});
					});
				}
			});
		}
		callback(true, undefined);
	});
};

PluginsController.prototype.downloadZip = function(pluginName, callback) {
	var dockerZipPath = pluginName + '-project/' + pluginName + '-client/build' + pluginName + '.zip';
	exec(config.che.dockerCpCommand + dockerZipPath + ' ' + config.che.downloadFolder.replace(/\s+/g, "\\ "), function(error, stdout, stderr) {
		if(error && stderr)
			return callback(false, stderr);
		else if(error)
			return callback(false, error);

		return callback(true, undefined);
	});
};

module.exports = new PluginsController();