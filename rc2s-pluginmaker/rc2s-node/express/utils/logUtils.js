var log4js = require('log4js');
var logger = log4js.getLogger();

var writeHttpLog = (errorsMapSerial, apiPath, method, statusCode) => {
	
	// Access errors map
	var errorsMap = require("../models").errorsMaps;
	// Obtain context errors
	var ctxErrors = errorsMap[method][errorsMapSerial];

	console.log();
	logger.info("*** Accessing HTTP ***");
	logger.info(method + " on " + apiPath);
	logger.warn(ctxErrors[statusCode]);
};

var writeHttpErrorLog = (errorsMapSerial, message) => {

	console.log();
	logger.error("Failed HTTP operation " + errorsMapSerial);
	logger.error("Message : " + message);
};

module.exports = {

	"writeHttpLog" : writeHttpLog,
	"writeHttpErrorLog" : writeHttpErrorLog
};