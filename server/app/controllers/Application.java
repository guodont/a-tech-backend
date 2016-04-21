////////
// This sample is published as part of the blog article at www.toptal.com/blog
// Visit www.toptal.com/blog and subscribe to our newsletter to read great posts
////////

package controllers;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Admin;
import models.BlogPost;
import models.User;
import models.enums.UserType;
import play.data.Form;
import play.data.validation.Constraints;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import utils.JsonResult;

/**
 * @author guodont
 *
 * 为前台服务的主控制器,包括用户注册 登录 首页数据 等
 */
public class Application extends BaseController {


    /**
     * 首页
     *
     * @return
     */
    public static Result index() {
        return ok("Welcome , there is nongke110 !");
    }

    /**
     * 用户注册第一步
     *
     * @return
     */
    @BodyParser.Of(BodyParser.Json.class)
    public static Result signUpOneStep() {

        Form<SignUpStepOne> signUpStepOneForm = Form.form(SignUpStepOne.class).bindFromRequest();

        if (signUpStepOneForm.hasErrors()) {
            return badRequest(signUpStepOneForm.errorsAsJson());
        }

        SignUpStepOne newUser = signUpStepOneForm.get();

        User existingUser = User.findByPhone(newUser.phone);

        if (existingUser != null) {
            //  用户已被注册
            return badRequest(new JsonResult("error", "User exists").toJsonResponse());

        } else {

            //  保存用户信息
            User user = new User();
            user.setPassword(newUser.password);
            user.name = newUser.userName;
            user.phone = newUser.phone;
            user.lastIp = request().remoteAddress();
            user.userType = UserType.PUBLIC;
            user.save();

            //  设置登录会话信息
            session().clear();
            session("username", newUser.userName);

            return created(new JsonResult("success", "User created successfully").toJsonResponse());
        }
    }


    /**
     * 用户注册第二步
     *
     * @return
     */
    public static Result signUpTwoStep() {

        Form<SignUpStepTwo> signUpStepTwoForm = Form.form(SignUpStepTwo.class).bindFromRequest();

        if (signUpStepTwoForm.hasErrors()) {
            return badRequest(signUpStepTwoForm.errorsAsJson());
        }

        SignUpStepTwo curUser = signUpStepTwoForm.get();

        User loginUser = getUser(); //  获取当前登录用户

        if (loginUser != null) {
            //  用户未登录
            return badRequest(new JsonResult("error", "User not login").toJsonResponse());

        } else {
            //  保存用户信息
            loginUser.realName = curUser.realName;
            loginUser.address = curUser.address;
            loginUser.industry = curUser.industry;
            loginUser.scale = curUser.scale;
            loginUser.update();

            return ok(new JsonResult("success", "User information saved").toJsonResponse());
        }

    }

    /**
     * 管理员注册(TODO 测试用,上线后关闭)
     *
     * @return
     */
    public static Result signUpAdmin() {
        Form<SignUp> signUpForm = Form.form(SignUp.class).bindFromRequest();

        if (signUpForm.hasErrors()) {
            return badRequest(signUpForm.errorsAsJson());
        }

        SignUp newUser = signUpForm.get();
        Admin existingAdmin = Admin.findByEmail(newUser.email);

        if (existingAdmin != null) {
            return badRequest(new JsonResult("error", "User exists").toJsonResponse());
        } else {
            //  保存用户信息
            Admin admin = new Admin();
            admin.setEmail(newUser.email);
            admin.setPassword(newUser.password);
            admin.phone = newUser.phone;
            admin.name = newUser.name;
            admin.lastIp = request().remoteAddress();
            admin.save();

            //  设置登录会话信息
            session().clear();
            session("username", newUser.name);
            session("isAdmin", "true");

            return created(new JsonResult("success", "Admin created successfully").toJsonResponse());
        }
    }

