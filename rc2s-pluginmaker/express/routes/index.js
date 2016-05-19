module.exports = function(app) {
	
	require('./UserRoute')(app);
	require('./PluginsRoute')(app);

	require('./workspaceGets')(app);
	require('./workspacePosts')(app);
	require('./workspacePuts')(app);
	require('./workspaceDeletes')(app);
};