package models;

import play.data.validation.Constraints;

import javax.persistence.*;

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
}
