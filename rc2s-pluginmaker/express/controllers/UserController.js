var mysql 	= require('mysql');
var crypto 	= require('crypto');
var logger 	= require('../utils/logUtils');
var config	= require('../utils/config');

function UserController() {};

UserController.prototype.login = function(req, callback) {
	
	req.checkBody('username', 'Invalid username').notEmpty().len(5, 20);
	req.checkBody('password', 'Invalid password').notEmpty().len(5, 20);

	var errors = req.validationErrors();

	if (errors)
		return callback(false, errors);

	var options = {
		host 		: config.database.host,
		user 		: config.database.user,
		password 	: config.database.password,
		database 	: config.database.name
	};

	var conn = mysql.createConnection(options);

	conn.connect(function(err) {
		if (err) {
			logger.writeConnectionLog(err, options);
			return callback(false, 'Internal Error');
		}

		logger.writeConnectionLog('Connection OK', options);
	});

	var query = '\
		SELECT r.name \
		FROM role r \
		INNER JOIN user_role ur ON ur.role_id = r.id \
		INNER JOIN user u ON u.id = ur.user_id \
		AND u.username = ? AND u.password = ? \
	';

	conn.query(query, [req.body.username, req.body.password], function(err, rows, fields) {

		if (err) {
			logger.writeQueryLog(err, query);
			return callback(false, 'Internal Error');
		}

		if (rows.length == 0) {
			logger.writeQueryLog('User not found !', query);
			return callback(false, 'Invalid Login');
		} else if (rows[0]["name"] == 'ROLE_ADMIN') {

			// Ensure hash uniqueness
			var currentDate = (new Date()).valueOf().toString();
			var random = Math.random().toString();

			// Token base
			var secret = rows[0]['username'] + rows[0]['password'];

			var shaToken = crypto.createHmac('sha1', 'secret')
				.update(currentDate + random)
				.digest('hex');

            logger.writeQueryLog('User found. Creating new token : ' + shaToken, query);

			req.session.token = shaToken;
			return callback(true, undefined);
		} else {
			logger.writeQueryLog('User not Admin !', query);
			//res.redirect("/login");
		}
	});
};

module.exports = new UserController();