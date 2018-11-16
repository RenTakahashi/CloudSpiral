//var endpoint = '/memo';

/*ページを読み込んだ時の初期化処理
 * APIを叩いてjsonを読み込む*/
var initToppage = function() {
	$.ajax({
		//type: 'GET',
		url: 'js/photo.json',
		//url: endpoint + '/travelogues',
		success: function(json) {
			for( var i=0; i<json.length; i++){
				var travelogue = json[i];
				addTravelogue(
						travelogue.title,
						travelogue.date,
						travelogue.author,
						travelogue.photos[0]);
				}}
	});
}
/*ページを読み込んだ時の初期化処理
 * htmlに旅行記を表示させる*/
var addTravelogue = function(title, date, author, photoID) {
	$('#cards')
	.append('<div class="card">'
			+ '<a href="travelogue.html">'
			+ '<img src="' + photoID + '" class="img-thumbnail">'
			+ '<div class="card-body">'
			+ '<h5 class="card-title">' + title + '</5>'
			+ '<h6 class="card-subtitle">' + date + '</5>'
			+ '<p class="card-text">by ' + author + '</p>'
			+ '</a>'
			+ '<img id="like-btn" class="card-thumbnail float-right" src="http://133.1.236.184:8000/jenkins/static/f9132b26/images/headshot.png">'
			+ '</div>'
			+ '</div>');
}
/*初期化処理*/
$(document).ready(function(){
	initToppage();
});