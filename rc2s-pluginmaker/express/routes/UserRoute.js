var UserController 	= require('../controllers').UserController;
var CommonUtils		= require('../utils/CommonUtils');

module.exports = function(app) {
	var errors;

	app.get('/', function(req, res, next) {
		if (req.session.token)
			return res.redirect('/plugins');

		res.render('login', {
			title 	: 'Connexion | RC2S-PluginMaker',
			css 	: ['login'],
			js 		: ['login','TweenLite.min'],
			flash	: {error : CommonUtils.formatFormErrors(errors)}
		});
		errors = null;
	});

	app.post('/', function(req, res, next) {
		UserController.login(req, function(valid, err) {
			if (valid)
				res.redirect('/plugins');
			else {	
				errors = err;
				res.redirect('/');
			}
		});
	});

	app.get('/logout', function(req, res, next) {
		if (req.session) {
			req.session.token = undefined;
			
			req.session.destroy(function(err) {
				if(err)
			   		console.log(err);
				else {
					errors = null;
					res.redirect('/');
				}
			});
		}
	});
};