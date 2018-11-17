// ログイン情報を送る
//user_idとpasswordを送る
//	console.log($('#user_id').val());
//	console.log($('#password').val());
//$('#send').click(function() {
//	console.log($('#user_id').val());
//	console.log($('#password').val());
//	$.ajax({
//		type: 'POST',
//		url: 'http://localhost:8080/memoworld/api/authentication',
//		data: { user_id : $('#user_id').val(), password : $('#password').val()},
//		success: function(json) {
//			console.log($('#user_id').val());
//			console.log($('#password').val());
//			console.log("clicked!!");
//			console.log(json);
//		}
//	})
//});


$('#send').click(function() {
	console.log($('#user_id').val());
	console.log($('#password').val());
	$.ajax({
		type: 'GET',
		url: 'http://localhost:8080/memoworld/api/accounts',
		success: function(json) {
			console.log("clicked!!");
			console.log(json);
		}
	})
});
