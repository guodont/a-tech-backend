package models;

import models.enums.TradeState;
import models.enums.TradeType;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by lzadmin on 2016/4/12 0012.
 * trade表实体类
 */
@Entity
public class Trade extends BaseModel {

    /**
     * 交易标题
     */
    @Column(length = 45, nullable = false)
    @Constraints.MaxLength(45)
    @Constraints.Required
    public String title;
    /**
     * 交易描述
     */
    @Column(length = 255, nullable = false)
    @Constraints.MaxLength(255)
    @Constraints.Required
    public String description;
    /**
     * 所属用户
     */
    @ManyToOne
    public User user;
    /**
     * 点击数
     */
    public Long clickCount;
    /**
     * 收藏数
     */
    public Long likeCount;
    /**
     * 结束时间
     */
    public Timestamp endTime;
    /**
     * 交易类型 供应/需求
     */
    @Enumerated(EnumType.STRING)
    public TradeType tradeType;
    /**
     * 所属分类
     */
    @ManyToOne
    public Category category;
    /**
     * 交易状态
     */
    @Enumerated(EnumType.STRING)
    public TradeState tradeState;

    @Column(columnDefinition = "TEXT")
    public String images;


    @Access(value = AccessType.PROPERTY)
    private boolean isFav;

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }

    /**
     * 交易图片
     */
//    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
//    public List<TradeImage> tradeImages;

    public static final Finder<Long, Trade> find = new Finder<Long, Trade>(
            Long.class, Trade.class);

    /**
     * 查找分类下的所有交易
     *
     * @param category
     * @return
     */
    public static List<Trade> findTradesByCategoryAndStatus(final Category category, String state, String tradeType, int page, int pageSize) {
        return find
                .where()
                .eq("category", category)
                .eq("tradeState", state)
                .eq("tradeType", tradeType)
                .setOrderBy("whenCreated desc")
                .setFirstRow((page - 1) * pageSize)
                .setMaxRows(pageSize)
                .findList();
    }

    public static List<Trade> findTradesByCategory(final Category category, String tradeType, int page, int pageSize) {
        return find
                .where()
                .eq("category", category)
                .eq("tradeState", TradeState.AUDITED.getName())
                .eq("tradeType", tradeType)
                .setOrderBy("whenCreated desc")
                .setFirstRow((page - 1) * pageSize)
                .setMaxRows(pageSize)
                .findList();
    }

    public static List<Trade> findTradesByCategoryForAdmin(String state, final Category category, String tradeType, int page, int pageSize) {
        if (state != null) {
            return find
                    .where()
                    .eq("category", category)
                    .eq("tradeType", tradeType)
                    .eq("tradeState", state)
                    .setOrderBy("whenCreated desc")
                    .setFirstRow((page - 1) * pageSize)
                    .setMaxRows(pageSize)
                    .findList();
        } else {
            return find
                    .where()
                    .eq("category", category)
                    .eq("tradeType", tradeType)
                    .setOrderBy("whenCreated desc")
                    .setFirstRow((page - 1) * pageSize)
                    .setMaxRows(pageSize)
                    .findList();
        }

    }

    public static List<Trade> findTrades(String tradeType, int page, int pageSize) {
        return find
                .where()
                .eq("tradeState", TradeState.AUDITED.getName())
                .eq("tradeType", tradeType)
                .setOrderBy("whenCreated desc")
                .setFirstRow((page - 1) * pageSize)
                .setMaxRows(pageSize)
                .findList();
    }

    public static List<Trade> findTradesForAdmin(String state, String tradeType, int page, int pageSize) {
        if (state != null) {

            return find
                    .where()
                    .eq("tradeType", tradeType)
                    .eq("tradeState", state)
                    .setOrderBy("whenCreated desc")
                    .setFirstRow((page - 1) * pageSize)
                    .setMaxRows(pageSize)
                    .findList();
        } else {

            return find
                    .where()
                    .eq("tradeType", tradeType)
                    .setOrderBy("whenCreated desc")
                    .setFirstRow((page - 1) * pageSize)
                    .setMaxRows(pageSize)
                    .findList();
        }
    }

    /**
     * 根据Id获取交易信息
     *
     * @param tradeId
     * @return
     */
    public static Trade findTradeById(long tradeId) {
        return find
                .where()
                .eq("id", tradeId)
//                .eq("tradeState", TradeState.AUDITED.getName())
                .findUnique();
    }

    public static List<Trade> findTradesByUser(User user, int page, int pageSize) {
        return find
                .where()
                .eq("user", user)
                .setOrderBy("whenCreated desc")
                .setFirstRow((page - 1) * pageSize)
                .setMaxRows(pageSize)
                .findList();
    }

    public static List<Trade> findNewTrades(int page, int pageSize) {
        return find
                .where()
                .between("whenCreated", new Timestamp(System.currentTimeMillis() - 5 * 60 * 1000), new Timestamp(System.currentTimeMillis()))
                .setOrderBy("whenCreated desc")
                .setFirstRow((page - 1) * pageSize)
                .setMaxRows(pageSize)
                .findList();
    }

    public static List<Trade> findTradesByUserAndStatus(final User user, String tradeState, int page, int pageSize) {

        if (tradeState != null) {
            return find
                    .where()
                    .eq("user", user)
                    .eq("tradeState", tradeState)
                    .setOrderBy("whenCreated desc")
                    .setFirstRow((page - 1) * pageSize)
                    .setMaxRows(pageSize)
                    .findList();
        } else {
            return find
                    .where()
                    .eq("user", user)
                    .setOrderBy("whenCreated desc")
                    .setFirstRow((page - 1) * pageSize)
                    .setMaxRows(pageSize)
                    .findList();
        }

    }
}