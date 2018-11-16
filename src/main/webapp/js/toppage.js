var endpoint = '/memo'
var initToppage = function() {
	$.ajax({
		//type: 'GET',
		url: 'js/photo.json',
		//url: endpoint + '/travelogues',
		success: function(json) {
			//console.log(json);
			addTravelogue(json.title, json.date, json.author, json.photos[0]);
		}
	});
}

var addTravelogue = function(title, date, author, photoID) {
	$('#cards')
	.append('<img src="' + photoID + '" class="rounded float-left" class="img-thumbnail">'
			+ '<div class="card">'
			+ '<div class="card-body">'
			+ '<h5 class="card-title">' + title + '</5>'
			+ '<h6 class="card-subtitle">' + date + '</5>'
			+ '<p class="card-text">by' + author + '</p>'
			+ '</div>'
			+ '</div>');

}

$(document).ready(function(){
	initToppage();
	//addTravelogue("aaaa", "2018/9/18", "itimon","https://www.sria.co.jp/blog/wp-content/uploads/2014/07/javascript.png");
});