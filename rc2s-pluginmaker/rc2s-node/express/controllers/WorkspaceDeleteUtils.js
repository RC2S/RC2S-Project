var http = require("http");

var method = 'DELETE';

var logger = require("../utils/logUtils");

var createOptions = require("../utils/httpUtils").createOptions;

var WorkspaceDeleteUtils = function() {};

WorkspaceDeleteUtils.prototype.DeleteWSByID = (wsID, callback) => {

	var errorsMapSerial = 'DELWSBID1';

	var apiPath = '/api/workspace/' + wsID;

	var options = createOptions(apiPath, method);

	var content;

	var req = http.request(options, (res) => {

		// Errors to manage
		logger.writeHttpLog(errorsMapSerial, apiPath, 
			method, res.statusCode);

	 	res.setEncoding('utf8');

	 	res.on('data', (chunk) => {
	  		content = chunk;
	 	});
		res.on('end', () => {
			callback(res.statusCode, res.headers, content);
		});
	});

	req.on('error', (e) => {
  		logger.writeHttpErrorLog(errorsMapSerial, e.message);
	});

	req.end();
};

WorkspaceDeleteUtils.prototype.RemoveProjectFromWS = (wsID, pjName, callback) => {

	var errorsMapSerial = 'DELRPFWS';

	var apiPath = '/api/workspace/' + wsID + '/project/' + pjName;

	var options = createOptions(apiPath, method);

	var content;

	var req = http.request(options, (res) => {

		// Errors to manage
		logger.writeHttpLog(errorsMapSerial, apiPath, 
			method, res.statusCode);

	 	res.setEncoding('utf8');

	 	res.on('data', (chunk) => {
	  		content = chunk;
	 	});
		res.on('end', () => {
			callback(res.statusCode, res.headers, content);
		});
	});

	req.on('error', (e) => {
  		logger.writeHttpErrorLog(errorsMapSerial, e.message);
	});

	req.end();
};

WorkspaceDeleteUtils.prototype.StopWorkspace = (wsID, callback) => {

	var errorsMapSerial = 'DELSWS1';

	var apiPath = '/api/workspace/' + wsID + '/runtime';

	var options = createOptions(apiPath, method);

	var content;

	var req = http.request(options, (res) => {

		// Errors to manage
		logger.writeHttpLog(errorsMapSerial, apiPath, 
			method, res.statusCode);

	 	res.setEncoding('utf8');

	 	res.on('data', (chunk) => {
	  		content = chunk;
	 	});
		res.on('end', () => {
			callback(res.statusCode, res.headers, content);
		});
	});

	req.on('error', (e) => {
  		logger.writeHttpErrorLog(errorsMapSerial, e.message);
	});

	req.end();
};

module.exports = WorkspaceDeleteUtils;