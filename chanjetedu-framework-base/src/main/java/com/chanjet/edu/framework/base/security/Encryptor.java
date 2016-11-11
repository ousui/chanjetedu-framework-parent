package com.chanjet.edu.framework.base.security;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.serializer.DefaultDeserializer;
import org.springframework.core.serializer.support.DeserializingConverter;
import org.springframework.core.serializer.support.SerializingConverter;
import org.springframework.util.Assert;
import sun.security.provider.Sun;
import sun.security.provider.VerificationProvider;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 加密工具，用于对 byte 进行加密和揭秘的操作
 * 原则上，类 new 出来以后，不允许改变加密类型
 * Created by shuai.w on 2016/4/14.
 */
public class Encryptor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final static int BUFFER = 1024;
    /**
     * 加密使用的密钥；
     */
    @Getter
    private final String password;
    @Getter
    private final EncryptType encryptType;

    private Cipher cipher;
    private SecretKeySpec key;

    public static Encryptor i(String password) {
        return new Encryptor(password, EncryptType.AES);
    }

    public static Encryptor i(String password, EncryptType encryptType) {
        return new Encryptor(password, encryptType);
    }

    public Encryptor(String password, EncryptType encryptType) {
        Assert.hasText(password, "密码不能为空！");
        this.password = password;
        if (encryptType == null) { // 默认使用 AES 加密
            encryptType = EncryptType.AES;
        }
        this.encryptType = encryptType;

        this.init();
    }

    /**
     * 初始化加密方法
     */
    private void init() {
        KeyGenerator keygen;
        SecureRandom sr;
        try {
            keygen = KeyGenerator.getInstance(encryptType.getName());
            sr = SecureRandom.getInstance("SHA1PRNG", new Sun()); // 固定算法和供应商
        } catch (NoSuchAlgorithmException e) {
            logger.warn("不支持的加密类型：{}", encryptType);
            return;
        }
        sr.setSeed(password.getBytes());
        keygen.init(128, sr);
        new VerificationProvider();
        SecretKey secretKey = keygen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        key = new SecretKeySpec(enCodeFormat, encryptType.getName());
        try {
            cipher = Cipher.getInstance(encryptType.getName());
        } catch (NoSuchAlgorithmException e) {
            logger.warn("不支持的加密类型：{}", encryptType);
            return;
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            logger.warn("不支持的加密类型：{}", encryptType);
            return;
        }
    }

    /**
     * 加密 byte 数组，返回 byte 数组
     *
     * @param content
     * @return
     */
    private byte[] encrypt(byte[] content) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
            return cipher.doFinal(content);
        } catch (InvalidKeyException e) {
            logger.warn("无效key：{}", key);
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return content;
    }

    /**
     * 加密对象
     *
     * @param object
     * @return
     * @throws IOException
     */
    public byte[] encrypt(Object object) throws IOException {
        byte[] src = new SerializingConverter().convert(object);
        return this.encrypt(src);
    }

    /**
     * 解密数组，并返回数组
     *
     * @param content
     * @return
     */
    private byte[] decrypt(byte[] content) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
            return cipher.doFinal(content);
        } catch (InvalidKeyException e) {
            logger.warn("无效key：{}", key);
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return content;
    }

    /**
     * 按照类型进行揭秘
     *
     * @param clazz
     * @param content
     * @param <T>
     * @return
     * @throws IOException
     */
    public <T> T decrypt(Class<T> clazz, byte[] content) throws IOException {
        try {
            return (T) new DeserializingConverter().convert(this.decrypt(content));
        } catch (ClassCastException e) {
            logger.error("类型转换错误, {}", e.getLocalizedMessage());
            return null;
        }
    }

    public <T> T decrypt(Class<T> clazz, InputStream inputStream) throws IOException {
        byte[] src = (byte[]) new DefaultDeserializer().deserialize(inputStream);
        return this.decrypt(clazz, src);
    }
}
