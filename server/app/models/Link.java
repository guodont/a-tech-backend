package models;

import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.List;

/**
 * Created by j on 2016/4/13.
 * 友情链接
 */
@Entity
public class Link extends BaseModel{

    /**
     * 名称
     */
    @Column(length = 45, nullable = false)
    @Constraints.MaxLength(45)
    @Constraints.Required
    public String name;

    /**
     * 地址
     */
    @Column(length = 45, nullable = false)
    @Constraints.MaxLength(45)
    @Constraints.Required
    public String url;

    /**
     * 图片
     */
    @Column(length = 255)
    @Constraints.MaxLength(255)
    public String image;


    public static final Finder<Long,Link> find =new Finder<Long, Link>(Long.class,Link.class);

    /**
     * 通过id查询链接
     */
    public static Link findLinkById(final Long id){
        return find
                .where()
                .eq("id", id)
                .findUnique();
    }

    /**
     * 查找所有链接
     */
    public static List<Link> findAlllinks(int page, int pageSize){
        return find
                .where()
                .setOrderBy("whenCreated desc")
                .setFirstRow((page - 1) * pageSize)
                .setMaxRows(pageSize)
                .findList();
    }
}
