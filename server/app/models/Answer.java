package models;


import org.joda.time.DateTime;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by j on 2016/4/12.
 */
@Entity
public class Answer extends BaseModel {

    /**
     * 所属问题
     */
    @ManyToOne
    public Question question;

    /**
     * 回答内容
     */
    @Column(columnDefinition = "TEXT")
    @Constraints.Required
    public String content;

    /**
     * 回答的专家
     */
    @ManyToOne
    public User user;

    public static final Finder<Long, Answer> find = new Finder<Long, Answer>(
            Long.class, Answer.class);

    /**
     * 查找一个问题的所有答案
     * @param question
     * @return
     */
    public static List<Answer> findAllAnswersByQuestion(final Question question) {
        return find
                .where()
                .eq("question", question)
                .findList();
    }

    /**
     * 查找专家的所有回答
     * @param user
     * @return
     */
    public static List<Answer> findAllAnswersByUser(final User user) {
        return find
                .where()
                .eq("user", user)
                .findList();
    }

}
