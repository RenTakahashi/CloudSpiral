var createTravelogueList = function(travelogue) {
	var [photo, photoDescription] = getPhoto(travelogue.photos[0])
	$().append(
			'<div class="card"><div class="row"><div class="col d-flex">'
			+ '<div>'
			+ '<img class="img-keep-ratio my-4" src="'+ photo + '">'
			+ '</div>'
			+ '<div class="card-body">'
			+ '<small class="text-muted">'
			+ travelogue.date
			+ '</small>'
			+ '<h5 class="card-title">'
			+ travelogue.title
			+ '</h5>'
			+ '<p style="font-size: 0.8rem">'
			+ description
			);
}
var endpoint = "";
var createTravelogueLists = function() {
	$.ajax({
		type: 'GET',
		url: endpoint + 'travelogues',
		success: function(json) {
			for(var i=0; json.length; i++){
				createTravelogue(json[i]);
			}
		}
	});
}

var getPhoto = function(photoID) {
	$.ajax({
		type: 'GET',
		url: endpoint + '/photos',
		success: function(json) {
			return [json.raw_uri, json.description];
		} 
	});
}

var omitDescription = function(description){
	if (description.length>10){
		return description.substr(0,10);
	}
	return description;
}