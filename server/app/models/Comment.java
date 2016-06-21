package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import models.enums.TradeState;
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
    public Article article;
    /**
     * 用户id
     */
    @ManyToOne
    public User user;
    /**
     * 评论内容
     */
    @Column(columnDefinition = "TEXT")
    public String content;

    /**
     * 审核状态
     */
    @Enumerated(EnumType.STRING)
    public TradeState auditState;

    public static final Finder<Long, Comment> find = new Finder<Long, Comment>(
            Long.class, Comment.class);

    /**
     * 通过文章查询所有评论
     * @param article
     * @return
     */
    public static List<Comment> findAllCommentsByArticle(final Article article, int page, int pageSize) {
        return find
                .where()
                .eq("article", article)
                .eq("auditState", TradeState.AUDITED)
                .setOrderBy("whenCreated desc")
                .setFirstRow((page - 1) * pageSize)
                .setMaxRows(pageSize)
                .findList();
    }

    /**
     * 查询所有评论
     * @return
     */
    public static List<Comment> findAllComments(int page, int pageSize) {
        return find
                .setFirstRow((page - 1) * pageSize)
                .setMaxRows(pageSize)
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
                .setOrderBy("whenCreated desc")
                .findList();
    }

    /**
     * 通过ID查询评论
     * @param id
     * @return
     */
    public static Comment findCommentById(final long id) {
        return find
                .where()
                .eq("id", id)
                .findUnique();
    }
}
