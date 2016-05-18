package models;

import org.joda.time.DateTime;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by lzadmin on 2016/4/12 0012.
 * album表实体类
 */
@Entity
public class Album extends BaseModel {

    /**
     * 照片名
     */
    @Column(length = 45, nullable = false)
    @Constraints.MaxLength(45)
    @Constraints.Required
    public String name;
    /**
     * 照片路径
     */
    @Column(length = 255, nullable = false)
    @Constraints.MaxLength(255)
    @Constraints.Required
    public String path;
    /**
     * 所属用户(专家)
     */
    @ManyToOne
    public User user;
    /**
     * 照片描述
     */
    @Column(length = 255, nullable = false)
    @Constraints.MaxLength(255)
    @Constraints.Required
    public String description;

    public static final Finder<Long, Album> find = new Finder<Long, Album>(
            Long.class, Album.class);

    public static List<Album> findAlbumsByUser(final User user) {
        return find
                .where()
                .eq("user", user)
                .findList();
    }

    public static Album findAlbumById(final Long id) {
        return find
                .where()
                .eq("id", id)
                .findUnique();
    }
}