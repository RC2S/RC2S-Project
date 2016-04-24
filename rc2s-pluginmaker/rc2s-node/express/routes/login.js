var mysql = require("mysql");

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

		var username = req.body.username;
		var passwd = req.body.passwd;

		var conn = mysql.createConnection({
			host : "localhost",
			user : "root",
			password : "root",
			database : "RC2S"
		});

		conn.connect((err) => {
			if (err)
				console.error(err);

			console.log("Connection OK");
		});

		console.log("User : " + username + ", pass : " + passwd);

		conn.query("SELECT u.* FROM user u WHERE username = ? AND password = ?", 
			[username, passwd],
			(err, rows, fields) => {
			if (err)
				console.error(err);

			console.log(rows.length + " => " + rows);

			if (rows.length == 0) {

				console.log("NOT FOUND!");
				res.redirect("/login");
			} else {

				var token = req.cookies.token;
				token = "NEWTOKEN";

				console.log("Creating new token : " + token);

				res.cookie("token", token, { maxAge : 900000 });		

				res.redirect("/login");
			}
		});
	});
};