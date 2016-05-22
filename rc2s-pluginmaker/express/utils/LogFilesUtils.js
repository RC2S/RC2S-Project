var log4js = require('log4js');
var config = require('./config');

log4js.loadAppender('file');
var httpLogsPath 	= config.log4js.dirname + config.log4js.httpLogName;
var authLogsPath 	= config.log4js.dirname + config.log4js.authLogName;
var dbLogsPath 		= config.log4js.dirname + config.log4js.dbLogName;

log4js.addAppender(log4js.appenders.file(httpLogsPath), config.log4js.httpLogName);
log4js.addAppender(log4js.appenders.file(authLogsPath), config.log4js.authLogName);
log4js.addAppender(log4js.appenders.file(dbLogsPath), config.log4js.dbLogName);

module.exports = {
	HttpLogger : log4js.getLogger(config.log4js.httpLogName),
	AuthLogger : log4js.getLogger(config.log4js.authLogName),
	DBLogger   : log4js.getLogger(config.log4js.dbLogName)
}