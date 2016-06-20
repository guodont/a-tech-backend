package utils;

import cn.jpush.api.common.ClientConfig;
import cn.jpush.api.push.model.*;
import cn.jpush.api.push.model.notification.IosAlert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

import java.util.HashMap;

public class JPushUtil {

    protected static final Logger LOG = LoggerFactory.getLogger(JPushUtil.class);

    private static final String appKey = "2039820b8454278e574f68f4";
    private static final String masterSecret = "531730b03acb183aaa4a95ca";

    public static final String TITLE = "Test from API example";
    public static final String ALERT = "Test from API Example - alert";
    public static final String MSG_CONTENT = "Test from API Example - msgContent";
    public static final String REGISTRATION_ID = "0900e8d85ef";
    public static final String TAG = "tag_api";

    private static String title;
    private static String msgContent;
    private static String alias;
    private static HashMap<String, String> extras;
    private String registrationId;

    public JPushUtil(String title, String msgContent, String alias, String registrationId) {
        this.title = title;
        this.msgContent = msgContent;
        this.alias = alias;
        this.registrationId = registrationId;
    }

    public JPushUtil(String msgContent, String title) {
        this.msgContent = msgContent;
        this.title = title;
    }

    public JPushUtil(String title, String msgContent, String alias, HashMap addExtras) {
        this.title = title;
        this.msgContent = msgContent;
        this.alias = alias;
        this.extras = addExtras;
    }


//    public static void main(String[] args) {
//        HashMap<String,String> extras = new HashMap<>();
//        extras.put("id","1");
//        new JPushUtil("这是标题","这是内容","18404968725",extras).sendPushWith();
//    }

    public static void sendPushWith() {
        JPushClient jpushClient = new JPushClient(masterSecret, appKey, 3);

        PushPayload payload = buildPushObject_android_and_ios();

        try {
            PushResult result = jpushClient.sendPush(payload);
            LOG.info("Got result - " + result);

        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);

        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Code: " + e.getErrorCode());
            LOG.info("Error Message: " + e.getErrorMessage());
            LOG.info("Msg ID: " + e.getMsgId());
        }
    }

    public static PushPayload buildPushObject_all_alias_alert() {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias("alias1"))
                .setNotification(Notification.alert(ALERT))
                .build();
    }

    public static PushPayload buildPushObject_android_tag_alertWithTitle() {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.tag("tag1"))
                .setNotification(Notification.android(ALERT, TITLE, null))
                .build();
    }

    public static PushPayload buildPushObject_android_and_ios() {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.alias(alias))
                .setNotification(Notification.newBuilder()
                        .setAlert(msgContent)
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .setTitle(title).build())
                        .addPlatformNotification(IosNotification.newBuilder()
                                .incrBadge(1)
                                .addExtras(extras).build())
                        .build())
                .build();
    }

}