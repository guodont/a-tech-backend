package utils;

import controllers.SecurityController;
import play.libs.Json;
import play.libs.ws.WS;

import java.util.HashMap;

/**
 * Created by guodont on 16/6/25.
 */
public class WeChatUtil {

    private static String userId = "o451ewNvK3JukkMqr0BaXw_MnASI";
    private static String templateId = "K_kz-KlSLOR0MyPJTxgZdKMd6xCkzY-o1VCWcyRgmF0";
    private static String url = "http://wechat.workerhub.cn/question/92";
    private static String color = "#FF0000";
    private static String first = "用户您好，您的提问已有回答";
    private static String keyword1 = "信息";
    private static String keyword2 = "2016-5-1";
    private static String keyword3 = "新回答";
    private static String keyword4 = "2016-5-2";
    private static String remark = "详细结果请点击“详情”查看！";


    public WeChatUtil(String userId, String templateId, String url, String color,
                      String first,
                      String keyword1,
                      String keyword2,
                      String keyword3,
                      String keyword4,
                      String remark) {
        this.userId = userId;
        this.templateId = templateId;
        this.url = url;
        this.color = color;
        this.first = first;
        this.keyword1 = keyword1;
        this.keyword2 = keyword2;
        this.keyword3 = keyword3;
        this.keyword4 = keyword4;
        this.remark = remark;
    }


    /**
     * 数据转换成String
     *
     * @return
     */
    private static String dataToString() {
        return "openId=" + userId + "&templateId=" + templateId
                + "&url=" + url
                + "&color=" + color
                + "&first=" + first
                + "&keyword1=" + keyword1
                + "&keyword2=" + keyword2
                + "&keyword3=" + keyword3
                + "&keyword4=" + keyword4
                + "&remark=" + remark;
    }

    /**
     * 发送微信模版消息
     */
    public static void pushWeChatWithTemplateMsg() {
        WS.url("http://wechat.workerhub.cn/sendTempMsg")
                .setHeader(SecurityController.WECHAT_AUTH_TOKEN_HEADER, "test")
                .setContentType("application/x-www-form-urlencoded;charset=utf-8")
                .post(dataToString());
    }

}
