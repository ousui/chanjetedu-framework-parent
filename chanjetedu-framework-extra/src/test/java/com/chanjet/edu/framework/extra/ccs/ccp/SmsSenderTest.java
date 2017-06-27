package com.chanjet.edu.framework.extra.ccs.ccp;

import com.chanjet.edu.framework.extra.ccs.Response;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by shuai.w on 2015/11/19.
 */
public class SmsSenderTest {
	SmsSender sender =SmsSender.i("64462b4e-09b9-11e5-9092-8f99b1c1c35c", "axjzot");

	String content = "百城联赛友情提醒：在导入考生信息后，考生需在线报名百城联赛考试，否则只能进行练习而无法参加考试。方法：1、学生登录后点击考试；2、选择考试报名；3、选择对应的百城联赛考试科目；4、点击“报名”。退订回N【畅捷通】";



	@Test
	public void testSend() throws Exception {

//		List<Long> mm = Lists.newArrayList(13910871384L);
		List<Long> mm = read();


		int count = 0;

		for (int i = 0; i < mm.size(); i=i+50) {

			int last = i + 50;
			if (last > mm.size()) {
				last = mm.size();
			}

			Response resp = sender.send(content, new HashSet(mm.subList(i, last)));
			System.err.println(resp);
			System.err.println(sender.status(resp.getInfo("smsId").toString()));

			Thread.sleep(2000);
		}



		System.out.println("------------测试发送。。。-----------");




	}


	public List<Long> read() throws IOException {

		if (1 == 2) {
			List<Long> q = Lists.newArrayList(13581851871L,18611770186L,18627011423L,13800138000L,13888126718L,13910028084L,13651095288L,13606932065L,13712683927L,18612077137L,13701271358L,17001098024L,15837612957L,18612905589L,15010365441L,13691358234L,17001098266L,13641081611L,18600890172L,13552196215L,15210899161L,13910871384L,18612929591L,15130630937L);

			return q;
		}

		File file = new File("/Users/shuai.w/ws/chanjet.com/database/sql/SELECT_t_telephone__t_email__t_username_.tsv");
		Map<Long, String > x = Files.readLines(file, Charset.defaultCharset(), new LineProcessor<Map>() {

			private String name;
			private String phone;
			private String email;

			Map<Long, String> mm = Maps.newHashMap();

			@Override
			public boolean processLine(String s) throws IOException {

				String[] array = s.split(";");
				name = array[2];
				phone = array[0];
				email = array[1];


				if (phone.matches("^1\\d{10}")) {
//					System.out.println(phone + "\t "  + name);

					if (name.startsWith("-")) {
						mm.put(Long.parseLong(phone), "");
					} else {
						mm.put(Long.parseLong(phone), String.valueOf(name.charAt(0)));
					}
				} else {
					System.out.println(phone + "\t "  + name);

				}



				return true;
			}

			@Override
			public Map getResult() {


				return mm;
			}
		});

		return new ArrayList<>(x.keySet());
	}


	@Test
	public void testStatus() {
		System.out.println(sender.status("5066897"));
	}
}