var http 	= require('http');
var logger	= require('./logUtils');
var config	= require('./config');

module.exports = function(errorsMapSerial, path, method, data, callback) {

	if(!config.che.authMethods.includes(method))
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
		logger.writeHttpLog(errorsMapSerial, path, 
			method, res.statusCode);

	 	res.setEncoding('utf8');

	 	res.on('data', function(chunk) {
	  		content += chunk;
	 	});
	 	
		res.on('end', function() {
			callback(JSON.parse(content));
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