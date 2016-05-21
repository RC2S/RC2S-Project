var logger = require("./logUtils");

module.exports = function(app) {

	app.use('/plugins\*', function(req, res, next) {

		if (req.session.token) {
			// Log every access to /plugins*
			logger.writeAuthAccess(req.originalUrl, req.session.token);
			next();
		} else {
			res.redirect('/');
		}
	});
};