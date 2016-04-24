module.exports = (app) => {
	
	require("./workspaceGets")(app);
	require("./workspacePosts")(app);
	require("./workspacePuts")(app);
	require("./workspaceDeletes")(app);
};