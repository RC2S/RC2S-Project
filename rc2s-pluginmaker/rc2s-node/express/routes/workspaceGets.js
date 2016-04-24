var controllers = require("../controllers");

module.exports = (app) => {

	app.get("/workspaces", (req, res, next) => {

		controllers.WorkspaceGetUtils.FindAll((statusCode, header, body) => {

			console.log("IN THE CALLBACK");

			//console.log("CB status : " + statusCode);
			//console.log("CB header : " + header);
			//console.log("CB body   : " + body);
		
			console.log("END CALLBACK");
		});
	});

	app.get("/workspace/name/:name", (req, res, next) => {

		controllers.WorkspaceGetUtils.FindByName(req.params.name || "", 
			(statusCode, header, body) => {

			console.log("IN THE CALLBACK");
			console.log("END CALLBACK");
		});
	});

	app.get("/workspace/:id", (req, res, next) => {

		controllers.WorkspaceGetUtils.FindByID(req.params.id || "",
			(statusCode, header, body) => {

			console.log("IN THE CALLBACK");
			console.log("END CALLBACK");
		});
	});

	app.get("/workspaces/runtime", (req, res, next) => {

		controllers.WorkspaceGetUtils.FindAllRuntime(
			(statusCode, header, body) => {

			console.log("IN THE CALLBACK");
			console.log("END CALLBACK");
		});
	});

	app.get("/workspace/runtime/:id", (req, res, next) => {

		controllers.WorkspaceGetUtils.FindRuntimeByID(req.params.id || "",
			(statusCode, header, body) => {

			console.log("IN THE CALLBACK");
			console.log("END CALLBACK");
		});
	});
};