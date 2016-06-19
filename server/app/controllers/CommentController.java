package controllers;

import controllers.secured.AdminSecured;
import models.Article;
import models.Comment;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Security;
import utils.JsonResult;

/**
 * Created by llz on 2016/6/19.
 */

public class CommentController extends BaseController{

    /**
     * 获取所有评论
     * @return
     */
    public static Result getAllComments() {
        initPageing();
        Comment comment = new Comment();
        response().setHeader("TOTAL_SIZE", String.valueOf(Comment.find.findRowCount()));
        response().setHeader("CUR_PAGE", String.valueOf(page));
        response().setHeader("PAGE_SIZE", String.valueOf(pageSize));
        return ok(Json.toJson(comment.findAllComments(page, pageSize)));
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

}
