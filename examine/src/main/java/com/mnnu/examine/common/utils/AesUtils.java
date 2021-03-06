package com.mnnu.examine.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * AES加密解密
 */
@Slf4j
public class AesUtils {

    private static final String KEY_ALGORITHM = "AES";

    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";// 默认的加密算法

    public static final String SALT="687849931afdf8b9fe67036e13728bc5";
    /**
     * AES 加密操作
     * @param content  待加密内容
     * @param password 加密密码
     * @return String 返回Base64转码后的加密数据
     */
    public static String encrypt(String content, String password) {
        try {
            // 创建密码器
            final Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            // 设置为UTF-8编码
            final byte[] byteContent = content.getBytes("utf-8");
            // 初始化为加密模式的密码器
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(password));
            // 加密
            final byte[] result = cipher.doFinal(byteContent);
            // 通过Base64转码返回 +/=
            return Base64.encodeBase64String(result).replaceAll("\\+","J").replaceAll("/","C").replaceAll("=","D");
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return "";
    }

    /**
     * AES 解密操作
     *
     * @param content
     * @param password
     * @return String
     */
    public static String decrypt(String content, String password) {
        try {
//            content=content.replaceAll("J","+").replaceAll("","C").replaceAll("=","D")
            // 实例化
            final Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            // 使用密钥初始化，设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(password));
            // 执行操作
            final byte[] result = cipher.doFinal(Base64.decodeBase64(content));
            // 采用UTF-8编码转化为字符串
            return new String(result, "utf-8");
        } catch (final Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return "";
    }

    /**
     * 生成加密秘钥
     *
     * @param password 加密的密码
     * @return SecretKeySpec
     */
    private static SecretKeySpec getSecretKey(final String password) {
        // 返回生成指定算法密钥生成器的 KeyGenerator 对象
        KeyGenerator kg = null;
        try {
            kg = KeyGenerator.getInstance(KEY_ALGORITHM);
            // AES 要求密钥长度为 128
            kg.init(128, new SecureRandom(password.getBytes()));
            // 生成一个密钥
            final SecretKey secretKey = kg.generateKey();
            // 转换为AES专用密钥
            return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);
        } catch (final NoSuchAlgorithmException ex) {
            log.error(ex.getMessage(), ex);
        }
        return null;
    }

    /**
     * 根据密钥，生成 aes.key
     * @param password
     * @return
     */
    public static String getKeyByPass(String password) {
        SecretKeySpec keySpec = getSecretKey(password);
        byte[] b = keySpec.getEncoded();
        return byteToHexString(b);
    }
    /**
     * byte数组转化为16进制字符串
     * @param bytes
     * @return
     */
    public static String byteToHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String strHex=Integer.toHexString(bytes[i]);
            if(strHex.length() > 3) {
                sb.append(strHex.substring(6));
            } else {
                if(strHex.length() < 2) {
                    sb.append("0" + strHex);
                } else {
                    sb.append(strHex);
                }
            }
        }
        return sb.toString();
    }
    public static byte[] encryptECB(byte[] data, byte[] key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"));
        byte[] result = cipher.doFinal(data);
        return result;
    }

    public static byte[] decryptECB(byte[] data, byte[] key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"));
        byte[] result = cipher.doFinal(data);
        return result;
    }

    public static String polyvAesEncrypt(String data,String key) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        byte[] ciphertext = encryptECB(data.getBytes(), key.substring(0,16).getBytes());
        return Base64.encodeBase64String(ciphertext);

    }
    public static void main(String[] args) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        String data = "Hello World"; // 待加密的明文
        String key = "12345678abcdefgh"; // key 长度只能是 16、25 或 32 字节

        byte[] ciphertext = encryptECB(data.getBytes(), key.getBytes());
        System.out.println("ECB 模式加密结果（Base64）：" + Base64.encodeBase64String(ciphertext));

        byte[] plaintext = decryptECB(ciphertext, key.getBytes());
        System.out.println("解密结果：" + new String(plaintext));
    }
}
