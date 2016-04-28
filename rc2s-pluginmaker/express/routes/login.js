var mysql = require('mysql');

var logger = require("../utils/logUtils");

module.exports = (app) => {

	app.get('/login', (req, res, next) => {

		var html = "<!DOCTYPE html>\n";
		html += "<html>\n";
		html += "	<body>\n";
		html += "		<form method=\"POST\" action=\"\">\n";
		html += "			<input type=\"text\" name=\"username\" placeholder=\"Username\" />\n";
		html += "			<br />\n";
		html += "			<input type=\"password\" name=\"passwd\" placeholder=\"Pass\" />\n";
		html += "			<br />\n";
		html += "			<input type=\"submit\" value=\"Login !\" />\n";
		html += "		</form>\n";
		html += "	</body>\n";
		html += "</html>\n";

		res.type("html");
		res.send(html);
	});

	app.post('/login', (req, res, next) => {

		var username 	= req.body.username;
		var passwd 		= req.body.passwd;

		var credentials = {
			host : "localhost",
			user : "root",
			password : "root",
			database : "RC2S"
		};

		var conn = mysql.createConnection(credentials);

		conn.connect((err) => {
			if (err)
				logger.writeConnectionLog(err, credentials);

			logger.writeConnectionLog("Connection OK", credentials);
		});

		var query = 'SELECT u.* FROM user u WHERE username = ? AND password = ?';

		conn.query(query, [username, passwd], (err, rows, fields) => {

			if (err)
				logger.writeQueryLog(err, query);

			if (rows.length == 0) {

				logger.writeQueryLog("User not found !", query);

				res.redirect("/login");
			} else if (rows[0]["role"] == "admin") {

				// Crypto node module
				const crypto = require('crypto');

				// Ensure hash uniqueness
				var currentDate = (new Date()).valueOf().toString();
				var random = Math.random().toString();

				// Token base
				const secret = rows[0]["username"] + rows[0]["password"];
				
				// Hash creation
				const hmacToken = crypto.createHmac('sha1', secret)
                   .update(currentDate + random)
                   .digest('hex');
                   
				logger.writeQueryLog("User found. Creating new token : " + hmacToken, query);

				res.cookie("token", hmacToken, { maxAge : 900000 });		

				res.redirect("/workspaces");
			} else {

				logger.writeQueryLog("User not Admin !", query);

				res.redirect("/login");
			}
		});
	});

	app.get('/logout', (req, res, next) => {

		req.cookies.token = undefined;
		res.cookie("token", req.cookies.token, { maxAge : 0 });

		res.redirect("/login");
	});
};