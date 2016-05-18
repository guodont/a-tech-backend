////////
// This sample is published as part of the blog article at www.toptal.com/blog
// Visit www.toptal.com/blog and subscribe to our newsletter to read great posts
////////

package controllers;

import controllers.secured.AdminSecured;
import models.*;
import models.enums.TradeState;
import models.enums.TradeType;
import play.data.Form;
import play.data.validation.Constraints;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Security;
import utils.JsonResult;

/**
 * @author guodont
 *         <p>
 *         交易控制器
 */
public class TradeController extends BaseController {

    /**
     * 用户发布交易
     *
     * @return
     */
    @Security.Authenticated(Secured.class)
    public static Result addTrade() {
        Form<TradeForm> postForm = Form.form(TradeForm.class).bindFromRequest();

        if (postForm.hasErrors()) {
            return badRequest(postForm.errorsAsJson());
        } else {
            //  保存交易
            Category category = Category.find.byId(postForm.get().categoryId);
            Trade trade = new Trade();
            trade.clickCount = 0L;
            trade.likeCount = 0L;
            trade.tradeState = TradeState.AUDITED.WAIT_AUDITED;

            if (postForm.get().tradeType.equals(TradeType.DEMAND.getName())) {
                trade.tradeType = TradeType.DEMAND;     // 需求
            } else {
                trade.tradeType = TradeType.SUPPLY;     // 供应
            }

            trade.title = postForm.get().title;
            trade.category = category;
            trade.user = getUser();
            // TODO 保存图片路径 逗号隔开
            trade.images = postForm.get().image;
            trade.description = postForm.get().content;
            trade.save();
        }
        return ok(new JsonResult("success", "Trade added successfully").toJsonResponse());

    }

    /**
     * 审核交易-通过审核
     *
     * @param tradeId
     * @return
     */
    @Security.Authenticated(AdminSecured.class)
    public static Result auditTradeWithAudited(long tradeId) {
        Trade trade = Trade.findTradeById(tradeId);
        trade.tradeState = TradeState.AUDITED;   // 已通过审核
        trade.save();
        return ok(new JsonResult("success", "handl success").toJsonResponse());
    }

    /**
     * 审核交易-审核失败
     *
     * @param tradeId
     * @return
     */
    @Security.Authenticated(AdminSecured.class)
    public static Result auditTradeWithAuditFailed(long tradeId) {
        Trade trade = Trade.findTradeById(tradeId);
        trade.tradeState = TradeState.FAILED;   // 审核失败
        trade.save();
        return ok(new JsonResult("success", "handl success").toJsonResponse());
    }

    /**
     * 获取某用户发布的交易
     *
     * @return
     */
    @Security.Authenticated(Secured.class)
    public static Result getUserTrades() {
        User user = getUser();
        if (user == null) {
            return badRequest(new JsonResult("error", "No such user").toJsonResponse());
        }
        initPageing();
        return ok(Json.toJson(Trade.findTradesByUser(user, page, pageSize)));
    }

    /**
     * 获取交易详情
     *
     * @param id
     * @return
     */
    public static Result getTrade(long id) {
        Trade trade = Trade.findTradeById(id);
        if (trade != null) {
            trade.clickCount += 1;   // 浏览量+1
            trade.save();
            return ok(Json.toJson(Trade.findTradeById(id)));
        } else {
            return badRequest(new JsonResult("error", "No such trade").toJsonResponse());
        }
    }


    /**
     * 获取所有交易
     *
     * @return
     */
    public static Result getTrades() {
        initPageing();
        if (request().getQueryString("category") != null) {
            int categoryId = Integer.parseInt(request().getQueryString("category"));
            Category category = Category.findCategoryById(categoryId);
            return ok(Json.toJson(Trade.findTradesByCategoryAndStatus(category, TradeState.AUDITED.getName(), page, pageSize)));
        } else {
            return ok(Json.toJson(Trade.findTrades(page, pageSize)));
        }
    }

    /**
     * 获取所有交易For Admin
     *
     * @return
     */
    @Security.Authenticated(AdminSecured.class)
    public static Result getTradesForAdmin() {
        initPageing();
        if (request().getQueryString("category") != null) {
            int categoryId = Integer.parseInt(request().getQueryString("category"));
            Category category = Category.findCategoryById(categoryId);
            return ok(Json.toJson(Trade.findTradesByCategory(category, page, pageSize)));
        } else {
            return ok(Json.toJson(Trade.findTradesForAdmin(page, pageSize)));
        }
    }


    /**
     * 删除交易
     *
     * @param id
     * @return
     */
    @Security.Authenticated(AdminSecured.class)
    public static Result deleteTrade(long id) {
        Trade trade = Trade.findTradeById(id);
        trade.delete();
        return ok(new JsonResult("success", "trade deleted").toJsonResponse());
    }

    public static Result updateTrade(long id) {
        return play.mvc.Results.TODO;
    }

    /**
     * 用户收藏交易
     *
     * @return
     */
    @Security.Authenticated(Secured.class)
    public static Result favTrade(long tradeId) {
        Trade trade = Trade.findTradeById(tradeId);

        FavoriteTrade favoriteTrade = new FavoriteTrade();
        favoriteTrade.trade = trade;
        favoriteTrade.user = getUser();
        favoriteTrade.save();            // 保存收藏记录表

        trade.likeCount += 1;    // 收藏数+1
        trade.save();

        return ok(new JsonResult("success", "fav trade success").toJsonResponse());
    }

    /**
     * 用户取消收藏交易
     *
     * @return
     */
    @Security.Authenticated(Secured.class)
    public static Result unFavTrade(long tradeId) {
        Trade trade = Trade.findTradeById(tradeId);

        FavoriteTrade.findFavoriteByTradeIdAndUser(getUser(), trade).delete();   // 删除记录

        trade.likeCount -= 1;    // 收藏数-1
        trade.save();

        return ok(new JsonResult("success", "cancel fav trade success").toJsonResponse());
    }

    /**
     * 获取用户收藏的交易
     *
     * @return
     */
    @Security.Authenticated(Secured.class)
    public static Result getUserFavoriteTrades() {
        initPageing();
        return ok(Json.toJson(FavoriteTrade.findAllFavoritesTradeByUser(getUser(), page, pageSize)));
    }

    /**
     * 发布交易表单数据
     */
    public static class TradeForm {

        @Constraints.Required
        public Long categoryId;     //  分类id

        // @Constraints.Required
        public Long tradeType;     //  交易类型

        @Constraints.Required
        @Constraints.MaxLength(45)
        public String title;        //  交易标题

        @Constraints.Required
        public String content;      //  内容

        public String image;        //  配图

    }
}
