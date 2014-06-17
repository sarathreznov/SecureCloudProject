<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Login</title>

<link href="css/login-box.css" rel="stylesheet" type="text/css" />
</head>

<body bgcolor="#7DB9FA">
<form action="AuthenticationServlet" method="post">
<div style="padding: 100px 0 0 250px;">
<div id="login-box">

<h2>Cloud Storage</h2>
Login with your registered credentials
<br />
<br />
<div id="login-box-name" style="margin-top:20px;">User Name:</div><div id="login-box-field" style="margin-top:20px;"><input name="username" class="form-login" title="Username" value="" size="30" maxlength="2048" /></div>
<div id="login-box-name">Password:</div><div id="login-box-field"><input name="password" type="password" class="form-login" title="Password" value="" size="30" maxlength="2048" /></div>
<br />
<br />
<br />
<a href="#"><img src="images/login-btn.png" width="103" height="42" style="margin-left:90px;" onclick="submit();"/></a>
<br/>
<a href="register_user.jsp">New User?</a>
</div>

</div>
<%
			if(request.getParameter("status")!=null){
				String status = request.getParameter("status");
				if(status!=null){
					if(status.equals("success"))
						out.println("<center><div><label style='color: red;'>User registered successfully</label></div></center>");
					else if(status.equals("logout"))
						out.println("<center><div><label style='color: red;'>User logged out successfully</label></div></center>");
				}
			}
			%>

</form>
</body>
</html>