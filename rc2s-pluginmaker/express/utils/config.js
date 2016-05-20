/*
 * Application Configurations
 */

module.exports = {
	port 	: 8081,
	secret 	: 'ssshhhhh',
	database : {
		type		: 'mysql',
		name 		: 'RC2S',
		host 		: 'localhost',
		port 		: 3306,
		user 		: 'root',
		password 	: 'root',
	},
	che : {
		host		: '192.168.1.108',
		port		: 8080,
		contentType : 'application/json',
		mainPath 	: '/api/',
		authMethods : ['GET', 'POST', 'PUT', 'DELETE'],
		workspace	: 'rc2s'
	},
	log4js : {
		dirname 	: __dirname + '/logs/',
		httpLogName : 'http_watcher.log',
		authLogName	: 'auth_watcher.log',
		dbLogName	: 'db_watcher.log'
	}
};