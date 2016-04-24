var controllers = require("../controllers");

module.exports = (app) => {

	app.post("/workspace/runtime/:id", (req, res, next) => {

		controllers.WorkspacePostUtils.StartWorkspace(req.params.id || "",
			(statusCode, header, body) => {

			console.log("CALLBACK");
		});
	});

	app.post("/workspace/runtime/name/:name", (req, res, next) => {

		controllers.WorkspacePostUtils.StartWorkspaceByName(req.params.name || "",
			(statusCode, header, body) => {

			console.log("CALLBACK");
		});
	});

	app.post("/workspace/snapshot/:id", (req, res, next) => {

		controllers.WorkspacePostUtils.CreateSnapshot(req.params.id || "",
			(statusCode, header, body) => {

			console.log("CALLBACK");
		});
	});
};