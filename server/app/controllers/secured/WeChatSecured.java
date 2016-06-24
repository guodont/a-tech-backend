////////
// This sample is published as part of the blog article at www.toptal.com/blog
// Visit www.toptal.com/blog and subscribe to our newsletter to read great posts
////////

package controllers.secured;

import controllers.SecurityController;
import models.User;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;

/**
 * 微信Server请求 权限验证
 */
public class WeChatSecured extends Security.Authenticator {

    @Override
    public String getUsername(Context ctx) {
//        String[] authTokenHeaderValues = ctx.request().headers().get(SecurityController.WECHAT_AUTH_TOKEN_HEADER);
//        String[] authOpenIdHeaderValues = ctx.request().headers().get(SecurityController.WECHAT_OPEN_ID);
//        if ((authTokenHeaderValues != null) && (authOpenIdHeaderValues != null) && (authTokenHeaderValues.length == 1)
//                && (authOpenIdHeaderValues.length == 1)
//                && (authTokenHeaderValues[0] != null)
//                && (authOpenIdHeaderValues[0] != null)
//                ) {
//
//            User user = User.findExpertByWeChatOpenId(authOpenIdHeaderValues[0]);
//            if (user != null) {
//                ctx.args.put("user", user);
//                return user.name;
//            }
//
//        }
//
//        return null;
        // TODO TEST
        String[] authOpenIdHeaderValues = ctx.request().headers().get(SecurityController.WECHAT_OPEN_ID);
        if ((authOpenIdHeaderValues != null)
                && (authOpenIdHeaderValues.length == 1)
                && (authOpenIdHeaderValues[0] != null)
                ) {

            User user = User.findExpertByWeChatOpenId(authOpenIdHeaderValues[0]);
            if (user != null) {
                ctx.args.put("user", user);
                return user.name;
            }

        }

        return null;
    }

    @Override
    public Result onUnauthorized(Context ctx) {
        return unauthorized();
    }
}
