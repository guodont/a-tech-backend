////////
// This sample is published as part of the blog article at www.toptal.com/blog
// Visit www.toptal.com/blog and subscribe to our newsletter to read great posts
////////

package controllers;

import controllers.secured.AdminSecured;
import models.*;
import models.enums.MessageType;
import models.enums.TradeState;
import models.enums.TradeType;
import play.data.Form;
import play.data.validation.Constraints;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Security;
import utils.JPushUtil;
import utils.JsonResult;

import java.util.HashMap;
import java.util.List;

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

        Message message = new Message();
        message.setMessageType(MessageType.TRADE);
        message.setMarkRead(false);
        message.setRelationId(trade.getId());
        message.setTitle("您的交易信息已通过审核");
        message.setUser(trade.user);
        message.setRemark(trade.title);
        message.save();

        HashMap<String, String> extras = new HashMap<String, String>();
        extras.put("id", String.valueOf(trade.getId()));
        extras.put("type", MessageType.TRADE.getName());
        new JPushUtil("您的交易信息已通过审核", trade.title, trade.user.getPhone(), extras).sendPushWith();

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

        Message message = new Message();
        message.setMessageType(MessageType.TRADE);
        message.setMarkRead(false);
        message.setRelationId(trade.getId());
        message.setTitle("您的交易信息未通过审核");
        message.setUser(trade.user);
        message.setRemark(trade.title);
        message.save();

        HashMap<String, String> extras = new HashMap<String, String>();
        extras.put("id", String.valueOf(trade.getId()));
        extras.put("type", MessageType.TRADE.getName());
        new JPushUtil("您的交易信息未通过审核", trade.title, trade.user.getPhone(), extras).sendPushWith();

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


        List<Trade> trades = null;

        if (request().getQueryString("status") != null) {
            trades = Trade.findTradesByUserAndStatus(user, request().getQueryString("status"), page, pageSize);
        } else {
            trades = Trade.findTradesByUserAndStatus(user, null, page, pageSize);
        }

        initPageing();
        return ok(Json.toJson(trades));
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

            // 判断用户的收藏状态
            if (FavoriteTrade.findFavoriteByTradeIdAndUser(getUser(), trade) != null) {
                trade.setFav(true);
            } else {
                trade.setFav(false);
            }
//            trade.user.setFieldSecurity();   // 设置字段安全性

            return ok(Json.toJson(trade));
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
        List<Trade> trades = null;
        String tradeType = TradeType.DEMAND.getName();
        if (request().getQueryString("tradeType") != null && !request().getQueryString("tradeType").equals("")) {
            if (request().getQueryString("tradeType").equals("DEMAND"))
                tradeType = "DEMAND";
            else
                tradeType = "SUPPLY";
        }
        if (request().getQueryString("category") != null && !request().getQueryString("category").equals("")) {
            int categoryId = Integer.parseInt(request().getQueryString("category"));
            Category category = Category.findCategoryById(categoryId);
            trades = Trade.findTradesByCategoryAndStatus(category, TradeState.AUDITED.getName(), tradeType, page, pageSize);
        } else {
            trades = Trade.findTrades(tradeType, page, pageSize);
        }
        for (Trade trade : trades) {
            // 判断用户的收藏状态
            if (FavoriteTrade.findFavoriteByTradeIdAndUser(getUser(), trade) != null) {
                trade.setFav(true);
            } else {
                trade.setFav(false);
            }
            trade.user.setFieldSecurity();   // 设置字段安全性
        }
        return ok(Json.toJson(trades));
    }

    /**
     * 获取所有交易For Admin
     *
     * @return
     */
    @Security.Authenticated(AdminSecured.class)
    public static Result getTradesForAdmin() {

        initPageing();
        String tradeType = TradeType.DEMAND.getName();
        List<Trade> trades = null;

        if (request().getQueryString("tradeType") != null && !request().getQueryString("tradeType").equals("")) {
            if (request().getQueryString("tradeType").equals("DEMAND"))
                tradeType = "DEMAND";
            else
                tradeType = "SUPPLY";
        }
        if (request().getQueryString("category") != null && !request().getQueryString("category").equals("")) {
            int categoryId = Integer.parseInt(request().getQueryString("category"));
            Category category = Category.findCategoryById(categoryId);

            if (request().getQueryString("status") != null && !request().getQueryString("status").equals("")) {
                trades = Trade.findTradesByCategoryForAdmin(request().getQueryString("status"),category, tradeType, page, pageSize);
            } else {
                trades = Trade.findTradesByCategoryForAdmin(null,category, tradeType, page, pageSize);
            }

        } else {

            if (request().getQueryString("status") != null && !request().getQueryString("status").equals("")) {
                trades = Trade.findTradesForAdmin(request().getQueryString("status"),tradeType, page, pageSize);
            } else {
                trades = Trade.findTradesForAdmin(null,tradeType, page, pageSize);
            }
        }

        return ok(Json.toJson(trades));

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

        if (FavoriteTrade.findFavoriteByTradeIdAndUser(getUser(), trade) != null)
            return badRequest(new JsonResult("error", "已收藏过此交易").toJsonResponse());

        if (trade != null) {

            FavoriteTrade favoriteTrade = new FavoriteTrade();
            favoriteTrade.trade = trade;
            favoriteTrade.user = getUser();
            favoriteTrade.save();            // 保存收藏记录表
            trade.likeCount += 1;    // 收藏数+1
            trade.save();

            return ok(new JsonResult("success", "交易收藏成功").toJsonResponse());
        } else {
            return badRequest(new JsonResult("error", "此交易不存在").toJsonResponse());
        }
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

        return ok(new JsonResult("success", "取消收藏成功").toJsonResponse());
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

        @Constraints.Required
        public String tradeType;     //  交易类型

        @Constraints.Required
        @Constraints.MaxLength(45)
        public String title;        //  交易标题

        @Constraints.Required
        public String content;      //  内容

        public String image;        //  配图

    }
}
