package models;

import models.enums.FavoriteType;
import org.joda.time.DateTime;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.List;

/**
 * Created by lzadmin on 2016/4/12 0012.
 * favorite表的实体类
 */
@Entity
public class Favorite extends BaseModel {

    /**
     * 被收藏的id
     */
    @Constraints.Required
    public Long beFavId;
    /**
     * 收藏的类型
     */
    @Enumerated(EnumType.STRING)
    public FavoriteType favoriteType;
    /**
     * 所属用户
     */
    @ManyToOne
    public User user;

    public static final Finder<Long, Favorite> find = new Finder<Long, Favorite>(
            Long.class, Favorite.class);

    /**
     * 根据用户和类型查找收藏
     * @param user
     * @param favoriteType
     * @return
     */
    public static List<Favorite> findAllFavoritesByUserAndType(final User user, String favoriteType) {
        return find
                .where()
                .eq("user", user)
                .eq("favoriteType", favoriteType)
                .findList();
    }

}
