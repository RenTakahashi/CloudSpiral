var endpoint = "./api/";
/*
 * 旅行記リストを作成する
 * */
var createTravelogueList = function(travelogue) {
	//var photo = travelogue.photo;
	//var photo = getPhoto(travelogue.photos[0]);
	var photo = getPhoto(1);
	console.log(photo);
	$("#travelogue-lists").append(
			'<a href="card.html" style="color: gray; text-decoration:none">'
			+ '<div class="card"><div class="row"><div class="col d-flex">'
			+ '<div class="col-md-6">'
			+ '<img class="img-keep-ratio my-4" height="250" src="'+ photo + '">'
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
		url: endpoint + '/travelogues',
		success: function(json) {
			for(var i=0; json.length; i++){
				createTravelogueList(json[i]);
			}
		}
	});
}

/*
 * 旅行記の写真を取得する
 * */
var getPhoto = function(photoID) {
	return $.ajax({
		type: 'GET',
		url: endpoint + 'photos/' + photoID + '/raw',
		contentType: 'image/jpeg',
		success: function(json) {
			return json.rawImage;
		}
	});
}

var travelogues = [{
	  "id": null,
	  "title":"イリヤかわいいあいうえおかきくけこ",
	  "date": "2018-09-18",
	  "author": "itimon",
	  "photos_id": [1,2],
	  "photo": "http://tn.smilevideo.jp/smile?i=24770287.L",
	  "description": "あいうえおかきくけこさしすせそ"
	},{
	  "id": null,
	  "title":"イリヤかわいい2",
	  "date": "2018-09-18",
	  "author": "itimon",
	  "photos_id": [1,2],
	  "photo": "http://tn.smilevideo.jp/smile?i=24770287.L",
	  "description": "あいうえおかきくけこさしすせそ"
	}];

$(document).ready(function(){
	initTemplate("みんなの投稿", "投稿", "post.html");
	for(var i=0; i < travelogues.length; i++){
		console.log(i);
		createTravelogueList(travelogues[i]);
	}
});
