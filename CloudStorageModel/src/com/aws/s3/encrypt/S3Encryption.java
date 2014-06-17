package com.aws.s3.encrypt;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3EncryptionClient;
import com.amazonaws.services.s3.model.CryptoConfiguration;
import com.amazonaws.services.s3.model.CryptoStorageMode;
import com.amazonaws.services.s3.model.EncryptionMaterials;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class S3Encryption
{
  private String bucketName = "sample_bucket_aws";
  private AmazonS3 s3client;

  public S3Encryption(boolean isClientSideEncryption, String keyStorePath)
  {
    try
    {
      ClassLoader loader = S3Encryption.class.getClassLoader();
      PropertiesCredentials propCred = new PropertiesCredentials(loader.getResourceAsStream("resources/AwsCredentials.properties"));

      if (isClientSideEncryption) {
        KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");
        keyGenerator.initialize(1024, new SecureRandom());
        KeyPair myKeyPair = keyGenerator.generateKeyPair();

        PrivateKey privateKey = myKeyPair.getPrivate();
        PublicKey publicKey = myKeyPair.getPublic();

        System.out.println("Private Key:" + getHexString(privateKey.getEncoded()));
        System.out.println("Public Key: " + getHexString(publicKey.getEncoded()));

        saveKeyPair(keyStorePath, myKeyPair);

        String myAccessKeyId = propCred.getAWSAccessKeyId();
        String mySecretKey = propCred.getAWSSecretKey();

        AWSCredentials credentials = new BasicAWSCredentials(myAccessKeyId, mySecretKey);

        CryptoConfiguration cryptoConfig = new CryptoConfiguration().withStorageMode(CryptoStorageMode.InstructionFile);
        EncryptionMaterials encryptionMaterials = new EncryptionMaterials(myKeyPair);

        this.s3client = new AmazonS3EncryptionClient(credentials, encryptionMaterials, cryptoConfig);
      } else {
        this.s3client = new AmazonS3Client(propCred);
      }
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static String getHexString(byte[] b) {
    String result = "";
    for (int i = 0; i < b.length; i++) {
      result = result + Integer.toString((b[i] & 0xFF) + 256, 16).substring(1);
    }
    return result;
  }

  public void saveKeyPair(String path, KeyPair keyPair) throws IOException {
    PrivateKey privateKey = keyPair.getPrivate();
    PublicKey publicKey = keyPair.getPublic();

    X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(
      publicKey.getEncoded());
    File keyFolder = new File(path);
    if ((!keyFolder.exists()) || (!keyFolder.isDirectory()))
      keyFolder.mkdirs();
    File pubKeyFile = new File(path + "/public.key");
    if (!pubKeyFile.exists())
      pubKeyFile.createNewFile();
    FileOutputStream fos = new FileOutputStream(pubKeyFile);
    fos.write(x509EncodedKeySpec.getEncoded());
    fos.close();

    PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
    File privKeyFile = new File(path + "/private.key");
    if (!privKeyFile.exists())
      privKeyFile.createNewFile();
    fos = new FileOutputStream(privKeyFile);
    fos.write(pkcs8EncodedKeySpec.getEncoded());
    fos.close();
  }

  public boolean doEncryptedPut(String uploadFileName, String keyName) {
    boolean uploadStatus = false;
    try {
      File file = new File(uploadFileName);
      PutObjectRequest putRequest = new PutObjectRequest(this.bucketName, keyName, file);

      ObjectMetadata objectMetadata = new ObjectMetadata();
      objectMetadata.setServerSideEncryption("AES256");
      putRequest.setMetadata(objectMetadata);

      PutObjectResult response = this.s3client.putObject(putRequest);
      uploadStatus = true;
      System.out.println("Uploaded object encryption status is " + 
        response.getServerSideEncryption());
    } catch (AmazonServiceException ase) {
      System.out.println("Caught an AmazonServiceException, which means your request made it to Amazon S3, but was rejected with an error response for some reason.");

      System.out.println("Error Message:    " + ase.getMessage());
      System.out.println("HTTP Status Code: " + ase.getStatusCode());
      System.out.println("AWS Error Code:   " + ase.getErrorCode());
      System.out.println("Error Type:       " + ase.getErrorType());
      System.out.println("Request ID:       " + ase.getRequestId());
    } catch (AmazonClientException ace) {
      System.out.println("Caught an AmazonClientException, which means the client encountered an internal error while trying to communicate with S3, such as not being able to access the network.");

      System.out.println("Error Message: " + ace.getMessage());
      ace.getStackTrace();
    }
    return uploadStatus;
  }

  public boolean doClientSideEncrypt(String uploadFileName, String keyName) {
    boolean uploadStatus = false;
    try {
      System.out.println("Client side encryption starts..");
      File file = new File(uploadFileName);
      PutObjectRequest putRequest = new PutObjectRequest(this.bucketName, keyName, file);
      PutObjectResult response = this.s3client.putObject(putRequest);
      uploadStatus = true;
      System.out.println("Uploaded object encryption status is " + 
        response.getServerSideEncryption());
    } catch (AmazonServiceException ase) {
      ase.printStackTrace();
      System.out.println("Caught an AmazonServiceException, which means your request made it to Amazon S3, but was rejected with an error response for some reason.");

      System.out.println("Error Message:    " + ase.getMessage());
      System.out.println("HTTP Status Code: " + ase.getStatusCode());
      System.out.println("AWS Error Code:   " + ase.getErrorCode());
      System.out.println("Error Type:       " + ase.getErrorType());
      System.out.println("Request ID:       " + ase.getRequestId());
    } catch (AmazonClientException ace) {
      ace.printStackTrace();
      System.out.println("Caught an AmazonClientException, which means the client encountered an internal error while trying to communicate with S3, such as not being able to access the network.");

      System.out.println("Error Message: " + ace.getMessage());
    }
    return uploadStatus;
  }

  public static void main(String[] args) {
    args = new String[2];
    args[0] = "D:\\debug.txt";
    args[1] = "testkey_client";

    if (args.length != 2) {
      System.err.println("Please specify required args..");
      System.exit(-1);
    }

    String uploadFileName = args[0];
    String keyName = args[1];

    S3Encryption clientEncrypt = new S3Encryption(true, "D:\\s3keys");
    clientEncrypt.doClientSideEncrypt(uploadFileName, keyName);
  }
}