package com.aws.s3.encrypt;

import com.aws.model.S3File;
import com.aws.model.User;
import com.aws.util.RDSDao;
import java.io.IOException;
import java.io.PrintStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet({"/ShareFilesServlet"})
public class ShareFilesServlet extends HttpServlet
{
  private static final long serialVersionUID = 1L;
  private RDSDao dao;

  public ShareFilesServlet()
  {
    this.dao = RDSDao.getInstance();
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    doPost(request, response);
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    String shareUser = request.getParameter("share_user");
    HttpSession sess = request.getSession();
    User user = (User)sess.getAttribute("user");
    System.out.println("share user:" + shareUser);

    String encrType = request.getParameter("encrType");

    String[] checkedFileNames = request.getParameterValues("share_files");

    for (String fileName : checkedFileNames) {
      S3File file = this.dao.getFileDetails(fileName, user.getUserId());
      this.dao.updateShareFileDetails(shareUser, fileName, file.getEncrType());
    }

    response.sendRedirect("admin/browsefiles.jsp?status=success");
  }
}