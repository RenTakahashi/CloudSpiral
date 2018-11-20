// 新規アカウント情報を送る
//user_idとpassword、名前を送る

$('#send').click(function() {
	var new_account = {user_id: $('#user_id').val(), password: $('#password').val(), name: $('#name').val()};
	console.log(new_account);
	console.log(JSON.stringify(new_account));
//	var access_token = '';
	$.ajax({
		type: 'POST',
		contentType: 'application/json',
		url: '/memoworld/api/accounts',
		data: JSON.stringify(new_account),
		success: function(json) {
			console.log("success!!");
			console.log(json);
			console.log(json.user_id);
			console.log(json.db_id);			
			console.log(json.name);
		}
	});
});