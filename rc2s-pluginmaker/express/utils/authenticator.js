var logger = require("./logUtils");

module.exports = (app) => {

	app.use('/workspace\*', (req, res, next) => {

		if (req.session.token) {

			// Log every access to /workspace*
			logger.writeAuthAccess(req.originalUrl, req.session.token);
			
			next();
		} else {
			res.redirect('/login');
		}
	});
};