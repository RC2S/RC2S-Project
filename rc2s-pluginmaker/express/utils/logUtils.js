var log4js = require('log4js');
log4js.loadAppender('file');

var basePath = __dirname + '/logs/';
var logExt = '.log';

var httpLogsName 	= 'http_watcher';
var authLogsName 	= 'auth_watcher';
var dbLogsName 		= 'db_watcher';

var httpLogsPath 	= basePath + httpLogsName + logExt;
var authLogsPath 	= basePath + authLogsName + logExt;
var dbLogsPath 		= basePath + dbLogsName + logExt;

log4js.addAppender(log4js.appenders.file(httpLogsPath), httpLogsName)
log4js.addAppender(log4js.appenders.file(authLogsPath), authLogsName)
log4js.addAppender(log4js.appenders.file(dbLogsPath), dbLogsName)

var httpLogger 	= log4js.getLogger(httpLogsName);
var authLogger 	= log4js.getLogger(authLogsName);
var dbLogger 	= log4js.getLogger(dbLogsName);
var logger;

// Begin HTTP Zone
var writeHttpLog = (errorsMapSerial, apiPath, method, statusCode) => {
	
	logger = httpLogger;

	// Access errors map
	var errorsMap = require("../models").errorsMaps;
	// Obtain context errors
	var ctxErrors = errorsMap[method][errorsMapSerial];

	console.log();
	logger.trace("*** Che HTTP " + method + " on " + apiPath);
	logger.warn(ctxErrors[statusCode]);
};

var writeHttpErrorLog = (errorsMapSerial, message) => {

	logger = httpLogger;
	
	console.log();
	logger.error("Failed HTTP operation " + errorsMapSerial);
	logger.error(message);
};
// End HTTP

// Begin Authentication Zone
var writeAuthAccess = (originalUrl, token) => {

	logger = authLogger;

	console.log();
	logger.trace("Entity [" + token + "] accessing Url '" + originalUrl + "'");
}
// End Authentication

// Begin Database Logs Zone
var writeConnectionLog = (message, credentials) => {

	logger = dbLogger;

	console.log();
	logger.trace("Tried to create MySQL connection with credentials :");
	logger.info("-> Host : " + credentials["host"]);
	logger.info("-> User : " + credentials["user"]);
	logger.info("-> Database : " + credentials["database"]);
	logger.info("-> Password : " + credentials["password"]);
	logger.info("Message : '" + message + "'");
}

var writeQueryLog = (message, query) => {

	logger = dbLogger;
	
	console.log();
	logger.trace("Queryied : '" + query + "'");
	logger.info(message);
};
// End Database Logs

module.exports = {

	"writeHttpLog" 			: writeHttpLog,
	"writeHttpErrorLog" 	: writeHttpErrorLog,

	"writeAuthAccess" 		: writeAuthAccess,

	"writeConnectionLog" 	: writeConnectionLog,
	"writeQueryLog" 		: writeQueryLog
};