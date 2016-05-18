package models;

import play.data.validation.Constraints;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * 图片资源实体类
 */
@Entity
public class Image extends BaseModel {

    /**
     * 图片名称
     */
    @Column(length = 255, nullable = false)
    @Constraints.MaxLength(255)
    @Constraints.Required
    public String name;

    /**
     * 图片原名称
     */
    @Column(length = 255, nullable = false)
    @Constraints.MaxLength(255)
    @Constraints.Required
    public String oldName;

    /**
     * 图片路径
     */
    @Column(length = 255, nullable = false)
    @Constraints.MaxLength(255)
    @Constraints.Required
    public String src;

    /**
     * 所属用户
     */
    @ManyToOne
    public User user;
}
