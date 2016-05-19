////////
// This sample is published as part of the blog article at www.toptal.com/blog
// Visit www.toptal.com/blog and subscribe to our newsletter to read great posts
////////

package controllers;

import controllers.secured.AdminSecured;
import models.*;
import models.enums.CategoryType;
import play.data.Form;
import play.data.validation.Constraints;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Security;
import utils.JsonResult;

import java.util.List;

/**
 * @author guodont
 *         <p>
 *         分类控制器
 */
public class CategoryController extends BaseController {

    /**
     * 管理员添加分类
     *
     * @return
     */
    @Security.Authenticated(AdminSecured.class)
    public static Result addCategory() {

        String categoryType = CategoryType.ARTICLE.getName();
        Long parentId = 0L;

        if (request().getQueryString("categoryType") != null && request().getQueryString("parentId") != null) {
            categoryType = request().getQueryString("categoryType");
            parentId = Long.valueOf(request().getQueryString("parentId"));
        }

        Form<CategoryForm> postForm = Form.form(CategoryForm.class).bindFromRequest();

        System.out.print(postForm.toString());
        if (postForm.hasErrors()) {

            return badRequest(postForm.errorsAsJson());

        } else {
            //  保存分类
            Category category = new Category();

            //  判断分类类型
            if (categoryType.equals(CategoryType.ARTICLE.getName())) {
                category.categoryType = CategoryType.ARTICLE;
            } else if (categoryType.equals(CategoryType.EXPERT.getName())) {
                category.categoryType = CategoryType.EXPERT;
            } else if (categoryType.equals(CategoryType.QUESTION.getName())) {
                category.categoryType = CategoryType.QUESTION;
            } else if (categoryType.equals(CategoryType.TRADE.getName())) {
                category.categoryType = CategoryType.TRADE;
            }

            category.name = postForm.get().name;
            category.image = postForm.get().image;
            category.sort = postForm.get().sort;
            category.pid = parentId;  // 父id
            category.save();
        }
        return ok(new JsonResult("success", "Category added successfully").toJsonResponse());

    }

    /**
     * 根据分类类型获取分类
     *
     * @return
     */
    public static Result getCategories() {
        String categoryType = CategoryType.ARTICLE.getName();
        List<Category> categories = null;

        if (request().getQueryString("categoryType") != null) {
            categoryType = request().getQueryString("categoryType");
        }
        if (request().getQueryString("parentId") != null) {
            if(Category.findCategoryById(Long.parseLong(request().getQueryString("parentId"))) != null) {
                categories = Category.findCategoriesByParent(Long.parseLong(request().getQueryString("parentId")));
            }
        } else {
            categories = Category.findCategoriesByType(categoryType);
        }
        return ok(Json.toJson(categories));
    }

    /**
     * 删除分类
     *
     * @param id
     * @return
     */
    @Security.Authenticated(AdminSecured.class)
    public static Result deleteCategory(long id) {
        Category category = Category.findCategoryById(id);
        if (category == null) {
            return status(404, new JsonResult("error", "Category not found").toJsonResponse());
        } else {
            category.delete();
            return ok(new JsonResult("success", "Category deleted successfully").toJsonResponse());
        }
    }

    /**
     * 添加分类表单数据
     */
    public static class CategoryForm {

        @Constraints.Required
        @Constraints.MaxLength(255)
        public String name;           //  分类名称

        @Constraints.MaxLength(255)
        @Constraints.Required
        public String image;          //  配图

        public Integer sort;          //  排序

    }

}
