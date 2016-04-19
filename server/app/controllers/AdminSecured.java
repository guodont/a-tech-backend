////////
// This sample is published as part of the blog article at www.toptal.com/blog
// Visit www.toptal.com/blog and subscribe to our newsletter to read great posts
////////

package controllers;

import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;

/**
 * Implements basic authentication
 *
 * 管理员用户权限控制
 */
public class AdminSecured extends Security.Authenticator {

  @Override
  public String getUsername(Context ctx) {
    // 判断是否是管理员
    if (ctx.session().get("isAdmin").equals("true")) {
      return ctx.session().get("username");
    } else {
      return null;
    }
  }

  @Override
  public Result onUnauthorized(Context ctx) {
    return unauthorized();
  }
}
