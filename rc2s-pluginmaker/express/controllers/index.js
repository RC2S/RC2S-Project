var WorkspaceGetUtils 		= require("./WorkspaceGetUtils");
var WorkspacePostUtils 		= require("./WorkspacePostUtils");
var WorkspacePutUtils 		= require("./WorkspacePutUtils");
var WorkspaceDeleteUtils 	= require("./WorkspaceDeleteUtils");

module.exports = {
	"WorkspaceGetUtils" 	: new WorkspaceGetUtils(),
	"WorkspacePostUtils" 	: new WorkspacePostUtils(),
	"WorkspacePutUtils" 	: new WorkspacePutUtils(),
	"WorkspaceDeleteUtils" 	: new WorkspaceDeleteUtils()
};