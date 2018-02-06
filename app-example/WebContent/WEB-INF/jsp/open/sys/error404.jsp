<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>找不到相应资源</title>
<style type="text/css">
.container {
	position: absolute;
	top: 18%;
	left: 48%;
}

.bigCircle {
	border-radius: 100%;
	border: 2px solid #E56529;
	width: 165px;
	height: 165px;
}

.smallCircle {
	border-radius: 100%;
	border: 2px solid #E56529;
	width: 145px;
	height: 145px;
	position: absolute;
	top: 10px;
	left: 10px;
}

.bridge {
	border: 2px solid #E56529;
	width: 9px;
	height: 26px;
	position: absolute;
	left: 78px;
	top: 167px;
}

.handler {
	height: 142px;
	border: 3px solid rgb(229, 101, 41);
	width: 15px;
	border-radius: 9px;
	position: absolute;
	top: 185px;
	left: 74px;
	background-color: #FFFFFF;
}

.line01, .line02, .line03, .line04 {
	width: 105px;
	height: 4px;
	background-color: #D8D8D8;
	border: 1px solid #979797;
	border-radius: 4px;
	position: absolute;
}

.line02 {
	top: 15px;
}

.line03 {
	top: 30px;
}

.line04 {
	top: 45px;
}

.desc {
	position: absolute;
	top: 320px;
	width: 300px;
	font-size: 18px;
	color: #4C4C4C;
}

.desc a, .desc a:VISITED {
	color: #488BDE;
	text-decoration: none;
}

.desc a:HOVER {
	color: #E56529;
}
</style>
</head>
<body>
	<div class="container">
		<div style="transform: rotate(-45deg);">
			<div class="bigCircle"></div>
			<div class="smallCircle"></div>
			<div class="bridge"></div>
			<div class="handler"></div>
			<div style="position: absolute; top: 32px; left: 65px; transform: rotate(45deg);">
				<div class="line01"></div>
				<div class="line02"></div>
				<div class="line03"></div>
				<div class="line04"></div>
			</div>
		</div>
		<div class="desc">
			找不到相应页面 <a href="<%=request.getContextPath()%>/index.do">请返回首页</a>
		</div>
	</div>
</body>
</html>