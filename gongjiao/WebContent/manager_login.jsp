<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<HTML>
<HEAD>
<TITLE>管理员登录</TITLE>
<LINK href="${pageContext.request.contextPath}/css/Default.css" type=text/css rel=stylesheet>
<LINK href="${pageContext.request.contextPath}/css/xtree.css" type=text/css rel=stylesheet>
<LINK href="${pageContext.request.contextPath}/css/User_Login.css" type=text/css rel=stylesheet>
<script type="text/javascript">

function panduan() {
	  if(self.frameElement.tagName=="IFRAME"){
		  var s = window.location.href;
		  window.parent.location.href = s;
	  }
  }

</script>
</HEAD>
<BODY id=userlogin_body onload="panduan()">
	<DIV id=user_login>
		<DL>
			<DD id=user_top>
				<UL>
					<LI class=user_top_l></LI>
					<LI class=user_top_c></LI>
					<LI class=user_top_r></LI>
				</UL>
				<form action="${pageContext.request.contextPath}/manager/logins" method="post">
			<DD id=user_main>
				<UL>
					<LI class=user_main_l></LI>
					<LI class=user_main_c>
						<DIV class=user_main_box>
							<UL>
								<LI class=user_main_text>用户名：</LI>
								<LI class=user_main_input><INPUT class=TxtUserNameCssClass
									id=username maxLength=20 name=username></LI>
							</UL>
							<UL>
								<LI class=user_main_text>密 码：</LI>
								<LI class=user_main_input><INPUT class=TxtPasswordCssClass
									id=pass type=password name=pass></LI>
							</UL>
						</DIV>
					</LI>
					<LI class=user_main_r><INPUT class=IbtnEnterCssClass
						id=IbtnEnter
						style="BORDER-TOP-WIDTH: 0px; BORDER-LEFT-WIDTH: 0px; BORDER-BOTTOM-WIDTH: 0px; BORDER-RIGHT-WIDTH: 0px"
						onclick='javascript:WebForm_DoPostBackWithOptions(new WebForm_PostBackOptions("IbtnEnter", "", true, "", "", false, false))'
						type=image src="${pageContext.request.contextPath}/images/user_botton.gif" name=IbtnEnter>
					</LI>
				</UL>
				</DD>
					</form>
			<DD id=user_bottom>
				<UL>
					<LI class=user_bottom_l></LI>
					<LI class=user_bottom_c><SPAN style="MARGIN-TOP: 40px">
					${text }</SPAN></LI>
					<LI class=user_bottom_r></LI>
				</UL>
			</DD>
		
		</DL>
	</DIV>
	<SPAN id=ValrUserName style="DISPLAY: none; COLOR: red"></SPAN>
	<SPAN id=ValrPassword style="DISPLAY: none; COLOR: red"></SPAN>
	<SPAN id=ValrValidateCode style="DISPLAY: none; COLOR: red"></SPAN>
	<DIV id=ValidationSummary1 style="DISPLAY: none; COLOR: red"></DIV>
	<DIV></DIV>
	
</BODY>
</HTML>
