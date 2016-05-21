var PluginsController 	= require('../controllers').PluginsController;
var CommonUtils 		= require('../utils/CommonUtils');

module.exports = function(app) {
	var globalFlash = {};

	app.get('/plugins', function(req, res, next) {
		PluginsController.getAllPlugins(function(plugins, errors) {
			res.render('plugins', {
				title	: 'Accueil | RC2S-PluginMaker',
				plugins : plugins,
				flash	: [{error : CommonUtils.formatFormErrors(errors)}, globalFlash],
				layout	: 'layoutPlugin'
			});
			globalFlash = {};
		});
	});

	app.get('/plugins/add', function(req, res, next) {
		res.render('addPlugin', {
			layout : 'layoutPlugin'
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