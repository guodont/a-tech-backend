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
    @Column(length = 255, nullable = false)
    @Constraints.MaxLength(255)
    public String image;

    public static final Finder<Long, Link> find = new Finder
            <Long, Link>(Long.class, Link.class);


    /**
     * 通过id查找分类
     * @param linkId
     * @return
     */
    public static Link findById(final  Long linkId) {
        return find
                .where()
                .eq("id",linkId)
                .findUnique();
    }




}
