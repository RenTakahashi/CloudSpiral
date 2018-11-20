var initTemplate = function(pageTitle, buttonName) {
	//headerとfooterの準備
	$("body").prepend(
			'<header class="fixed-top">'
			+ '<nav aria-label="breadcrumb">'
			+ '<ol class="breadcrumb">'
			+ '<li class="breadcrumb-item active" aria-current="page">Memoworld</li>'
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
				+ '<a class="btn btn-secondary float-right mx-3" href="post.html" role="button">'
				+ buttonName
				+ '</a>'
				+ '</footer>');
	}
}