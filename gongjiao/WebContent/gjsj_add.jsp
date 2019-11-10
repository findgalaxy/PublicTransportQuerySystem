<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<html>
<head>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/common.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/main.css" />
<script type="text/javascript">
	function addCity() {
		window
				.open(
						"zhuanye_add.jsp",
						"newwindow",
						"height=600, width=800, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no");

	}
</script>
</head>

<body>

	<!--/sidebar-->
	<div class="main-wraps">

		<div class="crumb-wrap">
			<div class="crumb-list">
				<i class="icon-font"></i>首页<span class="crumb-step">&gt;</span>公交司机<span
					class="crumb-step">&gt;</span><span>新增</span>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			</div>
		</div>
		<div class="result-wrap">
			<div class="result-content">


				<table class="insert-tab" width="100%" style="margin-top: 100dx;">
					<tbody>
						<form action="${pageContext.request.contextPath}/main/addgjsj"
							method="post">
							<tr>
								<th><i class="require-red"></i>姓名：</th>
								<td><input class="common-text required" id="gjsj.nickname"
									name="gjsj.nickname" size="50" value="" type="text"></td>
							</tr>
							<tr>
								<th><i class="require-red"></i>账号：</th>
								<td><input class="common-text required" id="gjsj.username"
									name="gjsj.username" size="50" value="" type="text"></td>
							</tr>
						<tr>
								<th><i class="require-red"></i>密码：</th>
								<td><input class="common-text required" id="gjsj.pass"
									name="gjsj.pass" size="50" value="" type="text"></td>
							</tr>
							
<tr>
								<th><i class="require-red"></i>公交线路：</th>
								<td><input class="common-text required" id="gjsj.xianlu"
									name="gjsj.luxian" size="50" value="" type="text"></td>
							</tr>
							
							<tr>
								<th></th>
								<td><input class="btn btn-primary btn6 mr10" value="提交"
									type="submit"> <input class="btn btn6"
									onClick="history.go(-1)" value="返回" type="button"></td>
							</tr>
						</form>
					</tbody>
				</table>

			</div>

		</div>
	</div>

</body>
</html>
