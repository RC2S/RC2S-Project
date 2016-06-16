var loggersList = require('./LogFilesUtils');
var logger;

// Begin HTTP Zone
var writeHttpLog = function(errorsMapSerial, apiPath, method, statusCode) {
	
	logger = loggersList.HttpLogger;

	// Access errors map
	var errorsMap = require("../models").errorsMaps;
	// Obtain context errors
	var ctxErrors = errorsMap[method][errorsMapSerial];

	console.log();
	logger.trace("*** Che HTTP " + method + " on " + apiPath);
	logger.warn(ctxErrors[statusCode]);
};

var writeHttpErrorLog = function(errorsMapSerial, message) {

	logger = loggersList.HttpLogger;
	
	console.log();
	logger.error("Failed HTTP operation " + errorsMapSerial);
	logger.error(message);
};
// End HTTP

// Begin Authentication Zone
var writeAuthAccess = function(originalUrl, token) {

	logger = loggersList.AuthLogger;

	console.log();
	logger.trace("Entity [" + token + "] accessing Url '" + originalUrl + "'");
}
// End Authentication

// Begin Database Logs Zone
var writeConnectionLog = function(message, credentials) {

	logger = loggersList.DBLogger;

	console.log();
	logger.trace("Tried to create MySQL connection with credentials :");
	logger.info("-> Host : " + credentials["host"]);
	logger.info("-> User : " + credentials["user"]);
	logger.info("-> Database : " + credentials["database"]);
	logger.info("-> Password : " + credentials["password"]);
	logger.info("Message : '" + message + "'");
}

var writeQueryLog = function(message, query) {

	logger = loggersList.DBLogger;
	
	console.log();
	logger.trace("Queryied : '" + query + "'");
	logger.info(message);
};
// End Database Logs

module.exports = {
	writeHttpLog 		: writeHttpLog,
	writeHttpErrorLog 	: writeHttpErrorLog,
	writeAuthAccess 	: writeAuthAccess,
	writeConnectionLog 	: writeConnectionLog,
	writeQueryLog 		: writeQueryLog
};