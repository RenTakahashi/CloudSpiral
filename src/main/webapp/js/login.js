// ログイン情報を送る
//user_idとpasswordを送る
$('#send').click(function() {
	var account = {user_id: $('#user_id').val(), password: $('#password').val()};
	$.ajax({
		type: 'POST',
		contentType: 'application/json',
		url: '/memoworld/api/authentication',
		data: JSON.stringify(account),
		success: function(json) {
			console.log('success');
			if(confirm("ログインページでログインしてください") == true){			
				$('#success').append('<script>location.href="account.html"</script>');
			}
			window.sessionStorage.setItem([$('#user_id').val()],[json.access_token]);
			var	access_token = window.sessionStorage.getItem([$('#user_id').val()]);
			console.log(access_token);
		}
	});
});