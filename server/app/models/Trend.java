package models;


import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.List;

/**
 * Created by lzadmin on 2016/4/12 0012.
 * 专家动态
 */
@Entity
public class Trend extends BaseModel {

    /**
     * 所属用户
     */
    @ManyToOne
    public User user;
    /**
     * 动态内容
     */
    @Column(length = 255, nullable = false)
    @Constraints.MaxLength(255)
    @Constraints.Required
    public String content;
    /**
     * 配图
     */
    @Constraints.MaxLength(255)
    public String images;

    public static final Finder<Long, Trend> find = new Finder<Long, Trend>(
            Long.class, Trend.class);


    public static List<Trend> findExpertsByUser(final User user, int page, int pageSize) {
        return find
                .where()
                .eq("user", user)
                .setOrderBy("whenCreated desc")
                .setFirstRow((page - 1) * pageSize)
                .setMaxRows(pageSize)
                .findList();
    }
}