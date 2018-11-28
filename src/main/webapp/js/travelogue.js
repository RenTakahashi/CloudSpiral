const DEFAULT_LOCATION_SETTING_MAP_ZOOM = 16;
const DEFAULT_LOCATION = {lat: 34.7018888889, lng: 135.494972222};
let DEFAULT_LATLNG;
let map;
let marker = [];
let infoWindow = [];
let id;
var debug;

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

function updateLikes() {
	$.ajax({
		type: 'GET',
		url: './api/likes/' + id,
		contentType: 'application/json; charset=UTF-8',
	})
	.then(result => {
		debug = result;
		$("#travel-likes").text("");
		$("#travel-likes").append(
				'<div class="float-right">いいね数：　'+ Object.keys(result.likes).length + '</div>'
				);
	});
}

function getTravelogueId() {
	return decodeURIComponent(location.search.substring(1));
}

function getTravelogue() {
	$.ajax({
		type: 'GET',
		url: './api/travelogues/' + id,
		contentType: 'application/json; charset=UTF-8',
	})
	.then(result => {
		$("#travel-title").append(
				'<h4>' + result.title + '</h4>'
				+ '<div class="float-left">' + getDate(result.date) + '</div>'
				+ '<div class="float-right">by ' + result.author + '</div>'
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
				content: result.photos[i].description + '<br><img src="' + getPhoto(result.photos[i]) + '" width="500">', 
			});
			markerEvent(i);
		}
		updateMap(photoList);
	});
	updateLikes();
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
		infoWindow.forEach(window => window.close());
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
	let h = formatDate.getHours();
	let min = formatDate.getMinutes();
	return y + '/' + m + '/' + d + ' ' + h + ':' + min;
}

function postLike() {
	return $.ajax({
		type: 'POST',
		url: './api/likes/' + id,
		contentType: 'application/json; charset=UTF-8',
	});
}

$(window).resize(() => {
    adjustMapSize();
});

$(document).ready(() => {
	id = getTravelogueId();
	initTemplate(
			'旅行記の詳細',
			'いいね',
			'travelogue.html?' + getTravelogueId(), 
	        (resolve, reject) => {
	        	postLike()
	        	.then(result => {
//	                resolve('travelogue.html?' + id);
	        		updateLikes();
	        	})
	        	.catch(result => {
	        		reject();
	        	});
	        });
	if(id) {
		$('body').append('<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAeH3QEuI3KqOwBkkjJ6nYe-jQBTWGDQdw&callback=initMap" async defer>');
		getTravelogue();
	    adjustMapSize();
	} else {
		location.href="index.html";
	}
});
