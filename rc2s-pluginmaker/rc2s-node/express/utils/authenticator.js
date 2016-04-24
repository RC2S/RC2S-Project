module.exports = (app) => {

	app.use('/workspace\*', (req, res, next) => {

		if (req.cookies.token) {
			console.log("Token found in middleware : " + req.cookies.token);
			next();
		} else {
			res.redirect('/login');
		}
	});

	app.use('/logout', (req, res, next) => {

		req.cookies.token = undefined;
	});
};