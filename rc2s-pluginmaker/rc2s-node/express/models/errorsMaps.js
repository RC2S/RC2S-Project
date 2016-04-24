module.exports = {
	"GETFA1" : {
		200 : 'The workspace successfully fetched',
		500 : 'Internal server error occurred during workspaces fetching'
	},
	"GETFBN1" : {
		200 : 'The response contains requested workspace entity',
		403 : 'The user is not the workspace owner',
		404 : 'The workspace with specified name does not exist for current user',
		500 : 'Internal server error occurred'
	}
};