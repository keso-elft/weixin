<!DOCTYPE html>
<html lang="cn">
<head>
<meta charset="utf-8">
<title>公众平台 · 频道</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="owen">
<meta name="keywords" content="公众平台 , 微信">
<!-- Le styles -->
<link href="css/bootstrap.css" rel="stylesheet">
<link href="css/bootstrap-responsive.css" rel="stylesheet">
<link href="css/docs.css" rel="stylesheet">
<!-- Le javascript -->
<script src="js/jquery-1.8.1.min.js"></script>
<script src="js/bootstrap.js"></script>
</head>
<body data-spy="scroll" data-target=".bs-docs-sidebar">
	<!-- 导航条 -->
	<div class="navbar navbar-inverse navbar-fixed-top">
		<div class="navbar-inner">
			<div class="container">
				<button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
					<span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
				</button>
				<a class="brand" href="index.jsp">Keso.elft</a>
				<div class="nav-collapse collapse">
					<ul class="nav">
						<li class=""><a href="index.jsp">主页</a></li>
						<li class="active"><a href="channel.jsp">频道</a></li>
						<li class=""><a href="arrange.jsp">发起</a></li>
						<li class=""><a href="#about">关于</a></li>
						<li class=""><a href="#contact">联系我</a></li>
					</ul>
				</div>
			</div>
		</div>
	</div>

	<!-- 登录框 -->
	<div class="modal hide fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			<h3 id="myModalLabel">进入频道</h3>
		</div>
		<div class="modal-body">
			<form class="form-horizontal">
				<div class="control-group">
					<label class="control-label" for="channelId">频道号</label>
					<div class="controls">
						<input type="text" id="channelId" placeholder="请输入频道ID" autofocus>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label" for="password">密码</label>
					<div class="controls">
						<input type="password" id="password" placeholder="请输入频道密码">
					</div>
				</div>
			</form>
		</div>
		<div class="modal-footer">
			<button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
			<button class="btn btn-primary" id="userInfo" data-dismiss="modal" aria-hidden="true" onclick="connect()">登录</button>
		</div>
	</div>

	<!-- 主界面 -->
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span3 bs-docs-sidebar">
				<section>
					<div class="well sidebar-nav">
						<ul class="nav nav-list" id="user_list">
							<li class="nav-header">Online People</li>
						</ul>
					</div>
					<center>
						<a href="#myModal" role="button" class="btn btn-primary" data-toggle="modal">登录ChannelRoom</a>
					</center>
				</section>
			</div>
			<div class="span9">
				<section>
					<fieldset class="well">
						<p class="head text-success">[+] 欢迎来到ChannelRoom!</p>
						<hr>
						<div class="text-info" id="chatlog" style="height: 350px; overflow: auto;"></div>
					</fieldset>
				</section>
			</div>
		</div>
	</div>
	<footer class="footer">
		<div class="container">
			<p>© keso.elft@gmail.com 2013 Based on HTML 5</p>
		</div>
	</footer>
	<script type="text/javascript">
		var server = 'comet';

		var comet = {
			connection : false,
			iframediv : false,

			initialize : function(channelId, password) {

				if (comet.connection) {
					comet.connection = false;
					$('#comet_iframe').remove();
				}

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

				} else if (navigator.appVersion.indexOf("KHTML") != -1
						|| navigator.userAgent.indexOf('Opera') >= 0) {
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
					comet.iframediv.setAttribute('onLoad',
							'comet.frameDisconnected()');
					comet.iframediv.setAttribute('src', server);
					comet.connection.appendChild(comet.iframediv);
					document.body.appendChild(comet.connection);
				}

				return comet.connection;
			},
			frameDisconnected : function() {
				comet.connection = false;
				$('#comet_iframe').remove();
				//setTimeout("chat.showConnect();",100);
			},
			//添加正常消息
			newMessage : function(data) {
				writeToChatLog(data, 'text-info');
			},

			//退出
			onUnload : function() {
				if (comet.connection) {
					comet.connection = false;
					$('#comet_iframe').remove();
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