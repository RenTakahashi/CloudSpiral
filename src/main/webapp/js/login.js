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
				$('#success').append('<script>location.href="index.html"</script>');
			window.sessionStorage.setItem(['access_token'],[json.access_token]);
		},
		error: function(xhr, status, error){
			if(xhr.status == '401'){
				$('#user_id').val('');
				$('#password').val('');
				$('#loginError').append('<div class="card row col-md-12">'
		   				+'<div class="alert alert-danger my-2" role="alert">'
		   				+'パスワードもしくはログインIDが間違えています。もう一度入力してください'
		   				+'</div>'
			)}
		}
	});
});