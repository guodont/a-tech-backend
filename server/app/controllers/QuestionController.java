////////
// This sample is published as part of the blog article at www.toptal.com/blog
// Visit www.toptal.com/blog and subscribe to our newsletter to read great posts
////////

package controllers;

import controllers.secured.AdminSecured;
import controllers.secured.ExpertSecured;
import models.*;
import models.enums.*;
import play.Play;
import play.data.Form;
import play.data.validation.Constraints;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import utils.JsonResult;

import java.io.File;
import java.util.List;

/**
 * @author guodont
 *         <p>
 *         问题控制器
 */
public class QuestionController extends BaseController {

    /**
     * 用户发布问题
     *
     * @return
     */
    @Security.Authenticated(Secured.class)
    public static Result addQuestion() {
        Form<QuestionForm> postForm = Form.form(QuestionForm.class).bindFromRequest();

        if (postForm.hasErrors()) {
            return badRequest(postForm.errorsAsJson());
        } else {

            //  保存问题
            Category category = Category.find.byId(postForm.get().categoryId);
            Question question = new Question();
            question.clickCount = 0L;
            question.likeCount = 0L;
            question.questionAuditState = QuestionAuditState.WAIT_AUDITED;
            question.questionResolveState = QuestionResolveState.WAIT_RESOLVE;
            question.title = postForm.get().title;
            question.category = category;
            question.user = getUser();
            // TODO 保存图片路径 逗号隔开
            question.images = postForm.get().image;
            question.content = postForm.get().content;
            question.save();
        }
        return ok(new JsonResult("success", "Question added successfully").toJsonResponse());

    }

    /**
     * 审核问题-通过审核
     *
     * @param questionId
     * @return
     */
    @Security.Authenticated(AdminSecured.class)
    public static Result auditQuestionWithAudited(long questionId) {
        Question question = Question.findQuestionById(questionId);
        question.questionAuditState = QuestionAuditState.AUDITED;   // 已通过审核
        question.save();
        return ok(new JsonResult("success", "handl success").toJsonResponse());
    }

    /**
     * 审核问题-审核失败
     *
     * @param questionId
     * @return
     */
    @Security.Authenticated(AdminSecured.class)
    public static Result auditQuestionWithAuditFailed(long questionId) {
        Question question = Question.findQuestionById(questionId);
        question.questionAuditState = QuestionAuditState.FAILED;   // 审核失败
        question.save();
        return ok(new JsonResult("success", "handl success").toJsonResponse());
    }

    /**
     * 获取某用户发布的问题
     *
     * @return
     */
    @Security.Authenticated(Secured.class)
    public static Result getUserQuestions() {
        User user = getUser();
        if (user == null) {
            return badRequest(new JsonResult("error", "No such user").toJsonResponse());
        }
        initPageing();
        return ok(Json.toJson(Question.findQuestionsByUser(user, page, pageSize)));
    }


    /**
     * 获取用户收藏的问题
     *
     * @return
     */
    @Security.Authenticated(Secured.class)
    public static Result getUserFavoriteQuestions() {
        initPageing();
        return ok(Json.toJson(FavoriteQuestion.findAllFavoritesQuestionByUser(getUser(), page, pageSize)));
    }

    /**
     * 添加回答
     *
     * @return
     */
    @Security.Authenticated(ExpertSecured.class)
    public static Result addAnswer() {

        Form<AnswerForm> commentForm = Form.form(AnswerForm.class).bindFromRequest();

        if (commentForm.hasErrors()) {
            return badRequest(commentForm.errorsAsJson());
        } else {
            Answer newAnswer = new Answer();
            Question question = Question.findQuestionById(commentForm.get().questionId);
            question.questionResolveState = QuestionResolveState.RESOLVED;  // 问题标记为已解决
            question.save();
            newAnswer.question = question;
            newAnswer.user = getUser();
            newAnswer.save();
            return status(201, new JsonResult("success", "Answer added successfully").toJsonResponse());
        }
    }

