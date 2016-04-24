var log4js = require('log4js');
var logger = log4js.getLogger();

var writeHttpLog = (errorsMapSerial, apiPath, method, statusCode) => {
	
	var errorsMap = require("../models").errorsMaps[errorsMapSerial];

	logger.info("*** Accessing HTTP");
	logger.info(method + " on " + apiPath);
	logger.warn(errorsMap[statusCode] + "\n");
};

module.exports = {

	"writeHttpLog" : writeHttpLog
};