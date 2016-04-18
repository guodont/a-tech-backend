package models;

import models.enums.Position;
import org.joda.time.DateTime;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by llz on 2016/4/13.
 */
@Entity
public class Advertisement extends BaseModel {

    /**
     *广告名称
     */
    @Column(length = 45, nullable = false)
    @Constraints.MaxLength(45)
    @Constraints.Required
    public String name;
    /**
     *广告描述
     */
    @Column(length = 255, nullable = false)
    @Constraints.MaxLength(255)
    @Constraints.Required
    public String description;
    /**
     *广告链接
     */
    @Column(length = 255, nullable = false)
    @Constraints.MaxLength(255)
    @Constraints.Required
    public String url;
    /**
     *广告图片
     */
    @Column(length = 255, nullable = false)
    @Constraints.MaxLength(255)
    @Constraints.Required
    public String image;

    /**
     *点击数
     */
    public Long clickCount;
    /**
     *位置
     */
    @Enumerated(EnumType.STRING)
    public Position position;

    public static final Finder<Long, Advertisement> find = new Finder<Long, Advertisement>(
            Long.class, Advertisement.class);

    /**
     * 通过位置参数查找广告
     * @param position
     * @return
     */
    public static List<Advertisement> findAdvertisementsByPos(final String position) {
        return find
                .where()
                .eq("position", position)
                .findList();
    }

    /**
     * 通过id查找广告
     * @param id
     * @return
     */
    public static Advertisement findAdvById(final Long id) {
        return find
                .where()
                .eq("id", id)
                .findUnique();
    }

}
