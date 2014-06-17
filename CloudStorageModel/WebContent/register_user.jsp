<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="css/login-box.css" rel="stylesheet" type="text/css" />
<title>Register</title>
</head>
<body bgcolor="#7DB9FA">
<form action="RegisterServlet" method="post">
<div style="padding: 100px 0 0 250px;">
<div id="login-box">

<h2>Cloud Storage</h2>
Choose your credentials
<br />
<br />
<div id="login-box-name" style="margin-top:20px;">User Name:</div><div id="login-box-field" style="margin-top:20px;"><input name="username" class="form-login" title="Username" value="" size="30" maxlength="2048" /></div>
<div id="login-box-name">Password:</div><div id="login-box-field"><input name="password" type="password" class="form-login" title="Password" value="" size="30" maxlength="2048" /></div>
<div id="login-box-name">Re-enter password:</div><div id="login-box-field"><input name="password" type="password" class="form-login" title="Password" value="" size="30" maxlength="2048" /></div>
<br />
<br />
<br />
<a href="#"><img src="images/register.png" width="103" height="42" style="margin-left:90px;" onclick="submit();"/></a>
<br/>
</div>
<%
			if(request.getParameter("status")!=null){
				String status = request.getParameter("status");
				if(status!=null){
					out.println("<center><div><label style='color: red;'>User registration failure</label></div></center>");
				}
			}
			%>
</div>
</form>
</body>
</html>