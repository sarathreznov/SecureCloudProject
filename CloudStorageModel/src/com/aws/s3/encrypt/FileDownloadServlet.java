package com.aws.s3.encrypt;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.aws.model.S3File;
import com.aws.model.User;
import com.aws.util.RDSDao;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet({"/FileDownloadServlet"})
public class FileDownloadServlet extends HttpServlet
{
  private static final long serialVersionUID = 1L;
  private String RSA_KEYS_LOCAL_DIR = "/home/ec2-user/s3keys/";
  private RDSDao dao;

  public FileDownloadServlet()
  {
    this.dao = RDSDao.getInstance();
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    String fileName = request.getParameter("fileName");
    HttpSession session = request.getSession();
    User user = (User)session.getAttribute("user");
    int userId = user.getUserId();
    S3ObjectInputStream in = null;
    System.out.println("filename:" + fileName);

    S3File file = this.dao.getFileDetails(fileName, userId);
    String encrType = file.getEncrType();
    String bucketName = file.getBucketName();
    if (encrType.equals("server")) {
      S3Client client = new S3Client(false, null);
      in = client.retrieveS3Object(fileName, bucketName);
    } else if (encrType.equals("client")) {
      String keysPath = this.RSA_KEYS_LOCAL_DIR + "/" + fileName;
      S3Client client = new S3Client(true, keysPath);
      in = client.retrieveS3Object(fileName, bucketName);
    }
    String ext = fileName.substring(fileName.indexOf(".") + 1, fileName.length());
    System.out.println("ext:" + ext);
    if (ext.equals("txt"))
      response.setContentType("text/plain");
    else if (ext.equals("pdf"))
      response.setContentType("application/pdf");
    else
      response.setContentType("application/force-download");
    response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
    OutputStream out = response.getOutputStream();

    byte[] buffer = new byte[4096];
    int length;
    while ((length = in.read(buffer)) > 0)
    {
     // int length;
      out.write(buffer, 0, length);
    }
    in.close();
    out.flush();
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
  }
}