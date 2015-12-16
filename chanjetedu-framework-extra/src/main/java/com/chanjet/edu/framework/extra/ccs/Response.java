package com.chanjet.edu.framework.extra.ccs;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * Created by shuai.w on 2015/11/19.
 */
@Data
public class Response implements Serializable {
	private final static Logger logger = LoggerFactory.getLogger(Response.class);

	private int httpCode;
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
			logger.error("pares string[{}] happend error: {}", info, e.getLocalizedMessage());
		}
	}

	public static Response parse(String content) {
		try {
			return JSON.parseObject(content, Response.class, Feature.IgnoreNotMatch);
		} catch (Exception e) {
			logger.error("pares string[{}] happend error: {}", content, e.getLocalizedMessage());
		}
		return null;
	}

}
