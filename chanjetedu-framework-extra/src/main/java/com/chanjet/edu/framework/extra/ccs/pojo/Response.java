package com.chanjet.edu.framework.extra.ccs.pojo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by shuai.w on 2015/11/19.
 */
@Data
public class Response implements Serializable {


    private int    httpCode;
    private String code;

    @Setter(value = AccessLevel.NONE)
    private String info;

    @Getter(value = AccessLevel.NONE)
    @Setter(value = AccessLevel.NONE)
    private JSONObject infoJson;

    public Object getInfo(String filed) {
        try {
            return infoJson.get(filed);
        } catch (NullPointerException e) {
            return this.info;
        }
    }

    public void setInfo(Object info) {
        try {
            this.infoJson = JSONObject.parseObject(String.valueOf(info));
        } catch (Exception e) {
            this.info = String.valueOf(info);
        }
    }

    public static Response parse(String content) {
        return JSON.parseObject(content, Response.class, Feature.IgnoreNotMatch);
    }

}
