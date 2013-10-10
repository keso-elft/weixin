<!DOCTYPE html>
<html lang="cn">
<head>
<title>Talk2Me--Use HTML5 new features</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta charset="utf-8">
<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet">
<style type="text/css">
body {
	padding-top: 60px;
	padding-bottom: 40px;
}
</style>
<link rel="stylesheet" type="text/css" href="css/bootstrap-responsive.css">
<!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
</head>
<body>
	<div class="navbar navbar-inverse navbar-fixed-top">
		<div class="navbar-inner">
			<div class="container">
				<a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse"> <span class="icon-bar"></span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> </a> <a class="brand" href="#">Talk2Me</a>
				<div class="nav-collapse collapse">
					<ul class="nav">
						<li class="active"><a href="#"> <i class="icon-home"></i>主页</a></li>
						<li><a href="#about">关于</a></li>
						<li><a href="#contact">联系我</a></li>
					</ul>
				</div>
			</div>
		</div>
	</div>
	<div class="modal" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-header">
			<h3 id="myModalLabel">新用户进入</h3>
		</div>
		<div class="modal-body">
			<div id="nickname_control" class="control-group">
				<div class="controls">
					<input type="text" id="NickName" placeholder="请输入频道号" autofocus> <span class="help-inline"></span>
				</div>
			</div>
			<div id="nickname_control" class="control-group">
				<div class="controls">
					<input type="text" id="NickName" placeholder="请输入频道号" autofocus> <span class="help-inline"></span>
				</div>
			</div>
		</div>
		<div class="modal-footer">
			<button class="btn btn-primary" id="userInfo" data-dismiss="modal" aria-hidden="true">进入</button>
		</div>
	</div>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span3">
				<div class="well sidebar-nav">
					<ul class="nav nav-list" id="user_list">
						<li class="nav-header">Online People</li>
					</ul>
				</div>
				<center>
					<button class="btn btn-primary" id="quitChatroom">离开聊天室</button>
				</center>
			</div>
			<div class="span9">
				<fieldset class="well">
					<p class="head text-success">[+] 欢迎来到Talk2Me在线聊天室，本站基于HTML5多种新特性开发，Enjoy ur self!</p>
					<div class="text-info" id="chatlog" style="height: 350px; overflow: auto;"></div>
				</fieldset>
				<form class="form-inline">
					<input type="text" id="messageTextBox" placeholder="输入聊天内容">
					<button type="button" id="sendMessage" class="btn">发送</button>
				</form>
			</div>
		</div>
		<hr>
		<footer>
			<p>© pnig0s@Freebuf.Com 2012 Based on HTML5 new features</p>
		</footer>
	</div>
	<script src="js/jquery-latest.js"></script>
	<script src="js/bootstrap.min.js"></script>
	<script type="text/javascript">
		var curr_user_nickname = '';
		var curr_user_sex = '';
		var socket;
		var wsServer = 'ws://127.0.0.1:2012/chatServ'

		$('#myModal').modal('show');

		function checkAvailable() {
			if (!("WebSocket" in window)) {
				if (this.MozWebSocket) {
					WebSocket = this.MozWebSocket;
				} else {
					return false;
				}
			}
			return true;
		}

		var result = checkAvailable();
		if (!result) {
			var message = "[-] 浏览器不支持或版本过低,无法使用Talk2Me。";
			writeToChatLog(message, 'text-error');
		} else {
			socket = {
				start : function() {
					this._ws = new WebSocket(wsServer);
					this._ws.onopen = this.whenOpen;
					this._ws.onmessage = this.whenMessage;
					this._ws.onclose = this.whenClose;
					this._ws.onerror = this.whenError;
				},
				whenOpen : function(m) {
					var message = '[*] 成功连接聊天室服务器....';
					writeToChatLog(message, 'text-info');
				},
				sendMsg : function(message) {
					if (this._ws) {
						this._ws.send(message);
					}
				},
				whenMessage : function(m) {
					var msgObj = JSON.parse(m.data);
					if (msgObj.type == "JOIN") {
						writeToChatLog(msgObj.msg, msgObj.text_type);
						if (!msgObj.succ) {
							return;
						}
						user = document.getElementById('user_list');
						userNodes = document.getElementById('user_list').children;
						userNodes_length = userNodes.length;
						for ( var i = 0; i < userNodes_length; i++) {
							if (userNodes[1]) {
								user.removeChild(userNodes[1]);
							}
						}

						for ( var user_name in msgObj.user_list) {
							user_list = document.getElementById("user_list");
							var newUser = document.createElement("li");
							var icon_type = "";
							if (msgObj.user_list[user_name] == "b") {
								icon_type = "/img/boy.png";
							} else {
								icon_type = "/img/girl.png";
							}
							if (user_name == curr_user_nickname) {
								newUser.innerHTML = '<img src='+icon_type+'/><strong> '
										+ user_name + '</strong>';
							} else {
								newUser.innerHTML = '<img src='+icon_type+'/> '
										+ user_name;
							}
							user_list.appendChild(newUser);
						}
					} else if (msgObj.type == "QUIT") {
						user = document.getElementById('user_list');
						userNodes = document.getElementById('user_list').children;
						userNodes_length = userNodes.length;
						for ( var i = 0; i < userNodes_length; i++) {
							if (userNodes[1]) {
								user.removeChild(userNodes[1]);
							}
						}

						for ( var user_name in msgObj.user_list) {
							user_list = document.getElementById("user_list");
							var newUser = document.createElement("li");
							var icon_type = "";
							if (msgObj.user_list[user_name] == "b") {
								icon_type = "/img/boy.png";
							} else {
								icon_type = "/img/girl.png";
							}
							if (user_name == curr_user_nickname) {
								newUser.innerHTML = '<img src='+icon_type+'/><strong>'
										+ user_name + '</strong>';
							} else {
								newUser.innerHTML = '<img src='+icon_type+'/>'
										+ user_name;
							}
							user_list.appendChild(newUser);
						}

						writeToChatLog(msgObj.msg, msgObj.text_type);
					} else if (msgObj.type == "POST") {
						writeToChatLog(msgObj.msg, msgObj.text_type);
					}
				},
				whenClose : function(m) {
					this._ws = null;
					var message = '[-] 连接已断开.'
					writeToChatLog(message, 'text-error');
				},
				whenError : function(m) {
					var message = '[-] 出现未知错误，可能导致连接中断...';
					writeToChatLog(message, 'text-error');
				}
			};
		}

		function writeToChatLog(message, message_type) {
			document.getElementById('chatlog').innerHTML += '<p class=\"'+message_type+'\">'
					+ message + '</p>';
		}
		window.onunload = function() {
			quit();
		};

		function quit() {
			var msg = new Object;
			msg.type = "QUIT";
			msg.user_nickname = curr_user_nickname;
			socket.sendMsg(JSON.stringify(msg));
		}
		socket.start();
		document.getElementById('userInfo')
				.addEventListener(
						'click',
						function() {
							curr_user_nickname = document
									.getElementById('NickName').value;
							var radio_list = document
									.getElementsByName('options');
							for ( var i = 0; i < radio_list.length; i++)

							{
								if (radio_list[i].checked) {
									curr_user_sex = radio_list[i].value;
									break;
								}
							}
							var msg = new Object;
							msg.type = "JOIN";
							msg.user_nickname = curr_user_nickname;
							msg.user_sex = curr_user_sex;
							socket.sendMsg(JSON.stringify(msg));

							setInterval(function() {
								var msg = new Object
								msg.type = "HEARTBEAT";
								socket.sendMsg(JSON.stringify(msg));
							}, 60000);

						}, true);

		document.getElementById('sendMessage').addEventListener('click',
				function() {
					message = document.getElementById('messageTextBox').value;
					if (curr_user_nickname) {
						var msg = new Object;
						msg.type = "POST";
						msg.from = curr_user_nickname;
						msg.context = message;
						msg.to = '';
						socket.sendMsg(JSON.stringify(msg));
					}
				}, true);

		document.getElementById('quitChatroom').addEventListener('click',
				function() {
					quit();
				}, true);
	</script>
</body>
</html>
