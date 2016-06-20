package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.User;
import play.data.Form;
import play.data.validation.Constraints;
import play.libs.Json;
import play.mvc.*;

/**
 * 测试Auth用
 */
public class SecurityController extends Controller {

    public final static String AUTH_TOKEN_HEADER = "X-AUTH-TOKEN";
    public final static String WECHAT_AUTH_TOKEN_HEADER = "WECHAT-AUTH-TOKEN";
    public static final String AUTH_TOKEN = "authToken";


    public static User getUser() {
        return (User)Http.Context.current().args.get("user");
    }

    // returns an authToken
    public static Result login() {

        Form<UserLogin> userLoginForm = Form.form(UserLogin.class).bindFromRequest();
        if (userLoginForm.hasErrors()) {
            return badRequest(userLoginForm.errorsAsJson());
        }

        UserLogin loggingInUser = userLoginForm.get();
        User user = User.findByPhoneAndPassword(loggingInUser.phone, loggingInUser.password);

        if (user == null) {
            return unauthorized();
        }
        else {
            String authToken = user.createToken();
            ObjectNode authTokenJson = Json.newObject();
            authTokenJson.put(AUTH_TOKEN, authToken);
            response().setCookie(AUTH_TOKEN, authToken);
            return ok(authTokenJson);
        }
    }

    @Security.Authenticated(Secured.class)
    public static Result logout() {
        response().discardCookie(AUTH_TOKEN);
        getUser().deleteAuthToken();
        return redirect("/");
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