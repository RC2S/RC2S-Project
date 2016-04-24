var http = require("http");

var method = 'GET';

var createOptions = require("../utils/httpUtils").createOptions;

var writeHttpLog = require("../utils/logUtils").writeHttpLog;

var WorkspaceGetUtils = function() {};

WorkspaceGetUtils.prototype.FindAll = (callback) => {

	var errorsMapSerial = 'GETFA1';

	var apiPath = '/api/workspace?skipCount=0&maxItems=30';

	var options = createOptions(apiPath, method);

	var content;

	var req = http.request(options, (res) => {

		// Errors to manage
		writeHttpLog(errorsMapSerial, apiPath, method, res.statusCode);

		res.setEncoding('utf8');

	 	res.on('data', (chunk) => {
	  		content = chunk;
	 	});
		res.on('end', () => {
			callback(res.statusCode, res.headers, content);
		});
	});

	req.on('error', (e) => {
  		console.log('Problem with request: ' + e.message);
	});

	req.end();
};

WorkspaceGetUtils.prototype.FindByName = (wsName, callback) => {

	var errorsMapSerial = 'GETFBN1';

	var apiPath = '/api/workspace/name/' + wsName;

	console.log("FindByName apiPath : " + apiPath);

	var options = createOptions(apiPath, 'GET');

	var content;

	var req = http.request(options, (res) => {

		// Errors to manage
		writeHttpLog(errorsMapSerial, apiPath, method, res.statusCode);

	 	res.setEncoding('utf8');

	 	res.on('data', (chunk) => {
	  		content = chunk;
	 	});
		res.on('end', () => {
			callback(res.statusCode, res.headers, content);
		});
	});

	req.on('error', (e) => {
  		console.log('Problem with request: ' + e.message);
	});

	req.end();
};

module.exports = WorkspaceGetUtils;