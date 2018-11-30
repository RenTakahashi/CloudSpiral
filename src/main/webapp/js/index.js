var endpoint = "./api/";

//旅行記リストを作成する
var createTravelogueList = function(travelogue) {
	var cardWidth = $("#travelogue-lists").width() * 3/4;//カードの高さを画面サイズに合わせる
	//旅行記リスト作成
	$("#travelogue-lists").append(
			'<a class="card my-1" href="travelogue.html?' + travelogue.id + '" style="">'
			+ '<img class="center-block" style="height:' + cardWidth + 'px;" src="' + getPhoto(travelogue.photos[0]) + '">'
			+ '<div class="card-body card-img-overlay text-monospace p-0 h-100 d-flex flex-column justify-content-end">'
			+ '<div id="card-texts" class="px-3">'
			+ '<i class="fas fa-heart fa-lg fa-heart-pink">'
			+ '<small>' + travelogue.likes.likes.length + '</small>'
			+ '</i>'
			+ '<small class="text-muted">' + getDate(travelogue.date) + '</small>'
			+ '<h5 class="card-title">' + omitDescription(travelogue.title) +'</h5>'
			+ '<p class="text-right" style="font-size: 1rem">by ' + travelogue.author + '</p>'
			+ '</div></div>'
			+ '</a>'
			
			);
	$(".progress").remove();//読み込みが終了
}
var createTravelogueLists = function() {
	$.ajax({
		type: 'GET',
		url: endpoint + 'travelogues',
		contentType: 'application/json; charset=UTF-8',
		success: function(json) {
			for(var i=json.travelogues.length-1; i >= 0; i--){
				createTravelogueList(json.travelogues[i]);
			}
		}
	});
}

//旅行記の要素をフォーマットしなおす
var omitDescription = function(description){
	if(description.length>=20){
		return description.substr(0,20) + "...";
	}
	return description;
}

var getPhoto = function(photos) {
	return 'data:image/jpeg;base64,' + photos.raw;
}
var getDate = function(date) {
	var formatDate = new Date(date)
	var y = formatDate.getFullYear();
	var m = formatDate.getMonth() + 1;
	var d = formatDate.getDate();
	return y + '/' + m + '/' + d; 
}

createTravelogueLists();

