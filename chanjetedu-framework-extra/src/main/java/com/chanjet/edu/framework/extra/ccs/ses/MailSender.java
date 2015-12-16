package com.chanjet.edu.framework.extra.ccs.ses;

import com.chanjet.ccs.ses.service.SesConstants;
import com.chanjet.ccs.ses.service.SesService;
import com.chanjet.edu.framework.base.utils.StringUtils;
import com.chanjet.edu.framework.extra.ccs.Response;
import com.chanjet.edu.framework.extra.ccs.SendException;
import com.google.common.base.Charsets;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

/**
 * Created by shuai.w on 2015/12/16.
 */
public class MailSender {
	private static final Logger logger = LoggerFactory.getLogger(MailSender.class);

	@Getter
	private final String appkey;
	@Getter
	private final String appsecret;

	@Setter
	@Getter
	private String titlePrefix = "【畅捷教育】";

	@Setter
	@Getter
	private String sign = "畅捷教育 - http://exam.chanjet.com";

	private final SesService sesService;

	private MailSender(String appkey, String appsecret) {
		this.appkey = appkey;
		this.appsecret = appsecret;
		this.sesService = new SesService(appkey, appsecret, UUID.randomUUID().toString());
	}

	public static MailSender i(String appkey, String secret) {
		logger.debug("--------------- appkey:{}, secret: ***{} ", appkey, secret.substring(3));
		return new MailSender(appkey, secret);
	}

	public Response status(String emailId) {
		String result = sesService.getMailStatus(emailId);
		logger.debug(result);
		Response resp = Response.parse(result);
		return resp;
	}

	public Response amount() {
		return amount(Type.BUSINESS);
	}

	private Response amount(Type type) {
		String result = sesService.getAmount(type.getParam());
		logger.debug(result);
		return Response.parse(result);
	}

	/**
	 * 发送通知邮件。
	 */
	public Response send(String subject, String content, String email) throws SendException {
		return this.send(Type.BUSINESS, subject, content, Sets.newHashSet(email), new Date());
	}

	public Response send(String subject, String content, Set<String> emails) throws SendException {
		return this.send(Type.BUSINESS, subject, content, emails, new Date());
	}

	/**
	 * 定时发送通知邮件
	 *
	 * @param subject
	 * @param content
	 * @param emails
	 * @param time
	 * @return
	 * @throws SendException
	 */
	public Response send(String subject, String content, Set<String> emails, Date time) throws SendException {
		return this.send(Type.BUSINESS, subject, content, emails, time);
	}

	private Response send(Type type, String subject, String content, Set<String> emails, Date sendTime) throws SendException {
		subject = this.getTitlePrefix() + subject;
		logger.debug("this subject is: {}", subject);

		try {
			logger.debug("the conten is: {}", content);
			content = URLEncoder.encode(content, Charsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			logger.warn("can't convert encode to [{}] for the content.", Charsets.UTF_8);
		}
		logger.debug("the encode content is: {}", content);
		logger.debug("the sign is: {}", this.getSign());
		String receivers = StringUtils.collectionToDelimitedString(emails, ",");
		logger.debug("the receivers is [{}]", receivers);
		Date now = new Date();
		if (sendTime != null && sendTime.before(now)) {
			logger.debug("the sendTime[{}] <= now time[{}], send now!", sendTime, now);
			sendTime = null;
		}

		String result = "";
		switch (type) {
			case MARKET:
				result = sesService.sendMTMail(subject, content, receivers, this.getSign(), sendTime);
				break;
			case BUSINESS:
				result = sesService.sendBTMail(subject, content, receivers, this.getSign(), sendTime);
				break;
			default:
				break;
		}
		logger.debug("发送结果[str]：{}", result);
		Response resp = Response.parse(result);
		logger.debug("发送结果[obj]：{}", resp);

		if (resp.getHttpCode() != 200) {
			logger.error("发送失败[obj]：{}", resp);
			throw new SendException("访问错误：" + resp.getInfo());
		}

		return resp;
	}

	enum Type {
		BUSINESS(SesConstants.mailBusi),
		MARKET(SesConstants.mailMark);

		private final String param;

		Type(String param) {
			this.param = param;
		}

		public String getParam() {
			return param;
		}
	}
}
