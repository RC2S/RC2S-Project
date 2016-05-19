var session 		= require('express-session');
var bodyParser 		= require('body-parser');
var cookieParser 	= require('cookie-parser');
var mustachex 		= require('mustachex');
var validator		= require('express-validator');
var config			= require('./utils/config');
var express 		= require('express');
var app 			= express();

module.exports = function() {
	
	/*** Configurations ***/

	// Mustache
	app.engine('html', mustachex.express);
	app.set('view engine', 'html');
	app.set('views', __dirname + '/views');
	app.use(express.static(__dirname + '/public'));
	app.set('layout', true);
	
	// Session
	app.use(session({
		secret: config.secret
	}));

	// BodyParser
	app.use(bodyParser.json());
	app.use(bodyParser.urlencoded({
		"extended": false
	}));

	// CookieParser (auth needed)
	app.use(cookieParser());

	// Express Validator
	app.use(validator());

	// Authentication middleware
	require('./utils/authenticator')(app);

	// App routes
	require('./routes')(app);

	app.listen(config.port, function() {
		console.log("Server running on port " + config.port);
	});
};