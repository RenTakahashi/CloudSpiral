'use strict';

const DEFAULT_LOCATION = {lat: 34.7018888889, lng: 135.494972222};

let locationSettingMap;
let previewMap = null;
let selectableMarker = null;

let photoList = [];
let inputPhotoData = {};

function initMap() {
    // 撮影場所設定用地図
    locationSettingMap = new google.maps.Map(document.getElementById('location-setting-map'), {
        center: DEFAULT_LOCATION,
        zoom: 16,
    });
    locationSettingMap.addListener('click', event => {
        console.log(this);
        console.log(event);
        inputPhotoData.location = { latitude: event.latLng.lat, longitude: event.latLng.lng };
        selectableMarker.setPosition(event.latLng);
    });

    // 旅程プレビュー用地図
    /*previewMap = new google.maps.Map(document.getElementById('preview-map'), {
        center: DEFAULT_LOCATION,
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

async function postPhoto(photoData) {
    const accessToken = await getAccessToken();
    if (!accessToken) {
        return Promise.reject();
    }

    return $.ajax({
        type: 'POST',
        url: './api/photos',
        contentType: 'application/json',
        data: JSON.stringify(photoData),
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

        $('#selected-photo').attr('src', 'img/nowloading.png');
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

            let location = null;
            if (data.GPSLatitude && data.GPSLongitude) {
                let latitude = data.GPSLatitude[0] + data.GPSLatitude[1] / 60 + data.GPSLatitude[2] / 3600;
                if (data.GPSLatitudeRef === 'S') {
                    latitude *= -1;
                }

                let longitude = data.GPSLongitude[0] + data.GPSLongitude[1] / 60 + data.GPSLongitude[2] / 3600;
                if (data.GPSLongitudeRef === 'W') {
                    longitude *= -1;
                }

                location = { latitude: latitude, longitude: longitude };
                $('#photo-taken-location').text(latitude.toFixed(5) + ', ' + longitude.toFixed(5));
            } else {
                $('#photo-taken-location').html('<span class="text-muted">タップして設定</span>');
            }
            inputPhotoData.location = location;
        });
        reader.readAsArrayBuffer(file);

        $('#photo-description').val('');

        hideElement('#select-photo-button');
        showElement('.photo-property-form, #selected-photo-area', 'd-flex');

        $('button[id$=-photo-button]').prop('disabled', false);
    });

    $('#location-setting-modal').on('shown.bs.modal', () => {
        adjustMapSize();

        let position = DEFAULT_LOCATION;
        if (inputPhotoData.location !== null) {
            position = { lat: inputPhotoData.location.latitude, lng: inputPhotoData.location.longitude };
        } else {
            console.log('ここで現在地を取得する');
        }

        if (selectableMarker === null) {
            selectableMarker = new google.maps.Marker({ position: position, map: locationSettingMap });
        } else {
            selectableMarker.setPosition(position);
        }
        locationSettingMap.setCenter(position);
    });

    $('#photo-taken-location').on('click', () => {
        $('#location-setting-modal').modal();
    });

    $('#reset-photo-button').on('click', () => {
        hideElement('.photo-property-form, #selected-photo-area');
        showElement('#select-photo-button', 'd-flex');

        // ファイルの選択状態をクリア
        $('#file-input').val('');

        $('button[id$=-photo-button]').prop('disabled', true);
    });

    $('#add-photo-button').on('click', () => {
        $('button[id$=-photo-button], input[id^=photo-taken-], textarea#photo-description').prop('disabled', true);

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
                $('button[id$=-photo-button], input[id^=photo-taken-], textarea#photo-description').prop('disabled', false);
            });
    });
});
