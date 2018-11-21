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
    $('[id^=map-]').each(function(i) {
        $(this).height($(this).width() * 0.75);
    });
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

let photoList = [];

$(window).resize(function() {
    adjustMapSize();
});

$(document).ready(function(){
    initTemplate('旅行記作成', '<span class="fas fa-upload"></span> 投稿');

    adjustMapSize();

    $('#add-photo-modal').on('shown.bs.modal', function() {
        adjustMapSize();
    });

    // ファイル選択ボタンか選択済みの画像がタップされたらファイル選択ダイアログを開く
    $('#select-photo-button, #selected-photo-area').on('click', function() {
        $('#file-input').click();
    });

    $('#file-input').on('change', function(event) {
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
        reader.addEventListener('load', function(event) {
            const base64Img = btoa(new Uint8Array(reader.result)
                .reduce((data, byte) => data + String.fromCharCode(byte), ''));
            const dataUrl = 'data:' + file.type + ';base64,' + base64Img;
            $('#selected-photo').attr('src', dataUrl);

            const data = EXIF.readFromBinaryFile(reader.result);
            console.log(data);

            if (data.DateTimeOriginal) {
                const dateTime = data.DateTimeOriginal.split(' ');
                $('#photo-taken-date').val(dateTime[0].replace(/:/g, '-'));
                $('#photo-taken-time').val(dateTime[1]);
            } else {
                const dateTime = new Date(file.lastModified);
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

            if (data.GPSLatitude && data.GPSLongitude) {
                let latitude = data.GPSLatitude[0] + data.GPSLatitude[1] / 60 + data.GPSLatitude[2] / 3600;
                if (data.GPSLatitudeRef === 'S') {
                    latitude *= -1;
                }

                let longitude = data.GPSLongitude[0] + data.GPSLongitude[1] / 60 + data.GPSLongitude[2] / 3600;
                if (data.GPSLongitudeRef === 'W') {
                    longitude *= -1;
                }

                $('#photo-taken-location').val(latitude.toFixed(6) + ', ' + longitude.toFixed(6));
            } else {
                $('#photo-taken-location').val('');
            }
        });
        reader.readAsArrayBuffer(file);

        $('#photo-description').val('');

        hideElement('#select-photo-button');
        showElement('.photo-property-form, #selected-photo-area', 'd-flex');
    });

    $('#photo-taken-location').on('click', function() {
        console.log('ここで撮影場所指定モーダルを表示する');
    });

    $('#reset-photo-button').on('click', function() {
        hideElement('.photo-property-form, #selected-photo-area');
        showElement('#select-photo-button', 'd-flex');

        // ファイルの選択状態をクリア
        $('#file-input').val('');
    });

    $('#add-photo-button').on('click', function() {
        console.log('ここで POST /api/photos する');

        console.log('投稿できたら #photo-list にカードを追加する');

        hideElement('.photo-property-form, #selected-photo-area');
        showElement('#select-photo-button', 'd-flex');

        // ファイルの選択状態をクリア
        $('#file-input').val('');
    });
});
