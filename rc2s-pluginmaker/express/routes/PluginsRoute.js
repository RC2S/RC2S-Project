var PluginsController 	= require('../controllers').PluginsController;
var CommonUtils 		= require('../utils/CommonUtils');

module.exports = function(app) {

	app.get('/plugins', function(req, res, next) {
		PluginsController.getAllPlugins(function(plugins, errors) {
			res.render('plugins', {
				plugins : plugins,
				flash	: {error : CommonUtils.formatFormErrors(errors)}
			});
		});
	});
};