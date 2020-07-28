function commonAjax(url, type, data, dataType, isAsync) {
	$.ajaxSetup({
		async: isAsync
	});
	var rtnData = "";
	$.ajax({
		url: url,
		type: type,
		data: data,
		dataType: dataType,
		success: function(data) {
			rtnData = data;
		}
	});
	return rtnData;
}