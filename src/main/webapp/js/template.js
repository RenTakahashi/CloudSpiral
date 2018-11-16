var initTemplate = function(pageTitle, buttonName) {
	$("#header").append(
			'<header class="sticky-top mx-auto" style="width: 400px;">'
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
		$("#footer").append(
				'<footer class="fixed-bottom mx-auto" style="width: 400px;">'
				+ '<!-- ボタンの処理を変更する -->'
				+ '<a class="btn btn-secondary float-right" href="post.html" role="button">'
				+ buttonName
				+ '</a>'
				+ '</footer>');
	}
	
}

$(document).ready(function(){
	initTemplate("みんなの投稿", "旅行記投稿");
});