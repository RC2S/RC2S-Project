var createOptions = (path, method) => {
	
	var options = {
		host: '192.168.209.128',
		port: 8080,
		path: path,
		method: method
	};

	return options;
};

module.exports = {
	"createOptions": createOptions
}