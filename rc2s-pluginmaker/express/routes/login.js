var mysql = require('mysql');

var logger = require("../utils/logUtils");

var sess;

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

		var query = "\
			SELECT r.name \
			FROM role r \
			INNER JOIN user_role ur ON ur.role_id = r.id \
			INNER JOIN user u ON u.id = ur.user_id \
			AND u.username = ? AND u.password = ? \
		";

		conn.query(query, [username, passwd], (err, rows, fields) => {

			if (err)
				logger.writeQueryLog(err, query);

			if (rows.length == 0) {

				logger.writeQueryLog("User not found !", query);

				res.redirect("/login");
			} else if (rows[0]["name"] == "ROLE_ADMIN") {

				// Crypto node module
				const crypto = require('crypto');

				// Ensure hash uniqueness
				var currentDate = (new Date()).valueOf().toString();
				var random = Math.random().toString();

				// Token base
				const secret = rows[0]["username"] + rows[0]["password"];

				const shaToken = crypto.createHmac('sha1', 'secret')
					.update(currentDate + random)
					.digest('hex');
                   
                logger.writeQueryLog("User found. Creating new token : " + shaToken, query);

				sess = req.session;
				sess.token = shaToken;		

				res.redirect("/workspaces");
			} else {

				logger.writeQueryLog("User not Admin !", query);

				res.redirect("/login");
			}
		});
	});

	app.get('/logout', (req, res, next) => {

		if (req.session) {

			req.session.token = undefined;

			req.session.destroy(function(err) {

				if(err)
			   		console.log(err);
				else
					res.redirect("/login");
			});
		}
	});
};