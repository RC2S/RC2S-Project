var logger = require("./logUtils");

var sess;

module.exports = (app) => {

	app.use('/workspace\*', (req, res, next) => {

		sess = req.session;

		if (sess.token) {

			// Log every access to /workspace*
			logger.writeAuthAccess(req.originalUrl, sess.token);
			
			next();
		} else {
			res.redirect('/login');
		}
	});
};