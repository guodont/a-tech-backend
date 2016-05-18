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

}