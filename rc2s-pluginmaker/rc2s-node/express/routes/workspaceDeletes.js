var controllers = require("../controllers");

module.exports = (app) => {

	app.delete("/workspace/:id", (req, res, next) => {

		controllers.WorkspaceDeleteUtils.DeleteWSByID(req.params.id || "", 
			(statusCode, header, body) => {

			console.log("IN THE CALLBACK");
			console.log("END CALLBACK");
		})
	});
};