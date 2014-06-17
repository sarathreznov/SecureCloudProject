<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="com.aws.model.*,java.util.*,com.aws.util.RDSDao" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="../css/css-frames-style.css" type="text/css">
<title>Cloud Storage</title>
</head>

<body style="overflow:auto;">
<div id="content">
<div class="paddingwrap">
<p style="text-align: center; font-weight: bold; color: #107710; font-size: xx-large; font-family: 'Bree Serif';">AUDIT S3 FILES</p>
			<br/>
	<div align="center">
			<form action="../AuditFilesServlet" method="post" id="myform">
			<div ali align="center" style="width:650px; height:280px; border:1px solid #000; font-family: 'Bree Serif';" >
			<table cellpadding="15" cellspacing="1" >
			<tr>
			<th>S.No</th>
			<th>File Name</th>
			<th>Choose to audit</th>
			</tr>
			<%
				User user = (User)session.getAttribute("user");
				int userId = user.getUserId();
				RDSDao dao = RDSDao.getInstance();
				List<S3File> files = dao.getUserFiles(userId);
				int i=1;
				for(S3File file:files){
					out.println("<tr>");
					out.println("<td>"+i +"</td>");
					out.println("<td>"+file.getFileName() +"</td>");
					out.println("<td><input type='checkbox' name='audit_files' value='"+file.getFileName()+"'></td>");
					out.println("</tr>");
					i++;
				}
				out.println("<tr>");
				out.println("<td></td><td><input name='audit_btn' type='submit' value='Request Auditing'/></td>");
				%>
				
			</table>
			<div id="share">
			
			<%
			dao= RDSDao.getInstance();
			List<String> userName = new ArrayList<String>();
			userName= dao.getShareUsers(user.getUserName());
			%>
			Choose the name of the user to share :
			<select name="share_user">
			<%for(String userList:userName){ %>
					<option value="<%=userList%>"><%=userList%></option>
			<%} %>
			</select> 
			</div>
			</div>
				<%
			if(request.getParameter("status")!=null){
				String status = request.getParameter("status");
				if(status!=null){
					out.println("<center><div><label style='color: red;'>Auditing request submitted!</label></div></center>");
				}
			}
			%>
			</form>
	</div>
</div>
</div>
<div id="sidebar-left">
<div class="paddingwrap">
 <p>&nbsp;</p>
  <p>&nbsp;</p>
  <p>&nbsp;</p>
  <ul>
    <li><a href="encryption.jsp">Encrypted Upload</a></li>
    <li><a href="browsefiles.jsp">Browse Files</a></li>
    <li><a href="#">Audit Files Request</a></li>
    <li><a href="../index.jsp?status=logout">Logout</a>  </li>
  </ul>
</div><!-- close div.paddingwrap -->
</div><!-- close div#sidebar-left -->
<div id="menu-wrapper">
</div>
<div id="banner"><a href="#"><img src="../images/img01.jpg" width="1200" height="300" alt="" /></a></div>
    <div id="banner">
  <p>A SECURE CODE-BASED PUBLIC CLOUD STORAGE SYSTEM </p>
  <p>WITH SECURE DATA FORWARDING </p>
  
</div>
<div id="banner1" >
  <div class="justie">
  <p class="justie">To  ensure the correctness of data in public cloud storage ,we implement an <u>external  auditing</u> mechanism with third  party auditor (TPA) as the security process while storage</p>
</div>
</div>
<div id="banner2"><a href="#"><img src="../images/ClouStorage.jpg"  align="absmiddle"     width="500" height="350"  alt="" /></a></div>
</body>
</html>