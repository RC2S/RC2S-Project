var log4js = require('log4js');
var logger = log4js.getLogger();

// Begin HTTP Zone
var writeHttpLog = (errorsMapSerial, apiPath, method, statusCode) => {
	
	// Access errors map
	var errorsMap = require("../models").errorsMaps;
	// Obtain context errors
	var ctxErrors = errorsMap[method][errorsMapSerial];

	console.log();
	logger.trace("*** Che HTTP log ***");
	logger.trace(method + " on " + apiPath);
	logger.warn(ctxErrors[statusCode]);
};

var writeHttpErrorLog = (errorsMapSerial, message) => {

	console.log();
	logger.error("Failed HTTP operation " + errorsMapSerial);
	logger.error("Message : " + message);
};
// End HTTP

// Begin Authentication Zone
var writeAuthAccess = (originalUrl, token) => {

	console.log();
	logger.trace("Entity [" + token + "] accessing Url '" + originalUrl + "'");
}
// End Authentication

// Begin Database Logs Zone
var writeConnectionLog = (message, credentials) => {

	console.log();
	logger.info("Tried to create MySQL connection with credentials :");
	logger.info("-> Host : " + credentials["host"]);
	logger.info("-> User : " + credentials["user"]);
	logger.info("-> Database : " + credentials["database"]);
	logger.info("-> Password : " + credentials["password"]);
	logger.info("Message : '" + message + "'");
}

var writeQueryLog = (message, query) => {

	console.log();
	logger.info("Tried to query database :");
	logger.info("'" + query + "'");
	logger.info("Message : " + message);
};
// End Database Logs

module.exports = {

	"writeHttpLog" 			: writeHttpLog,
	"writeHttpErrorLog" 	: writeHttpErrorLog,

	"writeAuthAccess" 		: writeAuthAccess,

	"writeConnectionLog" 	: writeConnectionLog,
	"writeQueryLog" 		: writeQueryLog
};