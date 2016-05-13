////////
// This sample is published as part of the blog article at www.toptal.com/blog
// Visit www.toptal.com/blog and subscribe to our newsletter to read great posts
////////

package controllers.secured;

import controllers.SecurityController;
import models.Admin;
import models.User;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;

/**
 * Implements basic authentication
 *
 * 专家用户权限控制
 */
public class ExpertSecured extends Security.Authenticator {

  @Override
  public String getUsername(Context ctx) {
    String[] authTokenHeaderValues = ctx.request().headers().get(SecurityController.AUTH_TOKEN_HEADER);
    if ((authTokenHeaderValues != null) && (authTokenHeaderValues.length == 1) && (authTokenHeaderValues[0] != null)) {
      User user = models.User.findExpertByAuthToken(authTokenHeaderValues[0]);
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
