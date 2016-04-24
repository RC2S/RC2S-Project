require("./express")();








/*
var http = require("http");

var get = '/api/workspace?skipCount=0&maxItems=30';
var getName = '/api/workspace/name/wksp-test-java-1';

var options = {
  host: '192.168.209.128',
  port: 8080,
  path: getName,
  method: 'GET'
};

var result = {}

var req = http.request(options, (res) => {
  console.log('STATUS  : ' + res.statusCode);
  var headerContent = res.headers;
  console.log('HEADERS : ');
  for (datas in headerContent)
  	console.log(datas + ' : ' + headerContent[datas]);

  res.setEncoding('utf8');

  res.on('data', (chunk) => {
  	console.log('BODY    : ');
  	var content = JSON.parse(chunk);
  	for(datas in content) {
  		console.log(datas + ' : ' + content[datas]);
  		result[datas] = content[datas];
  	}
  });
  res.on('end', () => {
    console.log('No more data in response.')
    for (datas in result)
    	console.log(datas + ' : ' + result[datas]);
  });
});

req.on('error', (e) => {
  console.log('Problem with request: ' + e.message);
});

req.end();
*/

