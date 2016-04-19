package utils;

import play.GlobalSettings;
import play.libs.F;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import views.html.error_400;
import views.html.error_404;
import views.html.error_500;


public class Global extends GlobalSettings {

    @Override
    public F.Promise<Result> onError(Http.RequestHeader request, Throwable t) {

        String errorMessage = "oops! 服务器开小差了, 请过会儿再来吧。";

        Result result;
        boolean jsonResponse = isJsonResponse(request);
        if(jsonResponse) {
            //需要返回json结果
            result = Results.internalServerError(new JsonResult("error", errorMessage).toJsonResponse());
        } else {
            result = show500();
        }
        return showResult(result, jsonResponse);
    }

    @Override
    public F.Promise<Result> onHandlerNotFound(Http.RequestHeader request) {
        String errorMessage = "您请求的页面没有找到，去其他地方逛逛吧";
        Result result;
        boolean jsonResponse = isJsonResponse(request);
        if(jsonResponse) {
            //需要返回json结果
            result = Results.notFound(new JsonResult("error", errorMessage).toJsonResponse());
        } else {
            result = show404();
        }
        return showResult(result, jsonResponse);
    }

    @Override
    public F.Promise<Result> onBadRequest(Http.RequestHeader request, String error) {
        String errorMessage = "您请求的参数有误";
        Result result;
        boolean jsonResponse = isJsonResponse(request);
        if(jsonResponse) {
            //需要返回json结果
            result = Results.badRequest(new JsonResult("error", errorMessage).toJsonResponse());
        } else {
            result = show400();
        }
        return showResult(result, jsonResponse);

    }

    public static Result show404() {
        return Results.notFound(error_404.render());
    }

    public static Result show500() {
        return Results.internalServerError(error_500.render());
    }

    public static Result show400() {
        return Results.badRequest(error_400.render());
    }


    protected boolean isJsonResponse(Http.RequestHeader request) {
        return request.acceptedTypes().stream().filter(mr -> mr.toString().contains("json")).findFirst().isPresent();
    }

    protected F.Promise<Result> showResult(Result result, boolean jsonResponse) {
        if (!jsonResponse) {
            return null;
        } else {
            return F.Promise.pure(result);
        }
    }

}