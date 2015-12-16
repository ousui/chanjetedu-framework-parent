package com.chanjet.edu.framework.extra.ccs.ccp;

import com.chanjet.ccs.ccp.service.CcpService;
import com.chanjet.edu.framework.extra.ccs.Response;
import com.chanjet.edu.framework.extra.ccs.SendException;
import com.google.common.collect.Sets;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.UUID;

/**
 * Created by shuai.w on 2015/11/17.
 */
public class SmsSender {

	private static final Logger logger = LoggerFactory.getLogger(SmsSender.class);

	@Getter
	private final String appkey;
	@Getter
	private final String appsecret;

	private final CcpService ccpService;

	public static SmsSender i(String appkey, String appsecret) {
		logger.debug("--------------- appkey:{}, secret: ***{} ", appkey, appsecret.substring(3));
		return new SmsSender(appkey, appsecret);
	}

	private SmsSender(String appkey, String appsecret) {
		this.appkey = appkey;
		this.appsecret = appsecret;
		this.ccpService = new CcpService(appkey, appsecret, UUID.randomUUID().toString());
	}

	public Response send(String content, Long phone) throws SendException {
		return send(content, Sets.newHashSet(phone));
	}

	public Response send(String content, Set<Long> phones) throws SendException {
		String mobiles = phones.toString().replace(" ", "").replace("[", "").replace("]", "");
		logger.debug("发送信息，手机号码为：{}", mobiles);
		logger.debug("发送信息，内容：{}", content);
		String result = ccpService.sendNoticeSms(content, mobiles, null);
		logger.debug("发送结果[str]：{}", result);
		Response resp = Response.parse(result);
		logger.debug("发送结果[obj]：{}", resp);

		if (resp.getHttpCode() != 200) {
			logger.error("发送失败[obj]：{}", resp);
			throw new SendException("访问错误：" + resp.getInfo());
		}
		return resp;
	}

	public Response status(String smsId) {
		String result = ccpService.getSmsStatus(smsId);
		logger.debug(result);
		Response resp = Response.parse(result);
		return resp;
	}

	public Response amount() {
		String result = ccpService.getNoticeAmount();
		logger.debug(result);
		return Response.parse(result);
	}


}
