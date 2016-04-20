////////
// This sample is published as part of the blog article at www.toptal.com/blog
// Visit www.toptal.com/blog and subscribe to our newsletter to read great posts
////////

package controllers;

import models.Admin;
import models.Article;
import models.Category;
import models.Comment;
import models.enums.ArticlePushState;
import models.enums.ArticleState;
import models.enums.ArticleType;
import models.enums.CategoryType;
import play.data.Form;
import play.data.validation.Constraints;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Security;
import utils.JsonResult;

public class CategoryController extends BaseController {

  /**
   * 管理员添加分类
   * @return
   */
  @Security.Authenticated(AdminSecured.class)
  public static Result addCategory(String categoryType, Long parentId) {

    Form<CategoryForm> postForm = Form.form(CategoryForm.class).bindFromRequest();

    if (postForm.hasErrors()) {

      return badRequest(postForm.errorsAsJson());

    } else {
      //  保存文章
      Category category = new Category();

      //  判断分类类型
      if (categoryType.equals(CategoryType.ARTICLE)) {
        category.categoryType = CategoryType.ARTICLE;
      } else if (categoryType.equals(CategoryType.EXPERT)) {
        category.categoryType = CategoryType.EXPERT;
      } else if (categoryType.equals(CategoryType.QUESTION)) {
        category.categoryType = CategoryType.QUESTION;
      } else if (categoryType.equals(CategoryType.TRADE)) {
        category.categoryType = CategoryType.TRADE;
      }

      category.name = postForm.get().name;
      category.image = postForm.get().image;
      category.sort = postForm.get().sort;
      category.save();
    }
    return ok(new JsonResult("success", "Article added successfully").toJsonResponse());

  }

  public static Result getCategories(String type) {
    return play.mvc.Results.TODO;
  }

  /**
   * 添加分类表单数据
   */
  public static class CategoryForm {

    @Constraints.Required
    @Constraints.MaxLength(255)
    public String name;           //  分类名称

    @Constraints.MaxLength(255)
    public String image;          //  配图

    public Integer sort;          //  排序

  }

}
