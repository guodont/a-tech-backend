package models;

import models.enums.CategoryType;
import org.joda.time.DateTime;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by llz on 2016/4/12.
 */
@Entity
public class Category extends BaseModel {

    /**
     * 分类表父编号
     */
    public Long pid;
    /**
     * 分类名称
     */
    @Column(length = 45, nullable = false)
    @Constraints.MaxLength(45)
    @Constraints.Required
    public String name;
    /**
     * 分类表类型
     */
    @Enumerated(EnumType.STRING)
    public CategoryType categoryType;
    /**
     * 分类表图片
     */
    @Column(length = 255, nullable = false)
    @Constraints.MaxLength(255)
    @Constraints.Required
    public String image;
    /**
     * 分类表排序
     */
    public Integer sort;


    public static final Finder<Long, Category> find = new Finder<Long, Category>(
            Long.class, Category.class);

    /**
     * 通过分类类型查找分类
     * @param type
     * @return
     */
    public static List<Category> findCategoriesByType(final String type) {
        return find
                .where()
                .eq("categoryType", type)
                .findList();
    }

}
