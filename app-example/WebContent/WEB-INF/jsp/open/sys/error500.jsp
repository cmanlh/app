<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>ÏµÍ³´íÎó</title>
<style type="text/css">
.container {
	position: absolute;
	top: 15%;
	left: 45%;
}

.face {
	width: 220px;
	height: 220px;
	border: 4px solid #E56529;
	border-radius: 110px;
}

.leftEye, .rightEye {
	width: 65px;
	height: 3px;
	background-color: #E56529;
	position: absolute;
	top: 85px;
}

.leftEye {
	left: 35px;
}

.rightEye {
	right: 15px;
}

.mouth {
	width: 25px;
	height: 50px;
	position: absolute;
	top: 135px;
	left: 125px;
	border-radius: 50px;
	border: 4px solid #E56529;
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
		<div class="face"></div>
		<div class="leftEye"></div>
		<div class="rightEye"></div>
		<div class="mouth"></div>
		<div class="desc">
			系统发生错误<a href="<%=request.getContextPath()%>/index.do">请尝试返回首页</a>
		</div>
	</div>
</body>
</html>