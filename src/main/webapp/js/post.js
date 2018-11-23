'use strict';

const DEFAULT_LOCATION_SETTING_MAP_ZOOM = 16;
const DEFAULT_LOCATION = {lat: 34.7018888889, lng: 135.494972222};
let DEFAULT_LATLNG;

let locationSettingMap;
let previewMap = null;
let selectableMarker;

let photoList = [];
let inputPhotoData = {};

function initMap() {
    DEFAULT_LATLNG = new google.maps.LatLng(DEFAULT_LOCATION);

    // 撮影場所設定用地図
    locationSettingMap = new google.maps.Map(document.getElementById('location-setting-map'), {
        center: DEFAULT_LATLNG,
        zoom: DEFAULT_LOCATION_SETTING_MAP_ZOOM,
    });
    locationSettingMap.addListener('click', event => {
        console.log(this);
        console.log(event);
        selectableMarker.setPosition(event.latLng);
    });

    selectableMarker = new google.maps.Marker({ position: DEFAULT_LATLNG, map: locationSettingMap });

    // 旅程プレビュー用地図
    /*previewMap = new google.maps.Map(document.getElementById('preview-map'), {
        center: DEFAULT_LATLNG,
        zoom: 14,
    });*/
}

function adjustMapSize() {
    $('[id$=-map]').each((i, element) => $(element).height($(element).width() * 0.75));
}

// $(selector) の d-none クラスを削除して、 displayType で指定されたクラスを追加する
function showElement(selector, displayType) {
    $(selector).removeClass('d-none');
    $(selector).addClass(displayType);
}

// $(selector) の d-**** クラスを削除して、 .d-none クラスを追加する
// 参考: https://qiita.com/shouchida/items/01bada913bf660cdad03
function hideElement(selector) {
    $(selector).removeClass((i, className) => (className.match(/\bd-\S+/g) || []).join(' '));
    $(selector).addClass('d-none');
}

// for debug
function _authorize() {
    return $.ajax({
        type: 'POST',
        url: './api/authentication',
        contentType: 'application/json',
        data: JSON.stringify({ user_id: 'hogefuga', password: 'Password' }),
    })
    .then(result => {
        return result.access_token;
    })
    .catch(result => {
        console.log(result);
        return null;
    });
}

async function getAccessToken() {
    console.log('ここで SessionStorage からアクセストークンを取得する');
    console.log('今はテストで値を返す');
    const token = await _authorize();
    return token;
}

async function postPhoto(requestData) {
    const accessToken = await getAccessToken();
    if (!accessToken) {
        return Promise.reject();
    }

    return $.ajax({
        type: 'POST',
        url: './api/photos',
        contentType: 'application/json; charset=UTF-8',
        data: JSON.stringify(requestData),
        beforeSend: xhr => {
            xhr.setRequestHeader("Authorization", "Bearer " + accessToken);
        },
    });
}

function getDataUrl(mime, base64) {
    return 'data:' + mime + ';base64,' + base64;
}

function appendPhoto(photoData) {
    const localDateStr = photoData.date.toLocaleDateString();
    const localTimeStr = photoData.date.toLocaleTimeString();
    const localDateTimeStr = localDateStr + ' ' + localTimeStr;

    const imgElement = $('<img>')
                       .attr('src', getDataUrl(photoData.mime, photoData.raw));
    const descriptionElement = $('<p class="card-text">')
                               .text(photoData.description);
    const dateElement = $('<p>')
                        .text(' ' + localDateTimeStr)
                        .prepend($('<span class="fas fa-clock">'));
    const locationElement = $('<p>')
                            .append($('<span class="fas fa-map-marker-alt">'))
                            .append(photoData.location.latitude !== 0 || photoData.location.longitude !== 0 // うーん
                                ? ' ' + photoData.location.latitude.toFixed(5) + ', ' + photoData.location.longitude.toFixed(5)
                                : ' <span class="text-muted">(未設定)</span>');
    const cardElement =
        $('<div class="card">')
        .attr('id', 'photo-card-' + photoData.id)
        .append($('<div class="row">')
                .append($('<div class="col">').append(imgElement))
                .append($('<div class="col">')
                        .append(descriptionElement)
                        .append(dateElement)
                        .append(locationElement)
                        ));
    $('#photo-list').append(cardElement);

    // 写真追加フォームを一番下へ
    $('#photo-list').append($('#new-photo'));
}

function appendPhotos(photoDataList) {
    photoDataList.forEach(appendPhoto);
}

function setLatLngToButton(latLng) {
    $('#photo-taken-location').text(latLng.lat().toFixed(5) + ', ' + latLng.lng().toFixed(5));
}

// 現在地を取得する
// 取得できなかったら null を返す
function getCurrentLatLng() {
    console.log('ここで現在地を取得する');
    return null;
}

$(window).resize(() => {
    adjustMapSize();
});

