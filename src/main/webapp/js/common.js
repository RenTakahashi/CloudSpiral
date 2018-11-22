var initTemplate = function(pageTitle, buttonName, nextPage) {
	//headerとfooterの準備
	$("body").prepend(
			'<header class="fixed-top">'
			+ '<nav aria-label="breadcrumb">'
			+ '<ol class="breadcrumb">'
			+ '<li class="breadcrumb-item active" aria-current="page">'
			+ '<a href="index.html" style="color: gray; text-decoration:none">Memoworld</a>'
			+ '</li>'
			+ '<li class="breadcrumb-item active" aria-current="page">'
			+ pageTitle
			+ '</li>'
			+ '</ol>'
			+ '</nav>'
			+ '</header>');
	if (buttonName != undefined){
		$("body").append(
				'<footer class="fixed-bottom mb-3">'
				+ '<!-- ボタンの処理を変更する -->'
				+ '<a class="btn btn-primary float-right mx-3" href="' + nextPage + '" role="button">'
				+ buttonName
				+ '</a>'
				+ '</footer>');
	}
}
