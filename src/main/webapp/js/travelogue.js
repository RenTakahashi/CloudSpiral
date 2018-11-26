const DEFAULT_LOCATION_SETTING_MAP_ZOOM = 16;
const DEFAULT_LOCATION = {lat: 34.7018888889, lng: 135.494972222};
let DEFAULT_LATLNG;
let map;
let marker = [];
let infoWindow = [];

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
	})
	.then(result => {
		$("#travel-title").append(
				'<p>' + getDate(result.date) + '</p>'
				+ '<p>' + result.title + '</p>'
				+ '<p>by ' + result.author + '</p>'
				);
		let photoList = [];
		for(var i = 0; i < result.photos.length; i++) {
			photoList[i] = result.photos[i];
			$("#travel-photos").append(
					'<div class="row">'
					+ '<div class="col">' + '<img class="img-fluid" src="' + getPhoto(result.photos[i]) + '"></div>'
					+ '<div class="col">' + '<p class="card-text">' + result.photos[i].description + '</p>'
					+ '<p>' + getDate(result.photos[i].date) + '</p></div>'
					+ '</div>'
					);
			marker[i] = new google.maps.Marker({
				position: new google.maps.LatLng(result.photos[i].location.latitude, result.photos[i].location.longitude),
				map: map,
			});
			infoWindow[i] = new google.maps.InfoWindow({
				content: result.photos[i].description + '<br><img src="' + getPhoto(result.photos[i]) + '" width="300">', 
			});
			markerEvent(i);
		}
		updateMap(photoList);
	});
}

function updateMap(photoList) {
    const locations = photoList
    	.filter(x => x.location.latitude !== 0 || x.location.longitude !== 0)
    	.map(x => ({ lat: x.location.latitude, lng: x.location.longitude }));
    let sumLocation = locations.reduce(
    		(sum, x) => ({ lat: sum.lat + x.lat, lng: sum.lng + x.lng }),
    		{ lat: 0, lng: 0 });
    map.setCenter({
    	lat: sumLocation.lat / locations.length,
    	lng: sumLocation.lng / locations.length,
    });
    map.setZoom(DEFAULT_LOCATION_SETTING_MAP_ZOOM);
    const bounds = locations.reduce(
    		(bound, x) => bound.extend({ lat: x.lat, lng: x.lng }),
    		new google.maps.LatLngBounds()
    );
    map.fitBounds(bounds);
    if (map.getZoom() > DEFAULT_LOCATION_SETTING_MAP_ZOOM) {
    	map.setZoom(DEFAULT_LOCATION_SETTING_MAP_ZOOM);
    }
}

function markerEvent(i) {
	marker[i].addListener('click', function() {
		infoWindow[i].open(map, marker[i]);
	});
}

var getPhoto = function(photos) {
	return 'data:image/jpeg;base64,' + photos.raw;
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