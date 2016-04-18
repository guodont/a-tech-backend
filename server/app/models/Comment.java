package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.joda.time.DateTime;
import play.db.ebean.Model;
import javax.persistence.*;
import java.util.List;

/**
 * Created by j on 2016/4/12.
 */
@Entity
public class Comment extends BaseModel {
    /**
     * 所属文章
     */
    @ManyToOne
    @JsonIgnore
    private Article article;
    /**
     * 用户id
     */
    @ManyToOne
    private User user;
    /**
     * 评论内容
     */
    @Column(columnDefinition = "TEXT")
    private String content;

    public static final Finder<Long, Comment> find = new Finder<Long, Comment>(
            Long.class, Comment.class);

    /**
     * 通过文章查询所有评论
     * @param article
     * @return
     */
    public static List<Comment> findAllCommentsByArticle(final Article article) {
        return find
                .where()
                .eq("article", article)
                .findList();
    }

    /**
     * 查询某用户的所有评论
     * @param user
     * @return
     */
    public static List<Comment> findAllCommentsByUser(final User user) {
        return find
                .where()
                .eq("user", user)
                .findList();
    }
}
