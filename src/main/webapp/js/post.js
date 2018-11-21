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

function clearInputData() {
    console.log('ここで入力データをクリアする');

    $('.photo-property-form').removeClass('d-flex');
    $('.photo-property-form').addClass('d-none');
}

$(window).resize(function() {
    adjustMapSize();
});

$(document).ready(function(){
    initTemplate('旅行記作成', '<span class="fas fa-upload"></span> 投稿');

    adjustMapSize();

    $('#add-photo-modal').on('shown.bs.modal', function() {
        adjustMapSize();
    });

    // ファイル選択ボタンがタップされたらファイル選択ダイアログを開く
    $('#select-photo-button').on('click', function() {
        $('#file-input').click();
    });

    $('#file-input').on('change', function(event) {
        console.log('ここで選択されたファイルを取得する');
        console.log(event.target.files);

        $('.photo-property-form').removeClass('d-none');
        $('.photo-property-form').addClass('d-flex');
    });

    $('#photo-taken-location').on('click', function() {
        console.log('ここで撮影場所指定モーダルを表示する');
    });

    $('#reset-photo-button').on('click', function() {
        clearInputData();
    });

    $('#add-photo-button').on('click', function() {
        console.log('ここで POST /api/photos する');

        clearInputData();
    });
});
