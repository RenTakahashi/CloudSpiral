// ログイン情報を送る
//user_idとpasswordを送る

$('#send').click(function() {
	var account = {user_id: $('#user_id').val(), password: $('#password').val()};
	console.log(account);
	console.log(JSON.stringify(account));
//	var access_token = '';
	$.ajax({
		type: 'POST',
		contentType: 'application/json',
		url: '/memoworld/api/authentication',
		data: JSON.stringify(account),
		success: function(json) {
			console.log("success!!");
			console.log(json);
			console.log(json.access_token);
			console.log(json.token_type);			
			window.sessionStorage.setItem([$('#user_id').val()],[json.access_token]);
			var	access_token = window.sessionStorage.getItem([$('#user_id').val()]);
			console.log(access_token);
		}
	});
});