$(document).ready(() => {
    initTemplate('旅行記作成', '<span class="fas fa-upload"></span> 投稿');

    adjustMapSize();

    // ファイル選択ボタンか選択済みの画像がタップされたらファイル選択ダイアログを開く
    $('#select-photo-button, #selected-photo-area').on('click', () => {
        $('#file-input').click();
    });

    $('#file-input').on('change', event => {
        if (event.target.files.length === 0) {
            // キャンセル
            return;
        }

        const file = event.target.files[0];
        if (!file.type.startsWith('image/')) {
            alert('画像ファイルを選択してください');

            return;
        }

        inputPhotoData = {};

        $('button[id$=-photo-button], [id^=photo-taken-], textarea#photo-description').prop('disabled', true);

        $('#selected-photo').attr('src', 'img/nowloading.png');
        $('#photo-taken-date').val('');
        $('#photo-taken-time').val('');
        $('#photo-taken-location').text('');
        $('#photo-description').val('');

        const reader = new FileReader();
        reader.addEventListener('load', event => {
            const base64Img = btoa(new Uint8Array(reader.result)
                .reduce((data, byte) => data + String.fromCharCode(byte), ''));
            inputPhotoData.mime = file.type;
            inputPhotoData.raw = base64Img;

            $('#selected-photo').attr('src', getDataUrl(file.type, base64Img));

            const data = EXIF.readFromBinaryFile(reader.result);

            let dateTime;
            if (data.DateTimeOriginal) {
                const splitDateTime = data.DateTimeOriginal.split(/:| /);
                $('#photo-taken-date').val(splitDateTime.slice(0, 3).join('-'));
                $('#photo-taken-time').val(splitDateTime.slice(3, 6).join(':'));
                splitDateTime[1] -= 1;  // because month of Date constructor's arguments is 0-11.
                dateTime = new Date(...splitDateTime);
            } else {
                dateTime = new Date(file.lastModified);
                $('#photo-taken-date').val([
                    dateTime.getFullYear(),
                    ('0' + (dateTime.getMonth()+1).toString()).slice(-2),
                    ('0' + dateTime.getDate().toString()).slice(-2)
                ].join('-'));
                $('#photo-taken-time').val([
                    ('0' + dateTime.getHours().toString()).slice(-2),
                    ('0' + dateTime.getMinutes().toString()).slice(-2),
                    ('0' + dateTime.getSeconds().toString()).slice(-2)
                ].join(':'));
            }
            inputPhotoData.date = dateTime;

            if (data.GPSLatitude && data.GPSLongitude) {
                let latitude = data.GPSLatitude[0] + data.GPSLatitude[1] / 60 + data.GPSLatitude[2] / 3600;
                if (data.GPSLatitudeRef === 'S') {
                    latitude *= -1;
                }

                let longitude = data.GPSLongitude[0] + data.GPSLongitude[1] / 60 + data.GPSLongitude[2] / 3600;
                if (data.GPSLongitudeRef === 'W') {
                    longitude *= -1;
                }

                inputPhotoData.latLng = new google.maps.LatLng(latitude, longitude);
                setLatLngToButton(inputPhotoData.latLng);
            } else {
                $('#photo-taken-location').html('<span class="text-muted">タップして設定</span>');
            }
        });
        reader.readAsArrayBuffer(file);

        hideElement('#select-photo-button');
        showElement('.photo-property-form, #selected-photo-area', 'd-flex');

        $('button[id$=-photo-button], [id^=photo-taken-], textarea#photo-description').prop('disabled', false);
    });

    $('#location-setting-modal').on('shown.bs.modal', () => {
        adjustMapSize();

        const latLng = inputPhotoData.latLng || getCurrentLocation() || DEFAULT_LOCATION;

        selectableMarker.setPosition(latLng);
        locationSettingMap.setCenter(latLng);
    });

    $('#reset-location-button').on('click', () => {
        let latLng = inputPhotoData.latLng || getCurrentLatLng() || DEFAULT_LATLNG;
        selectableMarker.setPosition(latLng);
        locationSettingMap.setCenter(latLng);
        locationSettingMap.setZoom(DEFAULT_LOCATION_SETTING_MAP_ZOOM);
    })

    $('#select-location-button').on('click', () => {
        inputPhotoData.latLng = selectableMarker.getPosition();
        setLatLngToButton(inputPhotoData.latLng);
        $('#location-setting-modal').modal('toggle');
    })

    $('#photo-taken-location').on('click', () => {
        $('#location-setting-modal').modal('toggle');
    });

    $('#reset-photo-button').on('click', () => {
        hideElement('.photo-property-form, #selected-photo-area');
        showElement('#select-photo-button', 'd-flex');

        inputPhotoData = {};

        // ファイルの選択状態をクリア
        $('#file-input').val('');

        $('button[id$=-photo-button]').prop('disabled', true);
    });

    $('#add-photo-button').on('click', () => {
        $('button[id$=-photo-button], [id^=photo-taken-], textarea#photo-description').prop('disabled', true);

        inputPhotoData.location = inputPhotoData.latLng != null
            ? { latitude: inputPhotoData.latLng.lat(), longitude: inputPhotoData.latLng.lng() }
            : null;
        inputPhotoData.description = $('#photo-description').val();

        postPhoto(inputPhotoData)
            .then(result => {
                result.raw = inputPhotoData.raw;
                result.date = new Date(result.date);
                appendPhoto(result);

                hideElement('.photo-property-form, #selected-photo-area');
                showElement('#select-photo-button', 'd-flex');

                // ファイルの選択状態をクリア
                $('#file-input').val('');
            })
            .catch(result => {
                console.error(result);
            })
            .finally(() => {
                $('button[id$=-photo-button], [id^=photo-taken-], textarea#photo-description').prop('disabled', false);
            });
    });
});
