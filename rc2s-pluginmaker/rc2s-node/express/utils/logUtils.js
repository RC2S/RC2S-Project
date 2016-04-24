var log4js = require('log4js');
var logger = log4js.getLogger();

var writeHttpLog = (errorsMapSerial, apiPath, method, statusCode) => {
	
	var errorsMap = require("../models").errorsMaps[errorsMapSerial];

	console.log();
	logger.info("*** Accessing HTTP ***");
	logger.info(method + " on " + apiPath);
	logger.warn(errorsMap[statusCode] + "\n");
	console.log();
};

var writeHttpErrorLog = (errorsMapSerial, message) => {

	console.log();
	logger.error("Failed HTTP operation " + errorsMapSerial);
	logger.error("Message : " + message);
	console.log();
};

module.exports = {

	"writeHttpLog" : writeHttpLog,
	"writeHttpErrorLog" : writeHttpErrorLog
};