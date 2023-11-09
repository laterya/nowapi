package com.yp.nowapi.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.ijpay.alipay.AliPayApi;
import com.yp.nowapi.payment.AliPayBean;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author yp
 * @date: 2023/11/9
 */
@SpringBootTest
public class PayTest {

    @Resource
    private AliPayBean aliPayBean;

    @Test
    public void test() {
        String subject = "Javen 支付宝扫码支付测试";
        String totalAmount = "86";
        String storeId = "123";
//        String notifyUrl = aliPayBean.getDomain() + NOTIFY_URL;
        String notifyUrl = aliPayBean.getDomain() + "/aliPay/cert_notify_url";

        AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
        model.setSubject(subject);
        model.setTotalAmount(totalAmount);
        model.setStoreId(storeId);
        model.setTimeoutExpress("5m");
        model.setOutTradeNo("12345678");
        try {
            String resultStr = AliPayApi.tradePrecreatePayToResponse(model, notifyUrl).getBody();
            JSONObject jsonObject = JSONObject.parseObject(resultStr);
            QrCodeUtil.generate(jsonObject.getJSONObject("alipay_trade_precreate_response").getString("qr_code"), 300, 300, FileUtil.file("A:/qrcode.jpg"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
