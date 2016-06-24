package controllers;

import models.Admin;
import models.User;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Security;

/**
 * Created by guodont on 16/4/18.
 */
public class BaseController extends Controller {

    public static int page;
    public static int pageSize;

    public static void initPageing() {
        if (request().getQueryString("page") != null && request().getQueryString("pageSize") != null) {
            page = Integer.valueOf(request().getQueryString("page"));
            pageSize = Integer.valueOf(request().getQueryString("pageSize"));
        } else {
            page = 1;
            pageSize = 20;
        }
    }

    /**
     * 获取当前会话管理员
     *
     * @return
     */
    public static Admin getAdmin() {
        return (Admin) Http.Context.current().args.get("admin");
    }

    /**
     * 获取当前会话用户
     *
     * @return
     */
    public static User getUser() {
//        return (User) Http.Context.current().args.get("user");
        // !!这里直接从请求头里获取token 再从数据库获取当前会话用户,否则普通不需权限的get动作就不能调用此方法了
        String[] authTokenHeaderValues = request().headers().get(SecurityController.AUTH_TOKEN_HEADER);
        if ((authTokenHeaderValues != null) && (authTokenHeaderValues.length == 1) && (authTokenHeaderValues[0] != null)) {
            User user = models.User.findByAuthToken(authTokenHeaderValues[0]);
            if (user != null) {
                return user;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 获取当前微信会话用户
     *
     * @return
     */
    public static User getWeChatUser() {
        // !!这里直接从请求头里获取token 再从数据库获取当前会话用户,否则普通不需权限的get动作就不能调用此方法了
        String[] authTokenHeaderValues = request().headers().get(SecurityController.WECHAT_OPEN_ID);
        if ((authTokenHeaderValues != null) && (authTokenHeaderValues.length == 1) && (authTokenHeaderValues[0] != null)) {
            User user = models.User.findExpertByWeChatOpenId(authTokenHeaderValues[0]);
            if (user != null) {
                return user;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

}
