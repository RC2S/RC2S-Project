function CommonUtils() {};

CommonUtils.prototype.formatFormErrors = function(errors) {
	var errorsString;

	if (errors) {
		errorsString = '';
		
		if (!Array.isArray(errors))
			return '<div>' + JSON.stringify(errors) + '</div>';
		
		errors.forEach(function(err) {
			if (err.param && err.msg)
				errorsString += '<div>' + err.param + ' : ' + err.msg + '</div>';
			else
				errorsString += '<div>' + err + '</div>';
		});
	}

	return errorsString;
}

module.exports = new CommonUtils();