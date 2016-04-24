var controllers = require("../controllers");

module.exports = (app) => {

	app.delete("/workspace/:id", (req, res, next) => {

		controllers.WorkspaceDeleteUtils.DeleteWSByID(req.params.id || "", 
			(statusCode, header, body) => {

			console.log("IN THE CALLBACK");
			console.log("END CALLBACK");
		})
	});

	app.delete("/workspace/:id/project/:name", (req, res, next) => {

		controllers.WorkspaceDeleteUtils.RemoveProjectFromWS(
			req.params.id || "",
			req.params.name || "",
			(statusCode, header, body) => {

			console.log("IN THE CALLBACK");
			console.log("END CALLBACK");
		});
	});

	app.delete("/workspace/:id/runtime", (req, res, next) => {

		controllers.WorkspaceDeleteUtils.StopWorkspace(req.params.id || "",
			(statuscode, header, body) => {

			console.log("IN THE CALLBACK");
			console.log("END CALLBACK");
		});
	});
};