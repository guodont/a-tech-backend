package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.secured.WeChatSecured;
import models.Category;
import models.Message;
import models.Question;
import models.User;
import models.enums.MessageType;
import models.enums.QuestionAuditState;
import models.enums.QuestionResolveState;
import models.enums.UserType;
import play.data.Form;
import play.data.validation.Constraints;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Security;
import utils.JPushUtil;
import utils.JsonResult;
import utils.WeChatUtil;

import java.util.HashMap;
import java.util.List;

import static controllers.Application.AUTH_TOKEN;


/**
 * Created by guodont on 16/6/25.
 */
public class WeChatController extends BaseController{


    /**
     * 用户绑定微信号
     *
     * @return
     */
    public static Result bindWeChatAccount() {

        Form<BindWeChatAccountData> bindWeChatAccountDataForm = Form.form(BindWeChatAccountData.class).bindFromRequest();

        if (bindWeChatAccountDataForm.hasErrors()) {
            return badRequest(bindWeChatAccountDataForm.errorsAsJson());
        }

        BindWeChatAccountData bindWeChatAccountData = bindWeChatAccountDataForm.get();

        // 判断是否存在对应的用户,存在则绑定,不存在则注册新用户 保存密码
        User user = User.findByPhoneAndPassword(bindWeChatAccountData.phone, bindWeChatAccountData.password);
        User user3 = User.findByPhone(bindWeChatAccountData.phone);

        if (user3 != null) {

            if(user != null) {  // 用户名密码正确

                if (User.findExpertByWeChatOpenId(bindWeChatAccountData.openId) != null) {
                    // 已绑定
                    return ok();
                } else {
                    // 未绑定

                    // 登录成功 绑定微信信息
                    user.setLastIp(request().remoteAddress());  //  保存最后登录ip
                    user.weChatOpenId = bindWeChatAccountData.openId;
                    user.update();

                    // 如果已经有token 则不重新生成token
                    String authToken = user.createToken();
                    ObjectNode authTokenJson = Json.newObject();
                    authTokenJson.put(AUTH_TOKEN, authToken);
                    return ok(authTokenJson);
                }

            } else {
                // 错误的密码
                return badRequest(new JsonResult("error", "用户名或者密码错误").toJsonResponse());
            }

        } else {

            // 注册新用户 并绑定微信号,保存密码
            User user2 = new User();
            user2.setPassword(bindWeChatAccountData.password);
            user2.name = bindWeChatAccountData.userName;
            user2.setPhone(bindWeChatAccountData.phone);
            user2.setLastIp(request().remoteAddress());
            user2.userType = UserType.PUBLIC;
            user2.weChatOpenId = bindWeChatAccountData.openId;
            user2.avatar = bindWeChatAccountData.avatar;
            user2.save();

            //  设置ToKen
            String authToken = user2.createToken();
            ObjectNode authTokenJson = Json.newObject();
            authTokenJson.put(AUTH_TOKEN, authToken);

            return status(201, authTokenJson);

        }
    }


    /**
     * 用户发布问题 for WeChat
     *
     * @return
     */
    @Security.Authenticated(WeChatSecured.class)
    public static Result addQuestionForWeChat() {
        Form<QuestionController.QuestionForm> postForm = Form.form(QuestionController.QuestionForm.class).bindFromRequest();

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
            question.mediaId = postForm.get().mediaId;
            question.category = category;
            question.user = getWeChatUser();

            User expert = User.findById(postForm.get().expertId);
            if (expert != null)
                question.expert = expert;

            // TODO 保存图片路径 逗号隔开
            question.images = postForm.get().image;
            question.content = postForm.get().content;
            question.save();

            Message message = new Message();
            message.setMessageType(MessageType.WECHAT);
            message.setMarkRead(false);
            message.setRelationId(question.getId());
            message.setTitle("您从微信提交了一条问题");
            message.setUser(question.user);
            message.setRemark(question.title);
            message.save();

            HashMap<String, String> extras = new HashMap<String, String>();
            extras.put("id", String.valueOf(question.getId()));
            extras.put("type", MessageType.QUESTION.getName());
            new JPushUtil("您从微信提交了一条问题", question.title, question.user.getPhone(), extras).sendPushWith();

        }
        return ok(new JsonResult("success", "Question added successfully").toJsonResponse());

    }


    /**
     * 获取某用户发布的问题 for WeChat
     *
     * @return
     */
    @Security.Authenticated(WeChatSecured.class)
    public static Result getUserQuestionsForWechat() {
        User user = getWeChatUser();
        if (user == null) {
            return badRequest(new JsonResult("error", "No such user").toJsonResponse());
        }
        initPageing();

        List<Question> questions = null;

        if (request().getQueryString("status") != null) {
            questions = Question.findQuestionsByUserAndStatus(user, request().getQueryString("status"), page, pageSize);
        } else {
            questions = Question.findQuestionsByUserAndStatus(user, null, page, pageSize);
        }

        return ok(Json.toJson(questions));
    }

    /**
     * 绑定微信账号表单
     */
    public static class BindWeChatAccountData {

        @Constraints.Required
        @Constraints.MinLength(6)
        public String password; //  密码

        @Constraints.Required
        @Constraints.MinLength(11)
        @Constraints.MaxLength(11)
        public String phone;    //  手机号

        public String userName; //  用户名

        public String avatar; //  微信头像地址

        @Constraints.Required
        public String openId; //  微信用户OpenId

    }

}
