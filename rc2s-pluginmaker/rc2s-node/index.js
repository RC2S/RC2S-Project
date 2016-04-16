var express = require("express");

var app = express();

var bodyParser = require("body-parser");

app.use(bodyParser.json());

require("./loggers")(app);
require("./routes")(app);

app.listen(8080, () => {
	console.log("Server running on port 8080...");
});