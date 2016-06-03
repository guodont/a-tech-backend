package models;

import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.List;


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
     * 视频缩略图
     */
    @Constraints.MaxLength(255)
    public String thumbnail;

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

    public static final Finder<Long, Video> find = new Finder<Long, Video>(
            Long.class, Video.class);

    /**
     * 查找全部视频
     *
     * @param
     * @return
     */
    public static List<Video> findVideos( int page, int pageSize) {
        return find
                .where()
                .setOrderBy("whenCreated desc")
                .setFirstRow((page - 1) * pageSize)
                .setMaxRows(pageSize)
                .findList();
    }

    /**
     * 通过视频ID查找视频
     *
     * @param id
     * @return
     */
    public static Video findVideoById(final Long id) {
        return find
                .where()
                .eq("id", id)
                .findUnique();
    }
}
