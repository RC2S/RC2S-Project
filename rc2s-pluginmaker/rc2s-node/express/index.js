module.exports = () => {
	var express = require("express");
	var app = express();

	var bodyParser = require("body-parser");
	//app.use(bodyParser.json());
	app.use(bodyParser.urlencoded({
		"extended": false
	}));

	// Used for auth
	var cookieParser = require("cookie-parser");
	app.use(cookieParser());

	// Authentication middleware
	require("./utils/authenticator")(app);

	// App routes
	require("./routes")(app);

	app.listen(8080, () => {
		console.log("Server running on port 8080...");
	});
};