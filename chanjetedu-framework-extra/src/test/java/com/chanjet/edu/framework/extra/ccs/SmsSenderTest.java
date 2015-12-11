package com.chanjet.edu.framework.extra.ccs;

import com.chanjet.edu.framework.extra.ccs.pojo.Response;
import com.google.common.collect.Sets;
import org.junit.Test;

/**
 * Created by shuai.w on 2015/11/19.
 */
public class SmsSenderTest {

	@Test
	public void testSend() throws Exception {
		System.out.println("------------测试发送。。。-----------");
		SmsSender sender =SmsSender.i("64462b4e-09b9-11e5-9092-8f99b1c1c35c", "axjzot");
		Response resp = sender.send("验证码：{0}，10分钟内有效，如非本人操作请忽略本条短信。畅捷教育【畅捷通】", Sets.newHashSet(13910871384L));
		System.err.println(resp);
//		System.err.println(sender.amount());
		System.err.println(sender.status(resp.getInfo("smsId").toString()));
	}
}