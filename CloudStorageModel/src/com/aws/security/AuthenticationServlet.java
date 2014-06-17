package com.aws.security;

import com.aws.model.User;
import com.aws.util.RDSDao;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet({"/AuthenticationServlet"})
public class AuthenticationServlet extends HttpServlet
{
  private static final long serialVersionUID = 1L;

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    String userName = request.getParameter("username");
    String password = request.getParameter("password");

    System.out.println("Username:" + userName + " pass:" + password);
    RDSDao dao = RDSDao.getInstance();
    try {
      User user = dao.getUser(userName, password);

      if (user != null) {
        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        response.sendRedirect("admin/encryption.jsp");
      } else {
        response.sendRedirect("error.jsp");
      }
    } catch (Exception e) {
      response.sendRedirect("error.jsp");
    }
  }
}