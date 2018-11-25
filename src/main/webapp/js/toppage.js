var endpoint = "./api/";
/*
 * 旅行記リストを作成する
 * */
var createTravelogueList = function(travelogue) {
	var photo = getPhoto(travelogue.photos[0]);
	var cardWidth = $("#travelogue-lists").width() * 3/4;
	console.log(cardWidth);
	$("#travelogue-lists").append(
			'<a href="card.html?' + travelogue.id + '" style="color: gray; text-decoration:none">'
			+ '<div class="card my-1">'
			+ '<img class="center-block" style="width: 100%; height:' + cardWidth + 'px; object-fit: contain;" src="' + photo + '">'
			+ '<div class="card-body card-img-overlay">'
			+ '<div class="text-monospace" style="background: rgba(255,255,255,0.6);">'
			+ '<small class="text-muted">' + travelogue.date + '</small>'
			+ '<h5 class="card-title">' + travelogue.title.substr(0,10) + '</h5>'
			+ '<p class="text-right" style="font-size: 0.6rem">by ' + travelogue.author + '</p>'
			+ '</div></div>'
			+ '</div></a>'
			);
}

var createTravelogueLists = function() {
	$.ajax({
		type: 'GET',
		url: endpoint + 'travelogues',
		contentType: 'image/json',
		success: function(json) {
			for(var i=0; i < json.travelogues.length; i++){
				createTravelogueList(json.travelogues[i]);
			}
		}
	});
}

/*
 * 旅行記の写真を取得する
 * */
var getPhoto = function(photos) {
	return 'data:image/jpeg;base64,' + photos.rawImage;
}

var changeFooterURL = function(){
	if(window.sessionStorage.getItem(['access_token']) != null){
		return "post.html";
	}else{
		return "login.html";
	}
}

$(document).ready(function(){
	createTravelogueLists();
});
