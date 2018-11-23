'use strict';

function initMap() {
    const defaultLocation = {lat: -34.397, lng: 150.644};	// TODO: 初期値は現在地

    // 旅程プレビュー用地図
    let mapPreview = new google.maps.Map(document.getElementById('map-preview'), {
        center: defaultLocation,
        zoom: 10
    });
    new google.maps.Marker({
        position: defaultLocation,
        map: mapPreview,
        title: 'Hello World!'
    });
}

function adjustMapSize() {
    $('[id^=map-]').each((i, element) => $(element).height($(element).width() * 0.75));
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

function showLocationSettingModal() {
    console.log('ここで撮影場所指定モーダルを表示する');
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
                                ? ' ' + photoData.location.latitude.toFixed(6) + ', ' + photoData.location.longitude.toFixed(6)
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

let photoList = [];
let inputPhotoData = {};

$(window).resize(() => {
    adjustMapSize();
});

$(document).ready(() => {
    initTemplate('旅行記作成', '<span class="fas fa-upload"></span> 投稿');

    adjustMapSize();

    //$('#add-photo-modal').on('shown.bs.modal', adjustMapSize);

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
                $('#photo-taken-location').val(latitude.toFixed(6) + ', ' + longitude.toFixed(6));
            } else {
                $('#photo-taken-location').val('');
            }
            inputPhotoData.location = location;
        });
        reader.readAsArrayBuffer(file);

        $('#photo-description').val('');

        hideElement('#select-photo-button');
        showElement('.photo-property-form, #selected-photo-area', 'd-flex');

        $('button[id$=-photo-button]').prop('disabled', false);
    });

    $('#photo-taken-location').on('click', () => {
        showLocationSettingModal();
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
