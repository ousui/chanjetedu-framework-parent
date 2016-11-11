package com.chanjet.edu.framework.base.security;

import com.chanjet.edu.framework.base.BaseServiceImpl;
import com.google.common.collect.Maps;
import org.junit.Test;
import org.springframework.core.serializer.DefaultSerializer;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shuai.w on 2016/4/14.
 */
public class EncryptorTest {


    String path = "/home/shuai.w/_TEST-";

    @Test
    public void encrypt() throws Exception {

        encrypt("123", 1);
        encrypt("123", 2);
        encrypt("123", 3);

    }


    @Test
    public void decrypt() {
        try {
            decrypt("123", 1);
        } catch (Exception e) {
            System.out.println(e .getLocalizedMessage());
        }
        try {
            decrypt("123", 2);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
        try {
            decrypt("123", 3);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }


    private void encrypt(String pass, int index) throws Exception {
        Map<String, Object> hw = Maps.newHashMap();
        hw.put("K", "我知道");
        hw.put("KX", "UPDATE A SET x = ? WHERE 1 = 1;");
        hw.put("K2", StringUtils.class);
        hw.put("K3", new BaseServiceImpl());

        FileOutputStream fos = new FileOutputStream(new File(path + index));
        new DefaultSerializer().serialize(Encryptor.i(pass).encrypt(hw), fos);
    }

    private void decrypt(String pass, int index) throws Exception {
//		byte[] oo = (byte[]) new DefaultDeserializer().deserialize(fis);
//		oo = (byte[]) new DeserializingConverter().convert(oo);
//		System.out.println(oo);
//		byte[] dd = encryptor.decrypt(oo);

        FileInputStream fis2 = new FileInputStream(new File(path + index));
        HashMap ii2 = Encryptor.i(pass).decrypt(HashMap.class, fis2);
        System.out.println("pass[" + index + "] = " + ii2);
    }


}