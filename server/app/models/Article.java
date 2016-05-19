package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.PagingList;
import models.enums.ArticlePushState;
import models.enums.ArticleState;
import models.enums.ArticleType;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.List;

/**
 * Created by lzadmin on 2016/4/12 0012.
 * article表实体类
 */
@Entity
public class Article extends BaseModel {
    /**
     * 标题
     */
    public String title;
    /**
     * 内容
     */
    @Column(columnDefinition = "TEXT")
    @Constraints.Required
    public String content;
    /**
     * 关键字
     */
    @Column(length = 45)
    @Constraints.MaxLength(45)
    public String tag;
    /**
     * 文章分类
     */
    @ManyToOne
    public Category category;
    /**
     * 发布管理员
     */
    @ManyToOne
    public Admin admin;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Comment> comments;
    /**
     * 排序
     */
    public Integer sort;
    /**
     * 所属专家
     */
    @ManyToOne
    public User user;
    /**
     * 点击次数
     */
    public Long clickCount;
    /**
     * 评论数
     */
    public Long commentCount;
    /**
     * 图片
     */
    @Column(length = 255)
    @Constraints.MaxLength(255)
    public String image;
    /**
     * 文章类型
     */
    @Enumerated(EnumType.STRING)
    public ArticleType articleType;
    /**
     * 提交状态
     */
    @Enumerated(EnumType.STRING)
    public ArticleState articleState;
    /**
     * 是否推送
     */
    @Enumerated(EnumType.STRING)
    public ArticlePushState articlePushState;

    public static final Finder<Long, Article> find = new Finder<Long, Article>(
            Long.class, Article.class);


    /**
     * @param admin
     * @return
     */
    public static List<Article> findArticlesByAdmin(final Admin admin) {
        return find
                .where()
                .eq("admin", admin)
                .setOrderBy("whenCreated desc , sort desc")
                .findList();
    }

    public static List<Article> findArticlesByType(String articleType, int page, int pageSize) {
        return find
                .where()
                .eq("articleType", articleType)
                .setOrderBy("whenCreated desc , sort desc")
                .setFirstRow((page - 1) * pageSize)
                .setMaxRows(pageSize)
                .findList();
    }

    public static List<Article> findArticlesByCategory(Long categoryId, int page, int pageSize) {
        return find
                .where()
                .eq("categoryId", categoryId)
                .setOrderBy("whenCreated desc , sort desc")
                .setFirstRow((page - 1) * pageSize)
                .setMaxRows(pageSize)
                .findList();
    }

    /**
     * 通过用户查找文章
     *
     * @param user
     * @return
     */
    public static List<Article> findArticlesByUser(final User user, int page, int pageSize) {
        return find
                .where()
                .eq("user", user)
                .setOrderBy("whenCreated desc")
                .setFirstRow((page - 1) * pageSize)
                .setMaxRows(pageSize)
                .findList();
    }

    /**
     * 通过id查找文章
     *
     * @param id
     * @return
     */
    public static Article findArticleById(final Long id) {
        return find
                .where()
                .eq("id", id)
                .findUnique();
    }
}
