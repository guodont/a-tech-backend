package models;

import models.enums.QuestionAuditState;
import models.enums.QuestionResolveState;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.List;

/**
 * Created by j on 2016/4/13.
 * 问题model
 */
@Entity
public class Question extends BaseModel {

    /**
     * 所属分类
     */
    @ManyToOne
    public Category category;
    /**
     * 标题
     */
    @Column(length = 45, nullable = false)
    @Constraints.MaxLength(45)
    @Constraints.Required
    public String title;
    /**
     * 内容
     */
    @Column(columnDefinition = "TEXT")
    @Constraints.Required
    public String content;
    /**
     * 浏览数
     */
    public Long clickCount;
    /**
     * 收藏数
     */
    public Long likeCount;
    /**
     * 指定专家
     */
    @ManyToOne
    public User expert;
    /**
     * 提问用户
     */
    @ManyToOne
    public User user;
    /**
     * 问题审核状态
     */
    @Enumerated(EnumType.STRING)
    public QuestionAuditState questionAuditState;
    /**
     * 问题解决状态
     */
    @Enumerated(EnumType.STRING)
    public QuestionResolveState questionResolveState;

    @Constraints.MaxLength(255)
    public String images;

    /**
     * 问题的回答
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public List<Answer> answers;

    @Access(value = AccessType.PROPERTY)
    private boolean isFav;

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }

    /**
     * 问题图片
     */
//    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
//    public List<QuestionImage> questionImages;

    public static final Finder<Long, Question> find = new Finder<Long, Question>(
            Long.class, Question.class);

    /**
     * 查找用户的所有问题
     *
     * @param user
     * @return
     */
    public static List<Question> findQuestionsByUser(final User user, int page, int pageSize) {
        return find
                .where()
                .eq("user", user)
                .setOrderBy("whenCreated desc")
                .setFirstRow((page - 1) * pageSize)
                .setMaxRows(pageSize)
                .findList();
    }

    /**
     * 查找指派给专家的问题
     *
     * @param expert
     * @return
     */
    public static List<Question> findQuestionsByExpertAndStatus(final User expert, String questionResolveState, int page, int pageSize) {

        if (questionResolveState != null) {
            return find
                    .where()
                    .eq("user", expert)
                    .eq("questionResolveState", questionResolveState)
                    .setOrderBy("whenCreated desc")
                    .setFirstRow((page - 1) * pageSize)
                    .setMaxRows(pageSize)
                    .findList();
        } else {
            return find
                    .where()
                    .eq("user", expert)
                    .setOrderBy("whenCreated desc")
                    .setFirstRow((page - 1) * pageSize)
                    .setMaxRows(pageSize)
                    .findList();
        }

    }

    /**
     * 查找分类下的所有问题
     *
     * @param category
     * @return
     */
    public static List<Question> findQuestionsByCategory(final Category category, int page, int pageSize) {
        return find
                .where()
                .eq("category", category)
                .setOrderBy("whenCreated desc")
                .setFirstRow((page - 1) * pageSize)
                .setMaxRows(pageSize)
                .findList();
    }

    /**
     * 获取所有问题
     *
     * @param page
     * @param pageSize
     * @return
     */
    public static List<Question> findQuestions(int page, int pageSize) {
        return find
                .where()
                .setOrderBy("whenCreated desc")
                .setFirstRow((page - 1) * pageSize)
                .setMaxRows(pageSize)
                .findList();
    }

    /**
     * 通过Id查找问题
     *
     * @param id
     * @return
     */
    public static Question findQuestionById(final Long id) {
        return find
                .where()
                .eq("id", id)
                .findUnique();
    }
}
