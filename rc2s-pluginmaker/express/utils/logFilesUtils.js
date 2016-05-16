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

module.exports = {

	"HttpLogger" : httpLogger,

	"AuthLogger" : authLogger,

	"DBLogger" : dbLogger
}