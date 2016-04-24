var http = require("http");

var createOptions = require("../utils/httpUtils").createOptions;

var WorkspaceGetUtils = function() {};

WorkspaceGetUtils.prototype.FindAll = (callback) => {
	var apiPath = '/api/workspace?skipCount=0&maxItems=30';

	var options = createOptions(apiPath, 'GET');

	var content;

	console.log("[~] Workspace GetUtils retrieving...");

	var req = http.request(options, (res) => {

	 	console.log('STATUS  : ' + res.statusCode);
	 	console.log('HEADERS : ' + res.headers);

	 	res.setEncoding('utf8');

	 	res.on('data', (chunk) => {
	  		console.log('BODY    : ' + chunk);
	  		content = chunk;
	 	});
		res.on('end', () => {
			console.log("[~] Workspace GetUtils ending...");
			callback(res.headers, content);
		});
	});

	req.on('error', (e) => {
  		console.log('Problem with request: ' + e.message);
	});

	req.end();
};

module.exports = WorkspaceGetUtils;