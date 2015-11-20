package com.chanjet.edu.framework.extra.ccs;

import com.google.common.collect.Sets;
import org.junit.Test;

/**
 * Created by shuai.w on 2015/11/19.
 */
public class SmsSenderTest {

    @Test
    public void testSend() throws Exception {
        SmsSender.i("64462b4e-09b9-11e5-9092-8f99b1c1c35c", "axjzot").send("验证码：{0}，10分钟内有效，如非本人操作请忽略本条短信。畅捷教育【畅捷通】", Sets.newHashSet(17001098587L, 13910871384L));
    }
}