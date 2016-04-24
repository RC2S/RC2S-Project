var cheHost = '192.168.209.128';
var chePost = 8080;

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
	"createOptions": createOptions
}