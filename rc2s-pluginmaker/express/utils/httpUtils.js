var http 	= require("http");
var logger	= require("./logUtils");

var buildRequestFromParams = function(errorsMapSerial, apiPath, method, data, callback) {

	if(!config.api.authMethods.includes(method))
		return;

	var options = {
		host 	: config.che.host,
		port 	: config.che.port,
		path 	: config.che.mainPath + path,
		method 	: method,
		headers : {
			'Content-Type' : config.che.contentType
		}
	}

	if(data != undefined)
		options.headers['Content-Length'] = Buffer.byteLength(data);

	var req = http.request(options, function(res) {
		var content = '';

		// Write basic log
		logger.writeHttpLog(errorsMapSerial, apiPath, 
			method, res.statusCode);

	 	res.setEncoding('utf8');

	 	res.on('data', function(chunk) {
	  		content += chunk;
	 	});
	 	
		res.on('end', function() {
			callback(res.statusCode, res.headers, content);
		});
	});

	if(data != undefined)
		request.write(JSON.stringify(data));

	req.on('error', function(e) {
		// Write error log
  		logger.writeHttpErrorLog(errorsMapSerial, e.message);
	});

	req.end();
}

module.exports = {
	buildRequestFromParams : buildRequestFromParams
};