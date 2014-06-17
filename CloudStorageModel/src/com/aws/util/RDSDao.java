package com.aws.util;

import com.aws.model.S3File;
import com.aws.model.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RDSDao
{
  public static final String URL = "jdbc:mysql://project.cfhb7sbu6azd.ap-southeast-1.rds.amazonaws.com:3306/";
  public static final String userName = "test";
  public static final String password = "password";
  public static final String DB_NAME = "projectdb";
  public static final String driver = "com.mysql.jdbc.Driver";
  public static final String UPLOAD_BUCKET_NAME = "sample_bucket_aws";
  public static final String UPLOAD_KEYS_BUCKET_NAME = "sample_keys_bucket_aws";
  private static RDSDao dao;
  private Connection connection;

  public static RDSDao getInstance()
  {
    if (dao == null)
      dao = new RDSDao();
    return dao;
  }

  private RDSDao() {
    try {
      Class.forName("com.mysql.jdbc.Driver");
      this.connection = DriverManager.getConnection("jdbc:mysql://project.cfhb7sbu6azd.ap-southeast-1.rds.amazonaws.com:3306/projectdb", "test", "password");
      System.out.println(this.connection.getMetaData());
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  public void createTable() {
    boolean isCreated = false;
    try {
      Statement stmt = this.connection.createStatement();
      String query = "CREATE TABLE aws_user (id INTEGER not NULL auto_increment,  username VARCHAR(255),  password VARCHAR(255),  fileslist VARCHAR(255),  encrType VARCHAR(255),  bucketname VARCHAR(255),  keysbucket VARCHAR(255),  PRIMARY KEY ( id ))";

      stmt.executeUpdate(query);
      System.out.println("Query:" + query);
      isCreated = stmt.execute(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public boolean registerUser(String userName, String password) {
    boolean isInserted = false;
    try {
      Statement stmt = this.connection.createStatement();
      String query = "insert into aws_user (username,password) values ('" + userName + "','" + password + "')";
      System.out.println("Query:" + query);
      isInserted = stmt.execute(query);
      System.out.println("Is inserted:" + isInserted);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return isInserted;
  }

  public User getUser(String userName, String password) {
    User user = null;
    try {
      Statement stmt = this.connection.createStatement();
      String query = "select * from aws_user where username='" + userName + "' and password='" + password + "'";
      System.out.println("Query:" + query);
      ResultSet rs = stmt.executeQuery(query);

      if (rs != null)
        while (rs.next()) {
          user = new User();
          int id = rs.getInt("id");
          System.out.println("id" + id);
          user.setUserId(id);
          user.setUserName(userName);
        }
    }
    catch (SQLException e) {
      e.printStackTrace();
    }
    return user;
  }

  public List<S3File> getUserFiles(int userId)
  {
    List files = null;
    try {
      Statement stmt = this.connection.createStatement();
      String query = "select * from aws_user_file where uid=" + userId;
      System.out.println("Query:" + query);
      ResultSet rs = stmt.executeQuery(query);

      if (rs != null) {
        files = new ArrayList();
        while (rs.next()) {
          S3File s3file = new S3File();

          int id = rs.getInt("id");
          String fileName = rs.getString("filename");
          String encryptionType = rs.getString("encrType");
          String bucketName = rs.getString("bucketname");
          String keysBucket = rs.getString("keysBucket");

          s3file.setId(id);
          s3file.setFileName(fileName);
          s3file.setBucketName(bucketName);
          s3file.setEncrType(encryptionType);
          if ((encryptionType != null) && (encryptionType.equals("client"))) {
            s3file.setKeysBucket(keysBucket);
          }
          files.add(s3file);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return files;
  }

  public String getUploadedFilesList(int userId) {
    String filesList = null;
    try {
      Statement stmt = this.connection.createStatement();
      String query = "select fileslist from aws_user where id=" + userId;
      ResultSet rs = stmt.executeQuery(query);

      if (rs != null)
        while (rs.next())
          filesList = rs.getString("fileslist");
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    return filesList;
  }

  public boolean uploadFiles(int userId, String fileName, String encryptionType) {
    boolean isInsertSuccess = false;
    try {
      Statement stmt = this.connection.createStatement();
      String query = "insert into aws_user_file (uid,filename,encrType,bucketname,keysBucket) values(" + userId + ",'" + fileName + "','" + encryptionType + "','" + "sample_bucket_aws" + "','" + "sample_keys_bucket_aws" + "')";

      System.out.println("Query:" + query);
      isInsertSuccess = stmt.execute(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return isInsertSuccess;
  }

  private Map<String, Integer> getUserDetails() throws SQLException {
    Map userMap = new HashMap();
    Statement stmt = this.connection.createStatement();
    String query = "select id,username from aws_user";

    ResultSet rs = stmt.executeQuery(query);

    if (rs != null) {
      while (rs.next()) {
        String username = rs.getString("username");
        int id = rs.getInt("id");
        userMap.put(username, Integer.valueOf(id));
      }
    }
    return userMap;
  }
  public boolean updateShareFileDetails(String userName, String fileName, String encrType) {
    boolean isUpdateSuccess = false;
    try {
      Map userMap = getUserDetails();
      int userId = ((Integer)userMap.get(userName)).intValue();
      isUpdateSuccess = uploadFiles(userId, fileName, encrType);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    System.out.println("hi:" + isUpdateSuccess);
    return isUpdateSuccess;
  }
  public S3File getFileDetails(String fileName, int userId) {
    S3File s3file = null;
    try {
      Statement stmt = this.connection.createStatement();
      String query = "select * from aws_user_file where uid=" + userId + " and filename ='" + fileName + "'";
      System.out.println("Query:" + query);
      ResultSet rs = stmt.executeQuery(query);

      if (rs != null)
        while (rs.next()) {
          s3file = new S3File();
          String encryptionType = rs.getString("encrType");
          String bucketName = rs.getString("bucketname");
          String keysBucket = rs.getString("keysBucket");

          s3file.setBucketName(bucketName);
          s3file.setEncrType(encryptionType);
          s3file.setFileName(fileName);

          if (encryptionType.equals("client"))
            s3file.setKeysBucket(keysBucket);
        }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    return s3file;
  }

  public void executeQuery(String query) {
    try {
      Statement stmt = this.connection.createStatement();
      ResultSet rs = stmt.executeQuery("describe aws_user");

      ResultSetMetaData meta = rs.getMetaData();
      int columnCount = meta.getColumnCount();
      for (int i = 1; i <= columnCount; i++) {
        System.out.println(meta.getColumnName(i));
      }
      while (rs.next())
        System.out.println(rs.getString(1) + " " + rs.getString(2));
    }
    catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public List<String> getShareUsers(String userName) {
    List users = null;
    try {
      Statement stmt = this.connection.createStatement();
      ResultSet rs = stmt.executeQuery("select username from aws_user where username!='" + userName + "'");

      if (rs != null)
        users = new ArrayList();
      while (rs.next()) {
        String user = rs.getString("username");
        users.add(user);
        System.out.println(user);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return users;
  }

  public static void main(String[] args) {
    RDSDao dao = new RDSDao();

    dao.executeQuery("");
  }
}