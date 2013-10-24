<!DOCTYPE html>
<html lang="cn">
<head>
<meta charset="utf-8">
<title>公众平台 · 发起投票</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="owen">
<meta name="keywords" content="公众平台 , 微信">
<!-- Le styles -->
<link href="../css/bootstrap.css" rel="stylesheet">
<link href="../css/bootstrap-responsive.css" rel="stylesheet">
<link href="../css/docs.css" rel="stylesheet">
<!-- Le javascript -->
<script src="../js/jquery-1.8.1.min.js"></script>
<script src="../js/bootstrap.js"></script>
</head>
<body data-spy="scroll" data-target=".bs-docs-sidebar">
	<!-- 导航条 -->
	<div class="navbar navbar-inverse navbar-fixed-top">
		<div class="navbar-inner">
			<div class="container">
				<button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
					<span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
				</button>
				<a class="brand" href="../index.jsp">Keso.elft</a>
				<div class="nav-collapse collapse">
					<ul class="nav">
						<li class=""><a href="../index.jsp">主页</a></li>
						<li class=""><a href="../channel.jsp">频道</a></li>
						<li class=""><a href="../arrange.jsp">发起</a></li>
						<li class=""><a href="#about">关于</a></li>
						<li class=""><a href="#contact">联系我</a></li>
					</ul>
				</div>
			</div>
		</div>
	</div>

	<!-- 主界面 -->
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="well-small">
				<ul class="breadcrumb">
					<li><a href="../index.jsp">首页</a> <span class="divider">/</span></li>
					<li><a href="../arrange.jsp">发起</a> <span class="divider">/</span></li>
					<li class="active">投票</li>
				</ul>
			</div>
			<div class="well-large">
				<form class="form-inline">
					<div class="control-group">
						<label class="control-label" for="voteTitle"> 标题 </label>
						<div class="controls">
							<textarea id="voteTitle" rows="3" placeholder="请输入投票标题" autofocus></textarea>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="voteOptions"> 选项 </label>
						<div class="controls">
							<div class="input-append">
								<div class="btn-group">
									<button class="btn dropdown-toggle" data-toggle="dropdown">
										选项 <span class="caret"></span>
									</button>
									<ul class="dropdown-menu">
										<li class=""> 新增 </li>
										<li class=""> 修改 </li>
										<li class=""> 删除 </li>
									</ul>
								</div>
								<input class="span2" id="voteOptions" type="text">
							</div>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="voteAuthor"> 发起人 </label>
						<div class="controls">
							<input type="text" id="voteAuthor" placeholder="请输入您的称呼">
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
	<footer class="footer">
		<div class="container">
			<p>© keso.elft@gmail.com 2013 Based on HTML 5</p>
		</div>
	</footer>
</body>