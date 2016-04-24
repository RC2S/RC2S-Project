module.exports = {
	"GET" : {
		"GETFA1" : {
			200 : 'The workspace successfully fetched',
			500 : 'Internal server error occurred during workspaces fetching'
		},
		"GETFBN1" : {
			200 : 'The response contains requested workspace entity',
			403 : 'The user is not the workspace owner',
			404 : 'The workspace with specified name does not exist for current user',
			500 : 'Internal server error occurred'
		},
		"GETFBID1" : {
			200 : 'The response contains requested workspace entity',
			403 : 'The user is not workspace owner',
			404 : 'The workspace with specified id does not exist',
			500 : 'Internal server error occurred'
		},
		"GETFAR1" : {
			200 : 'Workspaces successfully fetched'
		},
		"GETFRBID1" : {
			200 : 'The response contains requested runtime workspace entity',
			403 : 'The user is not workspace owner',
			404 : 'Workspace is not running',
			500 : 'Internal server error occurred'
		}
	},
	"POST" : {

	},
	"PUT" : {

	},
	"DELETE" : {
		"DELWSBID1" : {
			204 : 'The workspace successfully removed',
			403 : 'The user does not have access to remove the workspace',
			404 : "The workspace doesn't exist",
			409 : 'The workspace is not stopped(has runtime)',
			500 : 'Internal server error occurred'
		},
		"DELRPFWS" : {
			204 : 'The project successfully removed',
			403 : 'The user does not have access remove the project',
			404 : 'The workspace not found',
			500 : 'Internal server error occurred'
		}, 
		"DELSWS1" : {
			204 : 'The workspace is stopping',
			403 : 'The user is not workspace owner',
			404 : "The workspace with specified id doesn't exist",
			500 : 'Internal server error occurred'
		}
	}
};