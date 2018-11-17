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
				'<footer class="fixed-bottom">'
				+ '<!-- ボタンの処理を変更する -->'
				+ '<a class="btn btn-secondary float-right" href="post.html" role="button">'
				+ buttonName
				+ '</a>'
				+ '</footer>');
	}
}

/*
 * ボタン処理がいらない場合は，第2引数を書かない
 * 例：initTemplate("みんなの投稿");
 */
$(document).ready(function(){
	initTemplate("タイトルテンプレート", "右下ボタンテンプレート");
});