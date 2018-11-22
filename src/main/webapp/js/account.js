// ログイン情報を送る
//user_idとpassword,名前を送る

$('#send').click(function() {
if($('#password').val() == $('#password1').val()){
	var account = {user_id: $('#user_id').val(), password: $('#password').val(), name: $('#name').val()};
		$.ajax({
			type: 'POST',
			contentType: 'application/json',
			url: '/memoworld/api/accounts',
			data: JSON.stringify(account),
			success: function(json) {
				console.log('success');
				if(confirm("ログインページでログインしてください") == true){			
					$('#success1').append('<script>location.href="login.html"</script>');
				}
			}
		});
   	}else{
   		$('#success1').append('<div class="card row col-md-12">'
   				+'<div class="card-body">'
   				+'<p>入力されたパスワードが違います。もう一度パスワードを入力しなおしてください</p>'
   				+'</div>'
   				+'</div>'
   		);
   	};
});