var logger = require("./logUtils");

module.exports = function(app) {

	app.use('/workspace\*', function(req, res, next) {

		if (req.session.token) {

			// Log every access to /workspace*
			logger.writeAuthAccess(req.originalUrl, req.session.token);
			
			next();
		} else {
			res.redirect('/');
		}
	});
};