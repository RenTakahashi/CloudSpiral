// ログイン情報を送る
//user_idとpassword,名前を送る

$('#send').click(function() {
	$('#passError').empty();
	$('#idError').empty();
	$('#nameError').empty();	
	
if($('#password').val() == $('#password1').val()){
	var account = {user_id: $('#user_id').val(), password: $('#password').val(), name: $('#name').val()};
		$.ajax({
			type: 'POST',
			contentType: 'application/json',
			url: '/memoworld/api/accounts',
			data: JSON.stringify(account),
			success: function(data, status, xhr) {
				if(confirm("アカウントの登録が完了しました。\n"+"ログインページでログインしてください") == true){
					$('#success').append('<script>location.href="login.html"</script>');
				}else{
				$('#success').empty();
				$('#success').append('<div class="card row col-md-12">'
		   				+'<div class="alert alert-success my-2" role="alert">'
		   				+'アカウントの登録が完了しました。</br>'
		   				+'ログインページでログインしてください'
		   				+'</div>'
				);}
			},
			error: function(xhr, status, error){
				if(xhr.status == '400'){
					$('#user_id').val('');
					$('#password').val('');
					$('#password1').val('');
					$('#idError').append('<div class="card row col-md-12">'
			   				+'<div class="alert alert-danger my-2" role="alert">'
			   				+xhr.responseJSON.message
			   				+'</div>'
			   				+'</div>'
			   		);}
			}
		});
	}else{   	
		$('#password').val('');
		$('#password1').val('');
   		$('#passError').append('<div class="card row col-md-12">'
   				+'<div class="alert alert-danger my-2" role="alert">'
   				+'入力されたパスワードが違います。同じパスワードを入力してください'
   				+'</div>'
   		)
   	};
});