package com.aws.model;

public class User
{
  private int userId;
  private String userName;

  public String getUserName()
  {
    return this.userName;
  }
  public void setUserName(String userName) {
    this.userName = userName;
  }
  public int getUserId() {
    return this.userId;
  }
  public void setUserId(int userId) {
    this.userId = userId;
  }
}