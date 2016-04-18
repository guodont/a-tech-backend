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
    public Expert expert;
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


    public static final Finder<Long, Question> find = new Finder<Long, Question>(
            Long.class, Question.class);

    /**
     * 查找用户的所有问题
     * @param user
     * @return
     */
    public static List<Question> findQuestionsByUser(final User user) {
        return find
                .where()
                .eq("user", user)
                .findList();
    }

    /**
     * 查找指派给专家的问题
     * @param expert
     * @return
     */
    public static List<Question> findQuestionsByExpertAndStatus(final Expert expert, String questionResolveState) {
        return find
                .where()
                .eq("expert", expert)
                .eq("questionResolveState", questionResolveState)
                .findList();
    }

    /**
     * 查找分类下的所有问题
     * @param category
     * @return
     */
    public static List<Question> findQuestionsByCategory(final Category category) {
        return find
                .where()
                .eq("category", category)
                .findList();
    }
}
