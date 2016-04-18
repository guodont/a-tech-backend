package models;

import play.data.validation.Constraints;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * 动态图片
 * Created by guodont on 16/4/17.
 */
@Entity
public class TrendImage extends BaseModel {

    /**
     * 图片名称
     */
    @Column(length = 255, nullable = false)
    @Constraints.MaxLength(255)
    @Constraints.Required
    public String name;

    /**
     * 图片路径
     */
    @Column(length = 255, nullable = false)
    @Constraints.MaxLength(255)
    @Constraints.Required
    public String src;

    /**
     * 所属动态
     */
    @ManyToOne
    public Trend trend;
}
