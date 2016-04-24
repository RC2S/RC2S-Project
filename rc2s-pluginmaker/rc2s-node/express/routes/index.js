module.exports = (app) => {
	
	require("./login")(app);

	require("./workspaceGets")(app);
	require("./workspacePosts")(app);
	require("./workspacePuts")(app);
	require("./workspaceDeletes")(app);
};