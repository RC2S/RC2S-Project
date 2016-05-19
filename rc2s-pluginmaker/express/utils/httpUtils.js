var http = require("http");

var logger = require("./logUtils");

var cheHost = '192.168.1.108';
var chePort = 8080;

var buildRequestFromParams = (errorsMapSerial, apiPath, method, callback) => {

	var options = createOptions(apiPath, method);

	var content;

	var req = http.request(options, (res) => {

		// Write basic log
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

		// Write error log
  		logger.writeHttpErrorLog(errorsMapSerial, e.message);
	});

	req.end();
}

var createOptions = (path, method) => {
	
	var options = {
		host: cheHost,
		port: chePort,
		path: path,
		method: method
	};

	return options;
};

module.exports = {
	
	"buildRequestFromParams" : buildRequestFromParams,
	"createOptions" : createOptions
};