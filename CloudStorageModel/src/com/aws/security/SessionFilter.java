package com.aws.security;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(filterName="/SessionFilter", urlPatterns={"/admin/*"})
public class SessionFilter
  implements Filter
{
  public SessionFilter()
  {
    System.out.println("Into the filter");
  }

  public void destroy()
  {
  }

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
    throws IOException, ServletException
  {
    HttpServletRequest req = (HttpServletRequest)request;
    HttpSession session = req.getSession(false);
    if ((session != null) && (session.getAttribute("user") != null)) {
      chain.doFilter(request, response);
    } else {
      HttpServletResponse res = (HttpServletResponse)response;
      res.sendRedirect("../index.jsp");
    }
  }

  public void init(FilterConfig fConfig)
    throws ServletException
  {
  }
}