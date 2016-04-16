var fs = require("fs");

var writeData = function(datas, log) {
	datas += JSON.stringify(log) + "\n";

	fs.writeFile("logs.json", datas, (err) => {
		if (err)
			throw err;

		console.log("[+] Wrote log !");
	});
}

module.exports = {
	"WriteData" : writeData
};