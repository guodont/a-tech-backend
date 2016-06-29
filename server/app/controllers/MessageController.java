package controllers;

import controllers.secured.AdminSecured;
import models.Message;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Security;
import utils.JsonResult;

import java.util.List;

/**
 * 消息中心控制器
 * Created by guodont on 16/6/5.
 */
public class MessageController extends BaseController {

    /**
     * 获取消息 for admin
     *
     * @return
     */
    @Security.Authenticated(AdminSecured.class)
    public static Result getMessagesForAdmin() {

        List<Message> messages = null;
        initPageing();

        messages = Message.getAllMessagesForAdmin(page, pageSize);

        return ok(Json.toJson(messages));
    }

    /**
     * 获取消息
     *
     * @return
     */
    @Security.Authenticated(Secured.class)
    public static Result getMessages() {

        List<Message> messages = null;
        initPageing();
        if (request().getQueryString("type") != null && request().getQueryString("type").equals("unRead")) {
            messages = Message.getUnReadMessages(getUser(), page, pageSize);
        } else {
            messages = Message.getAllMessages(getUser(), page, pageSize);
        }

        return ok(Json.toJson(messages));
    }

    /**
     * 将消息标为已读
     *
     * @return
     */
    @Security.Authenticated(Secured.class)
    public static Result readMarkMessage(long id) {

        Message message = Message.findByIdAndUser(id, getUser());
        if (message != null) {
            message.setMarkRead(true);
            message.update();
            return ok(new JsonResult("success", "Message marked read").toJsonResponse());
        } else {
            return notFound(new JsonResult("error", "Message not found").toJsonResponse());
        }

    }

    @Security.Authenticated(Secured.class)
    public static Result getUnReadMessageCount() {
        return ok(Json.toJson(Message.unReadMessageCount(getUser())));
    }
}
