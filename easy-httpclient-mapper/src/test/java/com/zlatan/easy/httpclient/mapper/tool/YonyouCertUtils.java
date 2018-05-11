package com.zlatan.easy.httpclient.mapper.tool;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.compression.CompressionCodecs;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.openssl.PEMParser;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Map;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 11/05/2018 2:58 PM
 */
public class YonyouCertUtils {

  public static String generateInvoiceSign(String requestData, String pemCert)
          throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
    // 读取CA证书与PEM格式证书需要根据实际证书使用情况而定,目前这两种都支持
    PrivateKey privateKey = loadPrivateKeyOfCA(pemCert);
    Map<String, Object> claims = JwtParamBuilder
            .build()
            .setSubject("tester")
            .setIssuer("einvoice")
            .setAudience("einvoice")
            .addJwtId()
            .addIssuedAt()
            .setExpirySeconds(300)
            .setNotBeforeSeconds(300)
            .getClaims();
    // 需要将表单参数requestdatas的数据进行md5加密，然后放到签名数据的requestdatas中
    // 此签名数据必须存在，否则在验证签名时会不通过
    claims.put("requestdatas", getMD5(requestData));
    return Jwts.builder()
            .signWith(SignatureAlgorithm.RS512, privateKey)
            .setClaims(claims)
            .compressWith(CompressionCodecs.DEFLATE)
            .compact();
  }

  public static PrivateKey loadPrivateKeyOfCA(String pemCert)
          throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
    StringReader reader = new StringReader(pemCert);
    PEMParser pr = new PEMParser(reader);
    byte[] privateKeyBytes = pr.readPemObject().getContent();
    PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privateKeyBytes);
    KeyFactory kf = KeyFactory.getInstance("RSA");
    PrivateKey privateKey = kf.generatePrivate(spec);
    reader.close();
    pr.close();
    return privateKey;
  }

  public static String getPermString() {
    URL f = YonyouCertUtils.class.getResource("/pro22.pfx");
    String pemCert = null;
    try {
      KeyStore keyStore = KeyStore.getInstance("pkcs12");
      keyStore.load(f.openStream(), "password".toCharArray());
      String alias = keyStore.aliases().nextElement();
      Base64 encoder = new Base64(64);
      byte[] keyBytes = null;
      PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, "password".toCharArray());
      keyBytes = privateKey.getEncoded();
      pemCert = "-----BEGIN PRIVATE KEY-----\n" +
              new String(encoder.encode(keyBytes)) +
              "-----END PRIVATE KEY-----";
    } catch (Exception e) {
      e.printStackTrace();
    }
    return pemCert;
  }

  /**
   * 计算MD5
   */
  private static String getMD5(String value) throws NoSuchAlgorithmException {
    byte[] buf = null;
    buf = value.getBytes(StandardCharsets.UTF_8);
    MessageDigest md5 = MessageDigest.getInstance("MD5");
    md5.update(buf);
    byte[] tmp = md5.digest();
    StringBuilder sb = new StringBuilder();
    for (byte b : tmp) {
      sb.append(String.format("%02x", b & 0xff));
    }
    return sb.toString();
  }
}
