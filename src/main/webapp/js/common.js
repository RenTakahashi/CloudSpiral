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
                callback = (resolve, reject) => {};
            }
            new Promise(callback)
                .then(() => { location.href = nextPage; });
        });
        $('<footer class="fixed-bottom mb-3">')
                .append(button)
                .appendTo($('body'));
	}
}
