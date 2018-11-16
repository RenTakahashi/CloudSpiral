// ログイン情報を送る
//user_idとpasswordを送る
$('#send').click(function() {
	$.ajax({
		type: 'POST',
		url: 'http://localhost:8080/memoworld/api/authentication',
		data: {user_id:$('#user_id').val(), password:$('#password').val()},
		success: function(json) {
			console.log("clicked!!");
			console.log(json);
		}
	})
});
