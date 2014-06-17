<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="../css/css-frames-style.css" type="text/css">
<title>Cloud Storage</title>
<style type="text/css">
#sidebar-left .paddingwrap ul li a {
	font-family: "Bree Serif";
}
</style>
</head>

<body style="overflow:auto;">

<div id="content">

<div class="paddingwrap">
<div align="right"></div>
<p style="text-align: center; font-weight: bold; font-family: 'Bree Serif'; font-size: xx-large; color: #107710;">ENCRYPTED UPLOAD</p>
<br/>
		<form action="ServerEncryptUploadServlet" enctype="multipart/form-data" method="post" id="myform">
  <div align="center">
			<div align="center" style="width:650ppx; height:180px; border-width: 1px; border-color: background; color: #000; font-family: 'Bree Serif'; font-weight: bold;">
			<table cellpadding="15" cellspacing="1">
				<tr>
					<td>Upload File : </td>
					<td><input name="file" type="file" /></td>
				</tr>
				<tr>
				<td>Encryption Type : </td> 
				<td>
					<input type="radio" name="encrType" value="server" checked="checked" onclick="keyStore.disabled=true">Server Side Encryption 
					<input type="radio" name="encrType" value="client" onclick="keyStore.disabled=false">Client Side Encryption
				</td>
				</tr>
				<tr>
					<td/>
					<td><input name="server_upload" type="submit" value="Encrypted Upload""/></td>
				</tr>
			</table>
			</div>
	    </div>
			<br/>
			<br/>
			<%
			if(request.getParameter("status")!=null){
				String status = request.getParameter("status");
				if(status!=null){
					out.println("<center><div><label style='color: red;'>Object uploaded successfully</label></div></center>");
				}
			}
			%>
		</form>
</div><!-- close div.paddingwrap -->
</div>
<!-- close div#content -->

<div id="sidebar-left">
<div class="paddingwrap">
<p>&nbsp;</p>
  <p>&nbsp;</p>
  <p>&nbsp;</p>
<ul>
<li><a href="#">Encrypted Upload</a></li>
<li><a href="browsefiles.jsp">Browse Files</a></li>
<li><a href="auditfiles.jsp">Audit Files Request</a></li>
<li><a href="../index.jsp?status=logout">Logout</a>
</ul>
</div><!-- close div.paddingwrap --><!-- close div#sidebar-left -->
</div>
<div id="menu-wrapper">
</div>
<div id="banner"><a href="#"><img src="../images/img01.jpg" width="1200" height="300" alt="" /></a></div>
<div id="banner">
  <p>A SECURE CODE-BASED PUBLIC CLOUD STORAGE SYSTEM </p>
  <p>WITH SECURE DATA FORWARDING </p>
</div>
<div id="banner1" >
  <div class="justie">
  <p class="justie">Implement Encryption algorithm for the users to generate their own random AES encryption keys    </p>
  <p class="justie">Implement the code to encrypt the data and store it in S3    </p>
  <p class="justie"> Implement the server side encryption algorithm in S3 </p>
</div>
</div>
<div id="banner2"><a href="#"><img src="../images/ClouStorage.jpg"  align="absmiddle"     width="500" height="350"  alt="" /></a></div>
<div id="banner">
</body>
</html>