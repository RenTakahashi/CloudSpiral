var endpoint = "./api/";
/*
 * 旅行記リストを作成する
 * */
var createTravelogueList = function(travelogue) {
	var cardWidth = $("#travelogue-lists").width() * 3/4;
	$("#travelogue-lists").append(
			'<a class="card my-1" href="card.html?' + travelogue.id + '" style="">'
			+ '<img class="center-block" style="height:' + cardWidth + 'px;" src="' + getPhoto(travelogue.photos[0]) + '">'
			+ '<div class="card-body card-img-overlay text-monospace">'
			+ '<div id="card-texts">'
			+ '<small class="text-muted">' + getDate(travelogue.date) + '</small>'
			+ '<h5 class="card-title">' + travelogue.title.substr(0,10) + '</h5>'
			+ '<p class="text-right" style="font-size: 0.6rem">by ' + travelogue.author + '</p>'
			+ '</div></div>'
			+ '</a>'
			);

	$(".progress").remove();
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

var getDate = function(date) {
	var formatDate = new Date(date)
	var y = formatDate.getFullYear();
	var m = formatDate.getMonth() + 1;
	var d = formatDate.getDate();
	return y + '/' + m + '/' + d; 
}

var changeFooterURL = function(){
	if(window.sessionStorage.getItem(['access_token']) != null){
		return "post.html";
	}else{
		return "login.html";
	}
}

createTravelogueLists();

