<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>S3 Upload</title>
<script src="js/jquery.js"></script> 
<script src="js/jquery.form.js"></script>
<link rel="stylesheet" type="text/css" href="css/styles.css"/>
<script> 
        // wait for the DOM to be loaded 
        $(document).ready(function() { 
            // bind 'myForm' and provide a simple callback function 
            $('#myForm').ajaxForm(function() { 
                alert("File is uploaded!"); 
            }); 
        }); 
    </script>  
</head>
<body style="font-family:arial;" >
<div style="height: 100px;"></div>
<p style="text-align: center; font-weight: bold;">
			Server Encrypt Upload</p>
			<br/>
		<form action="ServerEncryptUploadServlet" enctype="multipart/form-data" method="post" id="myform">
		<div align="center">
			<div style="width:550px;height:180px;border:1px solid #000;">
			<table cellpadding="15" cellspacing="1">
				<tr>
					<td>Upload File : </td>
					<td><input name="file" type="file" /></td>
				</tr>
				<tr>
					<td>Key Value : </td>
					<td><input name="key" type="text" /></td>
				</tr>
				<tr>
					<td/>
					<td><input name="server_upload" type="submit" value="Server Encrypt Upload"" /></td>
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
</body>
</html>