////////
// This sample is published as part of the blog article at www.toptal.com/blog
// Visit www.toptal.com/blog and subscribe to our newsletter to read great posts
////////

package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.secured.AdminSecured;
import models.*;
import models.enums.UserType;
import play.Play;
import play.data.Form;
import play.data.validation.Constraints;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import utils.JsonResult;

import java.io.File;
import java.util.UUID;

/**
 * @author guodont
 *         <p>
 *         为前台服务的主控制器,包括用户注册 登录 首页数据 等
 */
public class Application extends BaseController {

    public final static String AUTH_TOKEN_HEADER = "X-AUTH-TOKEN";
    public static final String AUTH_TOKEN = "authToken";

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
            user.setPhone(newUser.phone);
            user.setLastIp(request().remoteAddress());
            user.userType = UserType.PUBLIC;
            user.save();

            //  设置ToKen
            String authToken = user.createToken();
            ObjectNode authTokenJson = Json.newObject();
            authTokenJson.put(AUTH_TOKEN, authToken);
            response().setCookie(AUTH_TOKEN, authToken);

            return status(201, authTokenJson);
        }
    }


    /**
     * 用户注册第二步
     *
     * @return
     */
    @Security.Authenticated(Secured.class)
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
            loginUser.setRealName(curUser.realName);
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

            //  设置ToKen
            String authToken = admin.createToken();
            ObjectNode authTokenJson = Json.newObject();
            authTokenJson.put(AUTH_TOKEN, authToken);
            response().setCookie(AUTH_TOKEN, authToken);

            return status(201, authTokenJson);
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

            String authToken = admin.createToken();
            ObjectNode authTokenJson = Json.newObject();
            authTokenJson.put(AUTH_TOKEN, authToken);
            response().setCookie(AUTH_TOKEN, authToken);
            return ok(authTokenJson);
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
            user.setLastIp(request().remoteAddress());
            user.update();

            String authToken = user.createToken();
            ObjectNode authTokenJson = Json.newObject();
            authTokenJson.put(AUTH_TOKEN, authToken);
            response().setCookie(AUTH_TOKEN, authToken);
            return ok(authTokenJson);
        }

    }

    /**
     * 获取当前登录用户信息
     *
     * @return
     */
    @Security.Authenticated(Secured.class)
    public static Result getCurUser() {
        return ok(Json.toJson(getUser()));
    }

    /**
     * 获取当前登录管理员信息
     *
     * @return
     */
    @Security.Authenticated(AdminSecured.class)
    public static Result getCurAdmin() {
        return ok(Json.toJson(getAdmin()));
    }

    /**
     * 退出
     *
     * @return
     */
    @Security.Authenticated(Secured.class)
    public static Result logout() {
        response().discardCookie(AUTH_TOKEN);
        getUser().deleteAuthToken();

        ObjectNode wrapper = Json.newObject();
        ObjectNode msg = Json.newObject();
        msg.put("message", "Logout success");
        wrapper.put("success", msg);
        return ok(wrapper);
    }

    /**
     * 退出管理员帐号
     *
     * @return
     */
    @Security.Authenticated(AdminSecured.class)
    public static Result logoutForAdmin() {
        response().discardCookie(AUTH_TOKEN);
        getAdmin().deleteAuthToken();

        ObjectNode wrapper = Json.newObject();
        ObjectNode msg = Json.newObject();
        msg.put("message", "Logout success");
        wrapper.put("success", msg);
        return ok(wrapper);
    }

    /**
     * 判断是否授权
     *
     * @return
     */
    @Security.Authenticated(Secured.class)
    public static Result isAuthenticated() {
        if (getUser() == null) {
            return unauthorized();
        } else {
            ObjectNode wrapper = Json.newObject();
            ObjectNode msg = Json.newObject();
            msg.put("message", "User is logged in already");
            msg.put("user", getUser().name);
            wrapper.put("success", msg);
            return ok(wrapper);
        }
    }

    /**
     * 判断是否授权
     *
     * @return
     */
    @Security.Authenticated(AdminSecured.class)
    public static Result isAuthenticatedForAdmin() {
        if (getAdmin() == null) {
            return unauthorized();
        } else {
            ObjectNode wrapper = Json.newObject();
            ObjectNode msg = Json.newObject();
            msg.put("message", "User is logged in already");
            msg.put("user", getAdmin().name);
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
     * for CORS
     *
     * @param all
     * @return
     */
    public static Result preflight(String all) {
        response().setHeader("Access-Control-Allow-Origin", "*");
        response().setHeader("Allow", "*");
        response().setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS");
        response().setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Referer, User-Agent");
        return ok();
    }

    /**
     * 上传图片
     *
     * @return
     */
    @Security.Authenticated(Secured.class)
    public static Result uploadImage() {

        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart image = body.getFile("image");

        if (image != null) {

            String fileName = image.getFilename();
            String ext = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
            String accessName = UUID.randomUUID().toString() + ext;

            // 判断文件类型
            if ( !ext.equals(".gif") && !ext.equals(".jpg") && !ext.equals(".jpeg") && !ext.equals(".bmp") && !ext.equals(".png")) {
                return badRequest(new JsonResult("error", "格式不支持").toJsonResponse());
            } else {
                File file = image.getFile();
                file.renameTo(new File("public/images", accessName));

                // 保存图片信息到数据库
                Image imageData = new Image();
                imageData.name = accessName;
                imageData.oldName = fileName;
                imageData.src = accessName;
                imageData.save();

                return ok(new JsonResult("success", request().host() + "/assets/images/" + accessName).toJsonResponse());
            }

        } else {
            return badRequest(new JsonResult("error", "没有文件数据").toJsonResponse());
        }
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
