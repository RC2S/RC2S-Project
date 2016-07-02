var mysql 	= require('mysql');
var crypto 	= require('crypto');
var logger 	= require('../utils/LogUtils');
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
		INNER JOIN link_user_role ur ON ur.role = r.id \
		INNER JOIN user u ON u.id = ur.user \
		AND u.username = ? AND u.password = ? \
		AND u.activated = 1 AND u.locked = 0 \
	';	

	var shaPass = crypto.createHash('sha1').update(req.body.password).digest('hex');
	var password = crypto.createHash('sha1').update(config.database.salt + shaPass + config.database.pepper).digest('hex');

	conn.query(query, [req.body.username, password], function(err, rows, fields) {

		if (err) {
			logger.writeQueryLog(err, query);
			return callback(false, 'Internal Error');
		}

		if (rows.length == 0) {
			logger.writeQueryLog('User not found !', query);
			return callback(false, 'Invalid Login');
		} else if (rows[0]["name"] == 'ADMIN') {

			// Ensure hash uniqueness
			var currentDate = (new Date()).valueOf().toString();
			var random = Math.random().toString();

			// Token base
			var shaToken = crypto.createHmac('sha1', req.body.username + password)
				.update(currentDate + random)
				.digest('hex');

            logger.writeQueryLog('User found. Creating new token : ' + shaToken, query);

			req.session.token = shaToken;
			return callback(true, undefined);
		} else {
			logger.writeQueryLog('User not Admin !', query);
			res.redirect("/login");
		}
	});
};

module.exports = new UserController();