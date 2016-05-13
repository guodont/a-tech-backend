package utils;

import static play.core.j.JavaResults.BadRequest;
import static play.core.j.JavaResults.InternalServerError;
import static play.core.j.JavaResults.NotFound;

import java.util.ArrayList;
import java.util.List;

import play.GlobalSettings;
import play.api.mvc.Results.Status;
import play.libs.F.Promise;
import play.libs.Scala;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import scala.Tuple2;
import scala.collection.Seq;

public class Global extends GlobalSettings {

    private class ActionWrapper extends Action.Simple {
        public ActionWrapper(Action<?> action) {
            this.delegate = action;
        }

        @Override
        public Promise<Result> call(Http.Context ctx) throws java.lang.Throwable {
            Promise<Result> result = this.delegate.call(ctx);
            Http.Response response = ctx.response();
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Headers", "*");
            return result;
        }
    }

    /*
    * Adds the required CORS header "Access-Control-Allow-Origin" to successfull requests
    */
    @Override
    public Action<?> onRequest(Http.Request request, java.lang.reflect.Method actionMethod) {
        return new ActionWrapper(super.onRequest(request, actionMethod));
    }

    private static class CORSResult implements Result {
        final private play.api.mvc.Result wrappedResult;

        public CORSResult(Status status) {
            List<Tuple2<String, String>> list = new ArrayList<Tuple2<String, String>>();
            Tuple2<String, String> t = new Tuple2<String, String>("Access-Control-Allow-Origin","*");
            Tuple2<String, String> t2 = new Tuple2<String, String>("Access-Control-Allow-Headers","*");
            list.add(t);
            list.add(t2);
            Seq<Tuple2<String, String>> seq = Scala.toSeq(list);
            wrappedResult = status.withHeaders(seq);
        }

        public play.api.mvc.Result toScala() {
            return this.wrappedResult;
        }
    }

    /*
    * Adds the required CORS header "Access-Control-Allow-Origin" to bad requests
    */
    @Override
    public Promise<Result> onBadRequest(Http.RequestHeader request, String error) {
        return Promise.<Result>pure(new CORSResult(BadRequest()));
    }

    /*
    * Adds the required CORS header "Access-Control-Allow-Origin" to requests that causes an exception
    */
    @Override
    public Promise<Result> onError(Http.RequestHeader request, Throwable t) {
        return Promise.<Result>pure(new CORSResult(InternalServerError()));
    }

    /*
    * Adds the required CORS header "Access-Control-Allow-Origin" when a route was not found
    */
    @Override
    public Promise<Result> onHandlerNotFound(Http.RequestHeader request) {
        return Promise.<Result>pure(new CORSResult(NotFound()));
    }

}