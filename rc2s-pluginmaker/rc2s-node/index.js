//require("./express")();

var http = require("http");

var options = {
  host: '192.168.209.128',
  port: 8080,
  path: '/api/workspace?skipCount=0&maxItems=30'
};

http.get(options, function(res) {
  console.log('STATUS : ' + res.statusCode);
  console.log('HEADERS: ' + JSON.stringify(res.headers));
  console.log('BODY   : ' + res.body);
}).on('error', function(e) {
  console.log("Got error: " + e.message);
});