function commonAjax(url, type, data, dataType, isAsync, isForm) {
	$.ajaxSetup({
		async: isAsync
	});
	var rtnData = "";
	if (isForm) {
		$.ajax({
			url: url,
			type: type,
			data: data,
			dataType: dataType,
			processData: false,
			contentType: false,
			success: function(data) {
				rtnData = data;
			}
		});
	} else {
		$.ajax({
			url: url,
			type: type,
			data: data,
			dataType: dataType,
			success: function(data) {
				rtnData = data;
			}
		});
	}

	return rtnData;
}