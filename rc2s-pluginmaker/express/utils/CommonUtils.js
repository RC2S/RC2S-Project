function CommonUtils() {};

CommonUtils.prototype.formatFormErrors = function(errors) {
	var errorsString;

	if (errors) {
		errorsString = '';
		
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