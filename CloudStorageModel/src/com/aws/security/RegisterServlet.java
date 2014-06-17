package com.aws.security;

import com.aws.util.RDSDao;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet({"/RegisterServlet"})
public class RegisterServlet extends HttpServlet
{
  private static final long serialVersionUID = 1L;

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    String userName = request.getParameter("username");
    String password = request.getParameter("password");

    RDSDao dao = RDSDao.getInstance();
    if (!dao.registerUser(userName, password))
      response.sendRedirect("index.jsp?status=success");
    else
      response.sendRedirect("register_user.jsp?status=failure");
  }
}