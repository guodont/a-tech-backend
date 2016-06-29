package controllers;

import controllers.secured.AdminSecured;
import models.*;
import models.enums.MessageType;
import models.enums.QuestionAuditState;
import models.enums.TradeState;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Security;
import utils.JPushUtil;
import utils.JsonResult;

import java.util.HashMap;
import java.util.List;

/**
 * Created by llz on 2016/6/19.
 */

public class CommentController extends BaseController{

    /**
     * 获取所有评论
     * @return
     */
    @Security.Authenticated(AdminSecured.class)
    public static Result getAllComments() {
        initPageing();
        List<Comment> comments = null;

        if (request().getQueryString("status") != null && !request().getQueryString("status").equals("")) {
            comments = Comment.findAllComments(request().getQueryString("status"), page, pageSize);
        } else {
            comments = Comment.findAllComments(null, page, pageSize);
        }

        return ok(Json.toJson(comments));
    }

    /**
     * 删除评论
     * @param id
     * @return
     */
    @Security.Authenticated(AdminSecured.class)
    public static Result deleteComment(long id) {
        Comment comment = Comment.findCommentById(id);
        comment.delete();
        return ok(new JsonResult("success", "comment deleted").toJsonResponse());
    }

    /**
     * 审核评论-通过审核
     *
     * @param commentId
     * @return
     */
    @Security.Authenticated(AdminSecured.class)
    public static Result auditCommentWithAudited(long commentId) {

        Comment comment = Comment.findCommentById(commentId);

        comment.auditState = TradeState.AUDITED;   // 已通过审核

        comment.save();

        Message message = new Message();
        message.setMessageType(MessageType.SYSTEM);
        message.setMarkRead(false);
        message.setRelationId(comment.article.getId());
        message.setTitle("您的评论已通过审核");
        message.setUser(comment.user);
        message.setRemark(comment.content);
        message.save();

        HashMap<String, String> extras = new HashMap<String, String>();
        extras.put("id", String.valueOf(comment.article.getId()));
        extras.put("type", MessageType.SYSTEM.getName());
        new JPushUtil("您的评论已通过审核", comment.content, comment.user.getPhone(), extras).sendPushWith();

        return ok(new JsonResult("success", "handl success").toJsonResponse());
    }

    /**
     * 审核评论-审核失败
     *
     * @param commentId
     * @return
     */
    @Security.Authenticated(AdminSecured.class)
    public static Result auditCommentWithFailed(long commentId) {

        Comment comment = Comment.findCommentById(commentId);

        comment.auditState = TradeState.FAILED;

        comment.save();

        Message message = new Message();
        message.setMessageType(MessageType.SYSTEM);
        message.setMarkRead(false);
        message.setRelationId(comment.article.getId());
        message.setTitle("您的评论未通过审核");
        message.setUser(comment.user);
        message.setRemark(comment.content);
        message.save();

        HashMap<String, String> extras = new HashMap<String, String>();
        extras.put("id", String.valueOf(comment.article.getId()));
        extras.put("type", MessageType.SYSTEM.getName());
        new JPushUtil("您的评论未通过审核", comment.content, comment.user.getPhone(), extras).sendPushWith();

        return ok(new JsonResult("success", "handl success").toJsonResponse());
    }
}
