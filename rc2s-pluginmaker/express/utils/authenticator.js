var logger = require("./logUtils");

module.exports = (app) => {

	app.use('/workspace\*', (req, res, next) => {

		if (req.cookies.token) {

			// Log every access to /workspace*
			logger.writeAuthAccess(req.originalUrl, req.cookies.token);
			
			next();
		} else {
			res.redirect('/login');
		}
	});
};