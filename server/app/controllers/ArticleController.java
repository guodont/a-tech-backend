////////
// This sample is published as part of the blog article at www.toptal.com/blog
// Visit www.toptal.com/blog and subscribe to our newsletter to read great posts
////////

package controllers;

import models.*;
import play.data.Form;
import play.data.validation.Constraints;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

/*
 * This controller contains Posting and Commenting logic. All methods require user to be
 * authenticated.
 */
@Security.Authenticated(Secured.class)
public class ArticleController extends Controller {

  public static Result addPost() {
    Form<ArticleForm> postForm = Form.form(ArticleForm.class).bindFromRequest();

    if (postForm.hasErrors()) {
      return badRequest(postForm.errorsAsJson());
    } else {
      //  保存文章
      Article article = new Article();
      article.clickCount = 0L;
      article.title = postForm.get().subject;
      article.content = postForm.get().content;
      article.admin = getAdmin();
      article.save();
    }
    return ok(Application.buildJsonResponse("success", "Article added successfully"));
  }

  private static Admin getAdmin() {
    return Admin.findByEmail(session().get("username"));
  }

  public static Result getUserPosts() {
    Admin admin = getAdmin();
    if(admin == null) {
      return badRequest(Application.buildJsonResponse("error", "No such user"));
    }
    return ok(Json.toJson(Article.findArticlesByAdmin(admin)));
  }

//  public static Result addComment() {
//    Form<CommentForm> commentForm = Form.form(CommentForm.class).bindFromRequest();
//
//    if (commentForm.hasErrors()) {
//      return badRequest(commentForm.errorsAsJson());
//    } else {
//      PostComment newComment = new PostComment();
//      BlogPost blogPost = BlogPost.findBlogPostById(commentForm.get().postId);
//      blogPost.commentCount++;
//      blogPost.save();
//      newComment.blogPost = blogPost;
//      newComment.user = getUser();
//      newComment.content = commentForm.get().comment;
//      newComment.save();
//      return ok(Application.buildJsonResponse("success", "Comment added successfully"));
//    }
//  }

  public static class ArticleForm {

    @Constraints.Required
    @Constraints.MaxLength(255)
    public String subject;

    @Constraints.Required
    public String content;

  }

  public static class CommentForm {

    @Constraints.Required
    public Long postId;

    @Constraints.Required
    public String comment;

  }

}
