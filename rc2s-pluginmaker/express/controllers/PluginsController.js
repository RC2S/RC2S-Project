var WorkspaceController	= require('./WorkspaceController');
var config				= require('../utils/config');
var copy				= require('recursive-copy');
var through				= require('through2');
var fs 					= require('fs');
var del					= require('del');
var exec				= require('child_process').exec;
var unidecode			= require('unidecode');
// var recursive		= require('recursive-readdir');

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

			if (workspace.status == 'STOPPED')
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
	
	req.checkBody('pluginName', 'Invalid Plugin Name').notEmpty().len(3, 20).notSpecialChars();
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

	// Transform PluginName for package standard
	formatedPluginName = unidecode(pluginName); 			// Transform non ASCII to ASCII : é -> e
	formatedPluginName = pluginName.replace(/\W/g, ''); 	// Remove non alphanumeric

	var options = {
		overwrite: true,
		expand: true,
		dot: true,
		junk: true,
		filter: [
			'**/*'
		],
		rename: function(filePath) {
			return filePath.split('[pluginname]').join(formatedPluginName);
		},
		transform: function(src, dest, stats) {
			return through(function(chunk, enc, done)  {
				var output = chunk.toString().split('[pluginname]').join(formatedPluginName);
				done(null, output);
			});
		}
	};

	// Copy files from the template folder to the tmp folder by replacing [pluginname]
	// in the folder names and file contents by the plugin name
	copy(config.che.templateFolder, config.che.tmpFolder + formatedPluginName, options)
		.then(function(results) {
			console.log('Copied ' + results.length + ' files');

			exec('docker ps | cut -d" " -f1 | sed -n 2p', function(errorPs, idDockerMachine, stderrPs) {
				if (errorPs && stderrPs)
					return callback(false, stderrPs);
				else if (errorPs)
					return callback(false, errorPs);

				idDockerMachine = idDockerMachine.replace(/(\r\n|\r|\n|\s)/gm, '');

				exec('docker cp ' + config.che.tmpFolder.replace(/\s+/g, "\\ ") + formatedPluginName + '/. ' + idDockerMachine + ':/projects/' + pluginName, function(error, stdout, stderr) {
					if (error && stderr)
						return callback(false, stderr);
					else if (error)
						return callback(false, error);

					del(config.che.tmpFolder + '/*');
					return callback(true, undefined);
				});
			});
		})
		.catch(function(error) {
			callback(false, error);
		});

	// Async problems : files are created before folders in some cases
	/*recursive(config.che.template, function(errListFiles, files) {
		if (errListFiles)
			return callback(false, errListFiles);

		if (files) {
			files.forEach(function(absoluteFilePath) {
				var relativeFilePath 	= absoluteFilePath.substr(config.che.template.length, absoluteFilePath.length);
				var folders 			= relativeFilePath.substr(0, relativeFilePath.lastIndexOf('/'));
				var file 				= relativeFilePath.substr((folders ? relativeFilePath.lastIndexOf('/') : 0), relativeFilePath.length);

				WorkspaceController.addFolderToProject(wsID, pluginName, folders, function(resFolder, errFolder) {
					console.log(resFolder);
					if (errFolder)
						return callback(false, errFolder);

					var data = fs.readFileSync(absoluteFilePath, 'utf-8');
					
					if (!data) 
						return callback(false, 'no data');

					WorkspaceController.addFileToProject(wsID, pluginName, folders, file, data, function(resFile, errFile) {
						if (errFile)
							return callback(false, errFile);
					});
				});
			});
		}
		callback(true, undefined);
	});*/
};

PluginsController.prototype.downloadZip = function(pluginName, callback) {
	exec('docker ps | cut -d" " -f1 | sed -n 2p', function(errorPs, idDockerMachine, stderrPs) {
		if(errorPs && stderrPs)
			return callback(false, stderrPs);
		else if(errorPs)
			return callback(false, errorPs);

		idDockerMachine = idDockerMachine.replace(/(\r\n|\r|\n|\s)/gm, '');
		
		var pluginZipPath = pluginName + '-project/' + pluginName + '-client/build' + pluginName + '.zip';
		exec('docker cp ' + idDockerMachine + ':/projects/' + pluginZipPath + ' ' + config.che.tmpFolder.replace(/\s+/g, "\\ "), function(error, stdout, stderr) {
			if(error && stderr)
				return callback(false, stderr);
			else if(error)
				return callback(false, error);

			return callback(true, undefined);
		});
	});
};

module.exports = new PluginsController();