// ログイン情報を送る
//user_idとpasswordを送る

$('#send').click(function() {
	var account = {user_id: $('#user_id').val(), password: $('#password').val()};
	console.log(account);
	console.log(JSON.stringify(account));
	$.ajax({
		type: 'POST',
		url: '/memoworld/api/authentication',
		data: JSON.stringify(account),
		success: function(json) {
			console.log("success!!");
			console.log(json);
		}
	});
});


//$('#send').click(function() {
//	console.log($('#user_id').val());
//	console.log($('#password').val());
//	let account = {user_id:$('#user_id').val(), password:$('#password').val()};	
//	console.log(account);
//	$.ajax({
//		type: 'POST',
//		url: 'http://localhost:8080/memoworld/api/authentication',
//		data: JSON.stringify(account),
//		success: function(json) {
//			console.log("送信成功!!");
//			console.log("clicked!!");
//			console.log(json);
//		}
//	});
//});


//$('#send').click(function() {
//	console.log($('#user_id').val());
//	console.log($('#password').val());
//	$.ajax({
//		type: 'GET',
//		url: 'http://localhost:8080/memoworld/api/accounts',
//		success: function(json) {
//			console.log("clicked!!");
//			console.log(json);
//		}
//	})
//});
