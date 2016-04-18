package controllers;

import play.mvc.Controller;
import play.mvc.Security;

/**
 * Created by guodont on 16/4/18.
 */
@Security.Authenticated(Secured.class)
public class BaseAdminController extends Controller{

}
