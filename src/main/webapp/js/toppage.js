var endpoint = "./api/";
/*
 * 旅行記リストを作成する
 * */
var createTravelogueList = function(travelogue) {
	var photo = getPhoto(travelogue.photos[0]);
	$("#travelogue-lists").append(
			'<a href="card.html?' + travelogue.id + '" style="color: gray; text-decoration:none">'
			+ '<div class="card"><div class="row"><div class="col d-flex">'
			+ '<div class="col-md-6">'
			+ '<img id="traveloguePhoto" class="img-keep-ratio my-4" height="250" src="' + photo + '">'
			+ '</div>'
			+ '<div class="card-body col-md-6">'
			+ '<small class="text-muted">' + travelogue.date + '</small>'
			+ '<h5 class="card-title">' + travelogue.title.substr(0,10) + '</h5>'
			+ '<p class="float-right" style="font-size: 0.6rem">by ' + travelogue.author + '</p>'
			+ '</div></div></div></div></a>'
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
