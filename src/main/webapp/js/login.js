// ログイン情報を送る
//user_idとpasswordを送る

$('#send').click(function() {
	console.log($('#user_id').val());
	console.log($('#password').val());
	console.log($('#name').val());
	//	let data = {
//		      user_id: $('#user_id').val(),
//		      password: $('#password').val()
//	};
//	console.log(data);
	$.ajax({
		type: 'POST',
        contentType: 'application/JSON',
        dataType : 'JSON',
		url: 'http://localhost:8080/memoworld/api/accounts',		
		data: { user_id : $('#user_id').val(), password : $('#password').val(), name : $('#name').val()},
		success: function(json) {
			console.log("送信成功!!");
			console.log($('#user_id').val());
			console.log($('#password').val());
			console.log($('#name').val());
			console.log("clicked!!");
			console.log(json);
		}
	})
});


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
