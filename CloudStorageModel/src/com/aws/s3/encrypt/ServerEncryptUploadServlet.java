package com.aws.s3.encrypt;

import com.aws.model.User;
import com.aws.util.RDSDao;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;

@WebServlet({"/ServerEncryptUploadServlet"})
public class ServerEncryptUploadServlet extends HttpServlet
{
  private static final long serialVersionUID = 1L;
  private String DOWNLOAD_LOCAL_DIR = "/home/ec2-user/temp/";
  private String RSA_KEYS_LOCAL_DIR = "/home/ec2-user/s3keys/";
  private RDSDao dao;

  public ServerEncryptUploadServlet()
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
    System.out.println("Hi into the post method");
    DiskFileUpload upload = new DiskFileUpload();
    try
    {
      List items = upload.parseRequest(request);
      Iterator iter = items.iterator();
      String encrType = null;
      String fileName = null;
      String uploadFileName = null;

      HttpSession sess = request.getSession();
      User user = (User)sess.getAttribute("user");

      int userId = user.getUserId();
      while (iter.hasNext()) {
        FileItem item = (FileItem)iter.next();
        if (item.isFormField()) {
          String name = item.getFieldName();
          if (name.equals("encrType")) {
            encrType = item.getString();
            System.out.println("name:" + name + " value: " + encrType);
          }
        } else {
          String source = item.getName();
          fileName = source.substring(source.lastIndexOf("\\") + 1);
          System.out.println(fileName);
          File downloadDir = new File(this.DOWNLOAD_LOCAL_DIR);
          if ((!downloadDir.exists()) || (!downloadDir.isDirectory()))
            downloadDir.mkdirs();
          uploadFileName = this.DOWNLOAD_LOCAL_DIR + fileName;

          File outfile = new File(uploadFileName);
          item.write(outfile);
        }
      }
      if ((fileName != null) && (encrType.equals("server"))) {
        System.out.println("File name:" + fileName + " encrType:" + encrType);

        S3Encryption serverEncrypt = new S3Encryption(false, null);
        if (serverEncrypt.doEncryptedPut(uploadFileName, fileName))
          this.dao.uploadFiles(userId, fileName, encrType);
      }
      else if ((fileName != null) && (encrType.equals("client"))) {
        System.out.println("File name:" + fileName + " encrType:" + encrType);

        S3Encryption clientEncrypt = new S3Encryption(true, this.RSA_KEYS_LOCAL_DIR + "/" + fileName);
        if (clientEncrypt.doClientSideEncrypt(uploadFileName, fileName))
          this.dao.uploadFiles(userId, fileName, encrType);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    response.sendRedirect("encryption.jsp?status=success");
  }
}