package models;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.annotation.JsonIgnore;
import models.enums.FavoriteType;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.List;

/**
 * Created by lzadmin on 2016/4/12 0012.
 * favoriteTrade表的实体类
 */
@Entity
public class FavoriteTrade extends BaseModel {

    /**
     * 被收藏的交易
     */
    @ManyToOne
    @Constraints.Required
    public Trade trade;

    public Trade getTrade() {
        return trade;
    }

    /**
     * 所属用户
     */
    @ManyToOne
    public User user;

    public static final Finder<Long, FavoriteTrade> find = new Finder<Long, FavoriteTrade>(
            Long.class, FavoriteTrade.class);

    /**
     * 获取所有收藏的交易
     *
     * @param page
     * @param pageSize
     * @return
     */
    public static List<FavoriteTrade> findAllFavoritesTrade(int page, int pageSize) {
        return find
                .where()
                .setFirstRow((page - 1) * pageSize)
                .setMaxRows(pageSize)
                .findList();
    }

    /**
     * 获取用户收藏的所有交易
     *
     * @param user
     * @param page
     * @param pageSize
     * @return
     */
    public static List<FavoriteTrade> findAllFavoritesTradeByUser(User user, int page, int pageSize) {
        return find
                .where()
                .eq("user", user)
                .setFirstRow((page - 1) * pageSize)
                .setMaxRows(pageSize)
                .findList();
    }

    public static FavoriteTrade findFavoriteByTradeIdAndUser(User user, Trade trade) {
        return find
                .where()
                .eq("user", user)
                .eq("trade", trade)
                .findUnique();
    }
}
