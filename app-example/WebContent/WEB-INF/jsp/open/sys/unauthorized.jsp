<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>无权限</title>
<style type="text/css">
.container {
	position: absolute;
	top: 20%;
	left: 50%;
}

.line {
	width: 10px;
	height: 181px;
	background-color: #B0B0B0;
	border-radius: 8px;
}

.leftBorder {
	border-top: 36px solid #488BDE;
	width: 97px;
	border-left: 36px solid transparent;
	position: absolute;
	top: 31px;
	left: -75px;
}

.leftBorder:BEFORE {
	border-bottom: 36px solid #488BDE;
	position: absolute;
	border-left: 36px solid transparent;
	width: 97px;
	content: "";
	top: -36px;
	left: -36px;
}

.leftBorder:AFTER {
	border-left: 18px solid #488BDE;
	position: absolute;
	border-top: 18px solid transparent;
	border-bottom: 18px solid transparent;
	content: "";
	top: -36px;
	left: 97px;
}

.rightBorder {
	border-top: 36px solid #E56529;
	width: 123px;
	border-right: 36px solid transparent;
	position: absolute;
	top: 83px;
	left: -76px;
}

.rightBorder:AFTER {
	border-bottom: 36px solid #E56529;
	position: absolute;
	border-right: 36px solid transparent;
	width: 123px;
	content: "";
	top: -36px;
}

.rightBorder:BEFORE {
	border-right: 18px solid #E56529;
	position: absolute;
	border-top: 18px solid transparent;
	border-bottom: 18px solid transparent;
	content: "";
	top: -36px;
	left: -18px;
}

.login {
	position: absolute;
	top: 40px;
	left: -15px;
	font-size: 14px;
	color: #FFFFFF;
	width: 100px;
}

.contact {
	position: absolute;
	top: 93px;
	left: -38px;
	font-size: 14px;
	color: #FFFFFF;
	width: 100px;
}

.desc {
	font-size: 18px;
	position: absolute;
	font-size: 18px;
	position: absolute;
	width: 500px;
	top: 231px;
	left: -150px;
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
<script type="text/javascript"
	src="http://wm.orientsec.com.cn/js/jquery.js"></script>
<script type="text/javascript">
	$(function() {
		$.ajax({
			url : '../../open/user/info',
			dataType : 'json',
			success : function(data) {
				if ((data || {}).result) {
					$('#alreadyLogin').show().find('#loginUser').html(data.result.displayName);
				} else {
					$('#noLogin').show();
				}
			},
			error : function(data) {
				$('#noLogin').show();
			}
		});
	});

	function loginOut() {
		$.get('../../open/logout', function() {
			location.href = '../../index.do';
		});
	}
</script>
<body>
	<div class="container">
		<div class="line"></div>
		<div class="leftBorder"></div>
		<div class="rightBorder"></div>
		<div class="login">登 录</div>
		<div class="contact">联系管理员</div>
		<div class="desc">
			<div id="noLogin" style="display: none;">
				无权限访问该系统。请<a href="http://www.orientsec.com.cn/wps/myportal"> 登录
				</a>或<a href="<%=request.getContextPath()%>/index.do"> 尝试返回首页</a>
			</div>
			<div id="alreadyLogin" style="display: none;">
				当前登录用户为 <strong id="loginUser"></strong>, 请确认是否有权限访问此页面<br /> 请<a
					href="javascript:loginOut()"> 注销 </a>或<a
					href="http://www.orientsec.com.cn/wps/myportal"> 重新登录 </a>
			</div>
		</div>
	</div>
</body>
</html>