package com.chanjet.edu.framework.extra.ccs.ses;

import com.chanjet.ccs.base.util.Encodes;
import com.chanjet.ccs.base.util.HttpUtil;
import com.chanjet.edu.framework.extra.ccs.Response;
import com.chanjet.edu.framework.extra.ccs.SendException;
import com.google.common.base.Charsets;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

/**
 * Created by shuai.w on 2015/12/16.
 */
public class MailSenderTest {
	String appkey, secret;

	MailSender mailSender;

	@Before
	public void before() {
//		appkey = "13824ca1-7c7b-41b7-bcaa-18a4c8e8e66b";
//		secret = "ydfeme";
		appkey = "64462b4e-09b9-11e5-9092-8f99b1c1c35c";
		secret = "axjzot";
		mailSender = MailSender.i(appkey, secret);
	}

	@Test
	public void testSend() throws SendException, UnsupportedEncodingException {
		String content = "您好<br/>&nbsp;&nbsp;&nbsp;&nbsp;感谢您在畅捷教育云平台上申请教师账号。账号正在审批过程中，通过后会有邮件通知，请留意。";

		Response resp = mailSender.send("用户注册", content, Sets.newHashSet("x@ousui.org"));
		System.out.println("resp = " + resp);
	}

	@Test
	public void testStatus() {
		System.out.println("Charsets.UTF_8.encode(\"你好\") = " + Charsets.UTF_8.name());
		System.out.println("Charsets.UTF_8.encode(\"你好\") = " + Charsets.UTF_8.displayName());
	}

	public static void main(String[] args) {
		String auth = Encodes.encodeBase64("{\"appKey\":\"64462b4e-09b9-11e5-9092-8f99b1c1c35c\",\"paramInfo\":\"{\\\"date\\\":\\\"2016-06-24 12:00:00\\\",\\\"referer\\\":\\\"reid\\\"}\",\"authInfo\":\"7d2759a618db588bcb95dbcfcac921e7861fd933\"}".getBytes());
		String response = HttpUtil.HttpAction("POST", "http://ses.chanapp.chanjet.com/mail", auth);
		System.out.println(response);
	}

}