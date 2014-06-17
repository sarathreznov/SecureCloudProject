<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="com.aws.model.*,java.util.*,com.aws.util.RDSDao" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="../css/css-frames-style.css" type="text/css">
<title>Cloud Storage</title>
<script type="text/javascript">
function downloadfiles(obj){
	var objId= obj.id;
	alert('hi download starts'+objId);
	document.getElementById(objId).href='/FileDownloadServlet?fileName='+objId;
	alert('link now:'+obj.href);
	return;
}
function validate(){
	if(document.getElementById("share_user").value=='')
		return false;
	else
		return true;
}
</script>
</head>

<body style="overflow:auto;">
<div id="content">
<div class="paddingwrap">
<p style="text-align: center; font-weight: bold; font-family: 'Bree Serif'; color: #107710; font-size: xx-large;">
			BROWSE S3 FILES</p>
			<br/>
	<div align="center">
			<form action="../ShareFilesServlet" method="post" id="myform" onclick="validate()">
			<div align="center" style="width:650px;height:280px;border:1px solid #000;" >
			<div align="center">
			  <table cellpadding="15" cellspacing="1" >
			    <tr>
			      <th>S.No</th>
			      <th>File Name</th>
			      <th>Download</th>
			      <th>Share</th>
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
					out.println("<td><a href='../FileDownloadServlet?fileName="+file.getFileName()+"'><img src='../images/download.png' id='"+file.getFileName()+"' alt='download' height='20' width='20'/></a></td>");
					out.println("<td><input type='checkbox' name='share_files' value='"+file.getFileName()+"'></td>");
					out.println("</tr>");
					i++;
				}
				if(files.size()!=0){
					out.println("<tr>");
					out.println("<td></td><td><input name='share_btn' type='submit' value='share'/></td>");
					out.println("</tr>");
				}
				%>
		      </table>
			  </div>
			<div id="share">
			  
			  <div align="left">
			    <p>
			      <%
			dao= RDSDao.getInstance();
			List<String> userName = new ArrayList<String>();
			userName= dao.getShareUsers(user.getUserName());
			if(files.size()!=0){
				out.println("Choose the name of the user to share :");
				out.println("<select name='share_user'>");
				for(String userList:userName){
					out.println("<option value="+userList+">"+userList +"</option>");
				}
				out.println("</select>");
			}
			%>
		        </p>
			    <p>&nbsp;</p>
			  </div>
			  </div>
			</div>
            <br/>
            
				<div align="left">
				  <%
			if(request.getParameter("status")!=null){
				String status = request.getParameter("status");
				if(status!=null){
					out.println("<center><div><label style='color: red;'>Object shared successfully</label></div></center>");
				}
			}
			%>
			  </div>
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
<li><a href="#">Browse Files</a></li>
<li><a href="auditfiles.jsp">Audit Files Request</a></li>
<li><a href="../index.jsp?status=logout">Logout</a>
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
    <div>
      <p>Implement the logic  to allow the users to forward the data to others.</p>
      <p>Inform the other  users about the same using Amazon SNS. </p>
    </div>
  </div>
</div>
<div id="banner2"><a href="#"><img src="../images/ClouStorage.jpg"  align="absmiddle"     width="500" height="350"  alt="" /></a></div>
</body>
</html>