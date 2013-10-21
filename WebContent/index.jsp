<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet">
<style type="text/css">
body {
	padding-top: 60px;
	padding-bottom: 40px;
}
</style>
<link rel="stylesheet" type="text/css" href="css/bootstrap-responsive.css">
<title>ChannelRoom</title>
</head>
<body>
	<!-- 导航条 -->
	<div class="navbar navbar-inverse navbar-fixed-top">
		<div class="navbar-inner">
			<div class="container">
				<a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse"> <span class="icon-bar"></span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> </a> <a class="brand" href="#">ChannelRoom</a>
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

	<!-- 登录框 -->
	<div class="modal" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-header">
			<h3 id="myModalLabel">进入频道</h3>
		</div>
		<div class="modal-body">
			<div id="nickname_control" class="control-group">
				<div class="controls">
					<input type="text" id="channelId" placeholder="请输入频道ID" autofocus> <span class="help-inline"></span>
				</div>
				<div class="controls">
					<input type="password" id="password" placeholder="请输入频道密码" autofocus> <span class="help-inline"></span>
				</div>
			</div>
		</div>
		<div class="modal-footer">
			<button class="btn btn-primary" id="userInfo" data-dismiss="modal" aria-hidden="true" onclick="connect()">进入</button>
		</div>
	</div>

	<!-- 主界面 -->
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span3">
				<div class="well sidebar-nav">
					<ul class="nav nav-list" id="user_list">
						<li class="nav-header">Online People</li>
					</ul>
				</div>
				<center>
					<button class="btn btn-primary" id="quitChatroom" onclick="quit()">离开ChannelRoom</button>
				</center>
			</div>
			<div class="span9">
				<fieldset class="well">
					<p class="head text-success">[+] 欢迎来到ChannelRoom!</p>
					<div class="text-info" id="chatlog" style="height: 350px; overflow: auto;"></div>
				</fieldset>
			</div>
		</div>
		<hr>
		<footer>
		<p>© keso.elft@gmail.com 2013 Based on HTML5 new features</p>
		</footer>
	</div>
	<script type="text/javascript" src="js/jquery-1.4.2.min.js"></script>
	<script type="text/javascript">
		//var server = 'http://127.0.0.1:8080/weixin/comet?channelId=1';
		var server = 'http://127.0.0.1:8080/weixin/comet';

		var comet = {
			connection : false,
			iframediv : false,

			initialize : function(channelId, password) {
				var address = server + '?channelId=' + channelId + '&password='
						+ password;

				if (navigator.appVersion.indexOf("MSIE") != -1) {
					comet.connection = new ActiveXObject("htmlfile");
					comet.connection.open();
					comet.connection.write("<html>");
					comet.connection.write("<script>document.domain = '"
							+ document.domain + "'");
					comet.connection.write("</html>");
					comet.connection.close();
					comet.iframediv = comet.connection.createElement("div");
					comet.connection.appendChild(comet.iframediv);
					comet.connection.parentWindow.comet = comet;
					comet.iframediv.innerHTML = "<iframe id='comet_iframe' src='"+address+"'></iframe>";

				} else if (navigator.appVersion.indexOf("KHTML") != -1) {
					comet.connection = document.createElement('iframe');
					comet.connection.setAttribute('id', 'comet_iframe');
					comet.connection.setAttribute('src', address);
					with (comet.connection.style) {
						position = "absolute";
						left = top = "-100px";
						height = width = "1px";
						visibility = "hidden";
					}
					document.body.appendChild(comet.connection);

				} else {
					comet.connection = document.createElement('iframe');
					comet.connection.setAttribute('id', 'comet_iframe');
					with (comet.connection.style) {
						left = top = "-100px";
						height = width = "1px";
						visibility = "hidden";
						display = 'none';
					}
					comet.iframediv = document.createElement('iframe');
					comet.iframediv.setAttribute('src', address);
					comet.connection.appendChild(comet.iframediv);
					document.body.appendChild(comet.connection);
				}

				return comet.connection;
			},

			//添加正常消息
			newMessage : function(data) {
				writeToChatLog(data, 'text-info');
			},

			//退出
			onUnload : function() {
				if (comet.connection) {
					comet.connection = false;
				}
			}
		}//comet end

		function connect() {
			var channelId = document.getElementById('channelId');
			var password = document.getElementById('password');
			if (comet.initialize(channelId.value, password.value)) {
				document.getElementById('myModal').style.display = "none";
			}
		}

		function quit() {
			comet.onUnload();
			document.getElementById('myModal').style.visibility = "visible";
			document.getElementById('myModal').style.display = "block";
		}

		function writeToChatLog(message, message_type) {
			document.getElementById('chatlog').innerHTML += '<p class=\"'+message_type+'\">'
					+ message + '</p>';
		}

		if (window.addEventListener) {
			window.addEventListener("unload", comet.onUnload, false);
		} else if (window.attachEvent) {
			window.attachEvent("onunload", comet.onUnload);
		}
	</script>
</body>
</html>