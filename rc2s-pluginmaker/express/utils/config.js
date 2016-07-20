var path = require('path');

/*
 * Application Configurations
 */

module.exports = {
	port	: 8081,
	secret	: '^&yv<nVgGy75:gN+',
	database : {
		type			: 'mysql',
		name			: 'RC2S',
		host			: 'localhost',
		port			: 3306,
		user			: 'root',
		password		: 'root',
		salt			: 'c33A0{-LO;<#CB `k:^+8DnxAa.BX74H07z:Qn+U0yD$3ar+.:ge[nc>Trs|Fxy',
		pepper			: '>m9I}JqHTg:VZ}XISdcG;)yGu)t]7Qv5YT:ZWI^#]f06Aq<c]n7a? x+ZEl#pt:'
	},
	che : {
		host			: '192.168.1.108',
		port			: 8080,
		contentType 	: 'application/json',
		mainPath		: '/api/',
		authMethods 	: ['GET', 'POST', 'PUT', 'DELETE'],
		workspace		: 'rc2s',
		template		: __dirname + '/template/',
		templateFolder	: path.resolve(__dirname + '/template') + '/',
		tmpFolder 		: path.resolve(__dirname + '/tmp') + '/',
		prefixImport	: '[pluginname]'
	},
	log4js : {
		dirname 		: __dirname + '/logs/',
		httpLogName 	: 'http_watcher.log',
		authLogName		: 'auth_watcher.log',
		dbLogName		: 'db_watcher.log'
	}
};