    /**
     * 获取问题详情
     *
     * @param id
     * @return
     */
    public static Result getQuesion(long id) {
        Question question = Question.findQuestionById(id);
        question.clickCount += 1;   // 浏览量+1
        question.save();
        return ok(Json.toJson(Question.findQuestionById(id)));
    }

    /**
     * 获取问题的回答
     *
     * @param id
     * @return
     */
    public static Result getAnswer(long id) {
        Question question = Question.findQuestionById(id);
        return ok(Json.toJson(Answer.findAllAnswersByQuestion(question)));
    }

    /**
     * 获取所有问题
     *
     * @return
     */
    public static Result getQuestions() {
        initPageing();
        List<Question> questions = null;
        if (request().getQueryString("category") != null) {
            int categoryId = Integer.parseInt(request().getQueryString("category"));
            Category category = Category.findCategoryById(categoryId);
            questions = Question.findQuestionsByCategory(category, page, pageSize);
            for ( Question question : questions) {
                // 判断用户的收藏状态

            }
            return ok(Json.toJson(questions));
        } else {
            return ok(Json.toJson(Question.findQuestions(page, pageSize)));
        }
    }

    /**
     * 获取指派给专家的问题
     *
     * @param expertId
     * @return
     */
    public static Result getQuestionsByExpert(long expertId) {
        initPageing();
        User user = User.findById(expertId);
        if (request().getQueryString("status") != null) {
            return ok(Json.toJson(Question.findQuestionsByExpertAndStatus(user, request().getQueryString("status"), page, pageSize)));
        } else {
            return ok(Json.toJson(Question.findQuestionsByExpertAndStatus(user, null, page, pageSize)));
        }
    }

    /**
     * 删除问题
     *
     * @param id
     * @return
     */
    @Security.Authenticated(AdminSecured.class)
    public static Result deleteQuestion(long id) {
        Question question = Question.findQuestionById(id);
        question.delete();
        return ok(new JsonResult("success", "question deleted").toJsonResponse());
    }

    public static Result updateQuestion(long id) {
        return play.mvc.Results.TODO;
    }

    /**
     * 用户收藏问题
     *
     * @return
     */
    @Security.Authenticated(Secured.class)
    public static Result favQuestion(long questionId) {
        Question question = Question.findQuestionById(questionId);

        if (question !=null) {
            FavoriteQuestion favoriteQuestion = new FavoriteQuestion();
            favoriteQuestion.question = question;
            favoriteQuestion.user = getUser();
            favoriteQuestion.save();            // 保存收藏记录表

            question.likeCount += 1;    // 收藏数+1
            question.save();

            return ok(new JsonResult("success", "fav question success").toJsonResponse());
        } else {
            return badRequest(new JsonResult("error", "such question not exits").toJsonResponse());
        }

    }

    /**
     * 用户取消收藏问题
     *
     * @return
     */
    @Security.Authenticated(Secured.class)
    public static Result unFavQuestion(long questionId) {

        Question question = Question.findQuestionById(questionId);

        FavoriteQuestion.findFavoriteByQuestionAndUser(getUser(), question).delete();   // 删除记录

        question.likeCount -= 1;    // 收藏数-1
        question.save();

        return ok(new JsonResult("success", "cancel fav question success").toJsonResponse());
    }

    /**
     * 发布问题表单数据
     */
    public static class QuestionForm {

        @Constraints.Required
        public Long categoryId;     //  分类id

        @Constraints.Required
        @Constraints.MaxLength(45)
        public String title;        //  问题标题

        @Constraints.Required
        public String content;      //  内容


        public String image;        //  配图

    }

    /**
     * 回答问题表单数据
     */
    public static class AnswerForm {

        @Constraints.Required
        public Long questionId;      //  问题id

        @Constraints.Required
        public String comment;      //  评论内容

    }

}
