var PluginsController 	= require('../controllers').PluginsController;
var CommonUtils 		= require('../utils/CommonUtils');
var config				= require('../utils/config');
var del 				= require('del');

module.exports = function(app) {
	var globalFlash = {};

	app.get('/plugins', function(req, res, next) {
		PluginsController.getAllPlugins(function(result, errors) {
			res.render('plugins', {
				title		: 'Accueil | RC2S-PluginMaker',
				wsStatus 	: (result ? result.status : null),
				plugins 	: (result ? result.projects : null),
				flash		: [{error : CommonUtils.formatFormErrors(errors)}, globalFlash],
				layout		: 'layoutPlugin'
			});
			globalFlash = {};
		});
	});

	app.get('/plugins/add', function(req, res, next) {
		res.render('addPlugin', {
			title	: 'Ajout Plugin | RC2S-PluginMaker',
			layout	: 'layoutPlugin'
		});
	});

	app.post('/plugins/add', function(req, res, next) {
		PluginsController.addPlugin(req, function(valid, errors) {
			if (valid)
				globalFlash = {success : '<div>Le plugin a bien été créé</div>'};
			else
				globalFlash = {error : CommonUtils.formatFormErrors(errors)};

			res.redirect('/plugins');
		});
	});

	app.get('/plugins/download/:name', function(req, res, next) {
		PluginsController.downloadZip(req.params.name, function(valid, errors) {
			if (valid)
				res.sendFile(config.che.tmpFolder + req.params.name + '.zip');
			else {
				globalFlash = {error : CommonUtils.formatFormErrors(errors)};
				res.redirect('/plugins');
			}

			del(config.che.tmpFolder + '/*');
		});
	});

	app.get('/plugins/remove/:name', function(req, res, next) {
		PluginsController.removePlugin(req.params.name, function(valid, errors) {
			if (valid)
				globalFlash = {success : '<div>Le plugin a bien été supprimé</div>'};
			else
				globalFlash = {error : CommonUtils.formatFormErrors(errors)};

			res.redirect('/plugins');
		});
	});
};