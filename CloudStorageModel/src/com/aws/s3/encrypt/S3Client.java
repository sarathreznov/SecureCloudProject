package com.aws.s3.encrypt;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3EncryptionClient;
import com.amazonaws.services.s3.model.CryptoConfiguration;
import com.amazonaws.services.s3.model.CryptoStorageMode;
import com.amazonaws.services.s3.model.EncryptionMaterials;
import com.amazonaws.services.s3.model.GetObjectMetadataRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class S3Client
{
  private AmazonS3 s3client;

  public S3Client(boolean isClientEncrypt, String keysPath)
  {
    try
    {
      ClassLoader loader = S3Encryption.class.getClassLoader();
      PropertiesCredentials propCred = new PropertiesCredentials(loader.getResourceAsStream("resources/AwsCredentials.properties"));
      String myAccessKeyId = propCred.getAWSAccessKeyId();
      String mySecretKey = propCred.getAWSSecretKey();

      AWSCredentials credentials = new BasicAWSCredentials(myAccessKeyId, mySecretKey);
      if (isClientEncrypt) {
        CryptoConfiguration cryptoConfig = new CryptoConfiguration().withStorageMode(CryptoStorageMode.InstructionFile);
        KeyPair keypair = null;
        keypair = loadKeyPair(keysPath, "RSA");
        EncryptionMaterials encryptionMaterials = new EncryptionMaterials(keypair);
        this.s3client = new AmazonS3EncryptionClient(credentials, encryptionMaterials, cryptoConfig);
      } else {
        this.s3client = new AmazonS3Client(propCred);
      }
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (InvalidKeySpecException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public S3ObjectInputStream retrieveS3Object(String key, String bucketName) {
    GetObjectRequest request = new GetObjectRequest(bucketName, key);
    GetObjectMetadataRequest metaRequest = new GetObjectMetadataRequest(bucketName, key);

    S3Object object = this.s3client.getObject(request);
    System.out.println("Content-Type: " + object.getObjectMetadata().getContentType());
    ObjectMetadata metadata = this.s3client.getObjectMetadata(metaRequest);
    System.out.println("Encryption algorithm used: " + metadata.getServerSideEncryption());
    return object.getObjectContent();
  }

  private static void displayTextInputStream(InputStream input)
    throws IOException
  {
    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
    while (true) {
      String line = reader.readLine();
      if (line == null)
        break;
      System.out.println("    " + line);
    }
    System.out.println();
  }

  public static KeyPair loadKeyPair(String path, String algorithm) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException
  {
    File filePublicKey = new File(path + "/public.key");
    FileInputStream fis = new FileInputStream(path + "/public.key");
    byte[] encodedPublicKey = new byte[(int)filePublicKey.length()];
    fis.read(encodedPublicKey);
    fis.close();

    File filePrivateKey = new File(path + "/private.key");
    fis = new FileInputStream(path + "/private.key");
    byte[] encodedPrivateKey = new byte[(int)filePrivateKey.length()];
    fis.read(encodedPrivateKey);
    fis.close();

    KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
    X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(
      encodedPublicKey);
    PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
    System.out.println("Public Key:" + getHexString(publicKey.getEncoded()));
    PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(
      encodedPrivateKey);
    PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
    System.out.println("Private Key:" + getHexString(privateKey.getEncoded()));
    return new KeyPair(publicKey, privateKey);
  }

  private static String getHexString(byte[] b) {
    String result = "";
    for (int i = 0; i < b.length; i++) {
      result = result + Integer.toString((b[i] & 0xFF) + 256, 16).substring(1);
    }
    return result;
  }

  public static void main(String[] args) {
    S3Client client = new S3Client(true, "D:\\s3keys");
    try
    {
      client.retrieveS3Object("testkey_client", "aws_bucket");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}