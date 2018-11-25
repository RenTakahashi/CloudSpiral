const DEFAULT_LOCATION_SETTING_MAP_ZOOM = 16;
const DEFAULT_LOCATION = {lat: 34.7018888889, lng: 135.494972222};
let DEFAULT_LATLNG;
let map;

function initMap() {
	DEFAULT_LATLNG = new google.maps.LatLng(DEFAULT_LOCATION);
	map = new google.maps.Map(document.getElementById('travel-map'), {
		center: DEFAULT_LATLNG,
	    zoom: DEFAULT_LOCATION_SETTING_MAP_ZOOM,
	});
}

function adjustMapSize() {
    $('[id$=-map]').each((i, element) => $(element).height($(element).width() * 0.75));
}

function getTravelogueId() {
	return decodeURIComponent(location.search.substring(1));
}

function getTravelogue(id) {
	$.ajax({
		type: 'GET',
		url: './api/travelogues/' + id,
		contentType: 'application/json; chrset=UTF-8',
		success: function(json) {
			$("#travel-title").append(
					'<p>' + getDate(json.date) + '</p>'
					+ '<p>' + json.title + '</p>'
					+ '<p>by ' + json.author + '</p>'
					);
		}
	});
}

function getDate(date) {
	let formatDate = new Date(date)
	let y = formatDate.getFullYear();
	let m = formatDate.getMonth() + 1;
	let d = formatDate.getDate();
	return y + '/' + m + '/' + d;
}

$(window).resize(() => {
    adjustMapSize();
});

$(document).ready(() => {
	initTemplate(
			'旅行記の詳細',
			'',
			'index.html',
			);
	let id = getTravelogueId();
	if(id) {
		getTravelogue(id);
	} else {
		location.href="index.html";
	}
	$('body').append('<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAeH3QEuI3KqOwBkkjJ6nYe-jQBTWGDQdw&callback=initMap" async defer>');
    adjustMapSize();
});