    /**
     * 管理员登录
     *
     * @return
     */
    public static Result loginForAdmin() {
        Form<Login> loginForm = Form.form(Login.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return badRequest(loginForm.errorsAsJson());
        }

        Login loggingInUser = loginForm.get();
        Admin admin = Admin.findByEmailAndPassword(loggingInUser.email, loggingInUser.password);

        if (admin == null) {
            return badRequest(new JsonResult("error", "Incorrect email or password").toJsonResponse());
        } else {
            //  保存最后登录ip
            admin.lastIp = request().remoteAddress();
            admin.save();

            //  设置登录会话信息
            session().clear();
            session("username", loggingInUser.email);
            session("isAdmin", "true");

            ObjectNode wrapper = Json.newObject();
            ObjectNode msg = Json.newObject();
            msg.put("message", "Logged in successfully");
            msg.put("user", admin.name);
            wrapper.put("success", msg);
            return ok(wrapper);
        }
    }

    /**
     * 用户登录
     *
     * @return
     */
    public static Result login() {

        Form<UserLogin> userLoginForm = Form.form(UserLogin.class).bindFromRequest();
        if (userLoginForm.hasErrors()) {
            return badRequest(userLoginForm.errorsAsJson());
        }

        UserLogin loggingInUser = userLoginForm.get();
        User user = User.findByPhoneAndPassword(loggingInUser.phone, loggingInUser.password);

        if (user == null) {

            return badRequest(new JsonResult("error", "Incorrect phone or password").toJsonResponse());

        } else {

            //  保存最后登录ip
            user.lastIp = request().remoteAddress();
            user.update();

            //  设置登录会话信息
            session().clear();
            session("username", user.name);

            //  判断是否是专家
            if (user.userType.getValue().equals(UserType.EXPERT)) {
                session("isExpert", "true");    //  是专家的话设置此session
            }

            ObjectNode wrapper = Json.newObject();
            ObjectNode msg = Json.newObject();
            msg.put("message", "Logged in successfully");
            msg.put("user", user.name);
            wrapper.put("success", msg);
            return ok(wrapper);
        }

    }

    /**
     * 退出
     *
     * @return
     */
    public static Result logout() {
        session().clear();
        return ok();
    }

    /**
     * 判断是否授权
     *
     * @return
     */
    public static Result isAuthenticated() {
        if (session().get("username") == null) {
            return unauthorized();
        } else {
            ObjectNode wrapper = Json.newObject();
            ObjectNode msg = Json.newObject();
            msg.put("message", "User is logged in already");
            msg.put("user", session().get("username"));
            wrapper.put("success", msg);
            return ok(wrapper);
        }
    }

    public static Result getPosts() {
        return ok(Json.toJson(BlogPost.find.findList()));
    }

    public static Result getPost(Long id) {
        BlogPost blogPost = BlogPost.findBlogPostById(id);
        if (blogPost == null) {
            return notFound(new JsonResult("error", "Post not found").toJsonResponse());
        }
        return ok(Json.toJson(blogPost));
    }

    /**
     * 用户表单数据父类
     */
    public static class UserForm {
        @Constraints.Required
        @Constraints.Email
        public String email;   //  邮箱
    }

    /**
     * 管理员用户注册表单数据
     */
    public static class SignUp extends UserForm {
        @Constraints.Required
        @Constraints.MinLength(6)
        public String password; //  密码

        @Constraints.Required
        public String phone;    //  手机号

        @Constraints.Required
        public String name;     //  用户名
    }

    /**
     * 管理员用户登录表单数据
     */
    public static class Login extends UserForm {
        @Constraints.Required
        public String password;   //  密码
    }

    /**
     * 用户注册第一步表单数据
     */
    public static class SignUpStepOne {

        @Constraints.Required
        @Constraints.MinLength(6)
        public String password; //  密码

        @Constraints.Required
        @Constraints.MinLength(11)
        @Constraints.MaxLength(11)
        public String phone;    //  手机号

        @Constraints.Required
        @Constraints.MinLength(3)
        public String userName; //  用户名

    }

    /**
     * 用户注册第二步表单数据
     */
    public static class SignUpStepTwo {

        @Constraints.Required
        public String realName; //  真实姓名

        @Constraints.Required
        public String address;  //  地址

        @Constraints.MaxLength(45)
        public String industry; //  用户经营的产业

        @Constraints.MaxLength(45)
        public String scale;    //  产业规模
    }

    /**
     * 用户登录表单数据
     */
    public static class UserLogin {
        @Constraints.Required
        public String phone;    //  手机号

        @Constraints.Required
        public String password; //  密码
    }
}
