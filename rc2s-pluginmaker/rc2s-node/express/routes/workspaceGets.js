var controllers = require("../controllers");

module.exports = (app) => {

	app.get("/workspace", (req, res, next) => {

		controllers.WorkspaceGetUtils.FindAll((statusCode, header, body) => {

			console.log("\n\nIN THE CALLBACK");

			console.log("CB status : " + statusCode);
			console.log("CB header : " + header);
			console.log("CB body   : " + body);
/*
			for (datas in header)
	  			console.log(datas + ' : ' + header[datas]);

			for(datas in body)
	  			console.log(datas + ' : ' + body[datas]);

*/			console.log("END CALLBACK");
		});
	});

	app.get("/workspace/:name", (req, res, next) => {

		controllers.WorkspaceGetUtils.FindByName(req.params.name || "", 
			(statusCode, header, body) => {

			console.log("\n\nIN THE CALLBACK");

			console.log("CB status : " + statusCode);
			console.log("CB header : " + header);
			console.log("CB body   : " + body);
/*
			for (datas in header)
	  			console.log(datas + ' : ' + header[datas]);

			for(datas in body)
	  			console.log(datas + ' : ' + body[datas]);

*/			console.log("END CALLBACK");
		});
	});
};