package models;

import play.data.validation.Constraints;

import javax.persistence.*;

/**
 * Created by llz on 2016/4/13.
 */
@Entity
public class Video extends BaseModel {

    /**
     * 名称
     */
    @Column(length = 45, nullable = false)
    @Constraints.MaxLength(45)
    @Constraints.Required
    public String name;
    /**
     * 描述
     */
    @Column(columnDefinition = "TEXT")
    @Constraints.Required
    public String description;
    /**
     * 文件路径
     */
    @Column(length = 255, nullable = false)
    @Constraints.MaxLength(255)
    @Constraints.Required
    public String path;
    /**
     * 管理员
     */
    @ManyToOne
    public Admin admin;
    /**
     * 所属分类
     */
    @ManyToOne
    public Category category;
    /**
     * 点击数
     */
    public Long clickCount;


}
