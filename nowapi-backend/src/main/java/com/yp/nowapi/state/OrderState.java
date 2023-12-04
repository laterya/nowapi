package com.yp.nowapi.state;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yp
 * @date: 2023/11/17
 */
public enum OrderState {
    ORDER_WAIT_PAY("待支付", "not pay"),
    ORDER_FINISH_PAY("已支付", "paid");

    private final String text;

    private final String value;

    OrderState(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
