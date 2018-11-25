var initTemplate = function(pageTitle, buttonName, nextPage, callback) {
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
		let button = $('<button class="btn btn-primary float-right mx-3" role="button">'
				+ buttonName
				+ '</button>');
		button.click(() => {
			if (typeof callback !== 'function') {
				callback = (resolve, reject) => { resolve(nextPage); };
			}
			new Promise(callback)
				.then((nextPage) => { location.href = nextPage; })
				.catch(() => {});   // エラー時は遷移しない．エラー処理は callback 側の .catch() で行い，最後に reject(); すること
		});
		$('<footer class="fixed-bottom mb-3">')
				.append(button)
				.appendTo($('body'));
	}
}
