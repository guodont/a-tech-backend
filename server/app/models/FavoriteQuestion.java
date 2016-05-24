package models;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.annotation.JsonIgnore;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.util.List;

/**
 * Created by lzadmin on 2016/4/12 0012.
 * favoriteQuestion表的实体类
 */
@Entity
public class FavoriteQuestion extends BaseModel {

    /**
     * 被收藏的问题
     */
    @ManyToOne
    public Question question;

    /**
     * 所属用户
     */
    @ManyToOne
    public User user;

    public static final Finder<Long, FavoriteQuestion> find = new Finder<Long, FavoriteQuestion>(
            Long.class, FavoriteQuestion.class);

    /**
     * 获取所有收藏的问题
     *
     * @param page
     * @param pageSize
     * @return
     */
    public static List<FavoriteQuestion> findAllFavoritesQuestion(int page, int pageSize) {
        return find
                .where()
                .setFirstRow((page - 1) * pageSize)
                .setMaxRows(pageSize)
                .findList();
    }

    /**
     * 获取用户收藏的所有问题
     *
     * @param user
     * @param page
     * @param pageSize
     * @return
     */
    public static List<FavoriteQuestion> findAllFavoritesQuestionByUser(User user, int page, int pageSize) {
        return find
                .where()
                .eq("user", user)
                .setFirstRow((page - 1) * pageSize)
                .setMaxRows(pageSize)
                .findList();
    }

    public static FavoriteQuestion findFavoriteByQuestionAndUser(User user, Question question) {
        return find
                .where()
                .eq("user", user)
                .eq("question", question)
                .findUnique();
    }
}
