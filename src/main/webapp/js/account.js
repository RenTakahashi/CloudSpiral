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
			success: function(json) {
				console.log('success');
				if(confirm("ログインページでログインしてください") == true){			
					$('#success1').append('<script>location.href="login.html"</script>');
				}
			},
			error: function(xhr, status, error){
				if(xhr.status == '400'){
					$('#idError').append('<div class="card row col-md-12">'
			   				+'<div class="alert alert-danger" role="alert">'
			   				+'すでにログインIDが存在します。異なるログインIDを入力してください'
			   				+'</div>'
				)}
				if(xhr.status == '430'){
					$('#passError').append('<div class="card row col-md-12">'
			   				+'<div class="alert alert-danger" role="alert">'
			   				+'パスワードが入力されていません。'
			   				+'</div>'
				)}
				if(xhr.status == '431'){
					$('#passError').append('<div class="card row col-md-12">'
			   				+'<div class="alert alert-danger" role="alert">'
			   				+'パスワードが短すぎです'
			   				+'</div>'
				)}
				if(xhr.status == '432'){
					$('#passError').append('<div class="card row col-md-12">'
			   				+'<div class="alert alert-danger" role="alert">'
			   				+'パスワードが小文字または大文字のみになっています。大文字と小文字の両方を含めてください'
			   				+'</div>'
				)}				
				if(xhr.status == '433'){
					$('#nameError').append('<div class="card row col-md-12">'
			   				+'<div class="alert alert-danger" role="alert">'
			   				+'名前が入力されていません。名前を入力してください'
			   				+'</div>'
				)}
				if(xhr.status == '434'){
					$('#idError').append('<div class="card row col-md-12">'
			   				+'<div class="alert alert-danger" role="alert">'
			   				+'すでにログインIDが入力されていません。ログインIDを入力してください'
			   				+'</div>'
				)}
			}
		});
		}else{   	 
   		$('#passError').append('<div class="card row col-md-12">'
   				+'<div class="alert alert-danger" role="alert">'
   				+'入力されたパスワードが違います。同じパスワードを入力してください'
   				+'</div>'
   		);
   		$('#password').text();
   		$('#password1').text();
   	};
});