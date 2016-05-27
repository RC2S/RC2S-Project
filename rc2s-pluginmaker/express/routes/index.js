module.exports = function(app) {
	require('./UserRoute')(app);
	require('./PluginsRoute')(app);
};