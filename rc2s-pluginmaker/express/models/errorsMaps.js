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
		},
		"GETFSBID1" : {
			200 : 'Snapshots successfully fetched',
			403 : 'The user is not workspace owner',
			404 : "The workspace with specified id doesn't exist or the snapshot doesn't exist for the workspace",
			500 : 'Internal server error occurred'
		}
	},
	"POST" : {
		"POSTSWS1" : {
			200 : 'The workspace is starting',
			403 : 'The user is not workspace owner or the operation is not allowed for the user',
			404 : "The workspace with specified id doesn't exist",
			409 : 'Any conflict occurs during the workspace start',
			500 : 'Internal server error occurred'
		},
		"POSTSWSBN1" : {
			200 : 'The workspace is starting',
			400 : 'The workspace name is not valid',
			403 : 'The user is not workspace owner or the operation is not allowed for the user',
			404 : "The workspace with specified id doesn't exist",
			409 : 'Any conflict occurs during the workspace start',
			500 : 'Internal server error occurred'
		},
		"POSTCS1" : {
			200 : 'The snapshot successfully created',
			403 : 'The user is not workspace owner or the operation is not allowed for the user',
			404 : "The workspace with specified id doesn't exist.",
			409 : 'Any conflict occurs during the snapshot creation',
			500 : 'Internal server error occurred'
		},
		"POSTAPFWS" : {
			200 : 'The project successfully created',
			403 : 'The user is not workspace owner or the operation is not allowed for the user',
			404 : "The workspace with specified id doesn't exist.",
			409 : 'Any conflict occurs during the project creation',
			500 : 'Internal server error occurred'
		},
		"POSTAFTP" : {
			200 : 'The folder successfully created',
			403 : 'The user is not workspace owner or the operation is not allowed for the user',
			404 : "The workspace with specified id doesn't exist.",
			409 : 'Any conflict occurs during the folder creation',
			500 : 'Internal server error occurred'
		},
		"POSTAFITP" : {
			200 : 'The file successfully created',
			403 : 'The user is not workspace owner or the operation is not allowed for the user',
			404 : "The workspace with specified id doesn't exist.",
			409 : 'Any conflict occurs during the file creation',
			500 : 'Internal server error occurred'
		}
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