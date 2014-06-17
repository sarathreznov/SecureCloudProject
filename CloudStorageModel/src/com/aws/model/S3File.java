package com.aws.model;

import java.util.List;

public class S3File
{
  private int id;
  private String fileName;
  private String bucketName;
  private String keysBucket;
  private String encrType;
  private List<String> userList;

  public String getFileName()
  {
    return this.fileName;
  }
  public String getKeysBucket() {
    return this.keysBucket;
  }
  public void setKeysBucket(String keysBucket) {
    this.keysBucket = keysBucket;
  }
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }
  public String getBucketName() {
    return this.bucketName;
  }
  public void setBucketName(String bucketName) {
    this.bucketName = bucketName;
  }
  public String getEncrType() {
    return this.encrType;
  }
  public void setEncrType(String encrType) {
    this.encrType = encrType;
  }
  public List<String> getUserList() {
    return this.userList;
  }
  public void setUserList(List<String> userList) {
    this.userList = userList;
  }
  public int getId() {
    return this.id;
  }
  public void setId(int id) {
    this.id = id;
  }
}