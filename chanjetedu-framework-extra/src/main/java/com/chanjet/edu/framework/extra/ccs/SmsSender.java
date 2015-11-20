package com.chanjet.edu.framework.extra.ccs;

import com.chanjet.ccs.ccp.service.CcpService;
import com.chanjet.edu.framework.extra.ccs.pojo.Response;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.UUID;

/**
 * Created by shuai.w on 2015/11/17.
 */
public class SmsSender {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Getter
    private final String appkey;
    @Getter
    private final String appsecret;

    private final CcpService ccpService;

    public static SmsSender i(String appkey, String appsecret) {
        return new SmsSender(appkey, appsecret);
    }

    private SmsSender(String appkey, String appsecret) {
        this.appkey = appkey;
        this.appsecret = appsecret;
        this.ccpService = new CcpService(appkey, appsecret, UUID.randomUUID().toString());
    }

    public void send(String content, Set<Long> phones) throws SendException {
        String mobiles = phones.toString().replace(" ", "").replace("[", "").replace("]", "");
        logger.debug("发送信息，手机号码为：{}", mobiles);
        String       result = ccpService.sendNoticeSms(content, mobiles, null);
        Response resp   = Response.parse(result);
        logger.debug("发送结果：{}", resp);

        if (resp.getHttpCode() != 200) {
            throw new SendException("访问错误：" + resp.getInfo());
        }

    }


}
