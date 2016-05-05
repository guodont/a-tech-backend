////////
// This sample is published as part of the blog article at www.toptal.com/blog
// Visit www.toptal.com/blog and subscribe to our newsletter to read great posts
////////

package controllers;

import controllers.secured.AdminSecured;
import controllers.secured.Secured;
import models.*;
import models.enums.ArticlePushState;
import models.enums.ArticleState;
import models.enums.ArticleType;
import play.api.Logger;
import play.data.Form;
import play.data.validation.Constraints;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Security;
import utils.JsonResult;

/**
 * @author guodont
 *
 * 文章控制器
 */
public class ArticleController extends BaseController {

  /**
   * 管理员发布文章
   * @return
   */
  @Security.Authenticated(AdminSecured.class)
  public static Result addArticle() {
    Form<ArticleForm> postForm = Form.form(ArticleForm.class).bindFromRequest();

    if (postForm.hasErrors()) {
      return badRequest(postForm.errorsAsJson());
    } else {
      //  保存文章
      Category category = Category.find.byId(postForm.get().categoryId);

      Article article = new Article();
      article.clickCount = 0L;
      article.commentCount = 0L;
      article.title = postForm.get().title;
      article.category = category;
      article.tag = postForm.get().tag;
      article.sort = postForm.get().sort;
      article.image = postForm.get().image;
      article.content = postForm.get().content;
      article.admin = getAdmin();
      article.articleState = ArticleState.AUDITED;        // 管理员发布的文章状态直接为已审核
      article.articleType = ArticleType.WEB;              // 默认为网站文章
      article.articlePushState = ArticlePushState.NO_PUSH;// 默认不推送到app
      article.save();
    }
    return ok(new JsonResult("success", "Article added successfully").toJsonResponse());

  }

  /**
   * 获取管理员发布的文章
   * @return
   */
  @Security.Authenticated(AdminSecured.class)
  public static Result getAdminPosts() {
    Admin admin = getAdmin();
    if(admin == null) {
      return badRequest(new JsonResult("error", "No such user").toJsonResponse());
    }
    return ok(Json.toJson(Article.findArticlesByAdmin(admin)));
  }

  /**
   * 添加评论
   * @return
   */
  @Security.Authenticated(Secured.class)
  public static Result addComment() {

    Form<CommentForm> commentForm = Form.form(CommentForm.class).bindFromRequest();

    if (commentForm.hasErrors()) {
      return badRequest(commentForm.errorsAsJson());
    } else {
      Comment newComment = new Comment();
      Article article = Article.findArticleById(commentForm.get().articleId);
      article.commentCount++;
      article.save();
      newComment.article = article;
      newComment.user = getUser();
      newComment.content = commentForm.get().comment;
      newComment.save();
      return ok(new JsonResult("success", "Comment added successfully").toJsonResponse());
    }
  }

  public static Result getArticle(long id) {
    return play.mvc.Results.TODO;
  }

  /**
   * 获取所有网站文章
   * @return
   */
  public static Result getArticles() {
    initPageing();
    Article article = new Article();
    return ok(Json.toJson(article.findArticlesByType(ArticleType.WEB.getName(),page,pageSize)));
  }

  public static Result getArticlesByExpert(long expertId) {
    return play.mvc.Results.TODO;
  }

  public static Result getArticlesByCategory(long cateId) {
    initPageing();
    Article article = new Article();
    return ok(Json.toJson(article.findArticlesByCategory(cateId ,page,pageSize)));
  }

  public static Result deleteArticle(long id) {
    return play.mvc.Results.TODO;
  }

  public static Result updateArticle(long id) {
    return play.mvc.Results.TODO;
  }

  /**
   * 发布文章表单数据
   */
  public static class ArticleForm {

    @Constraints.Required
    public Long categoryId;     //  分类id

    @Constraints.Required
    @Constraints.MaxLength(255)
    public String title;        //  文章题目

    @Constraints.MaxLength(255)
    public String tag;          //  标签

    @Constraints.Required
    public String content;      //  内容

    public Integer sort;        //  排序

    public String image;        //  配图

  }

  /**
   * 发送评论表单数据
   */
  public static class CommentForm {

    @Constraints.Required
    public Long articleId;      //  文章id

    @Constraints.Required
    public String comment;      //  评论内容

  }

}
