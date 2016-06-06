package models;

import com.avaje.ebean.Ebean;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import scala.Int;

import javax.persistence.*;
import java.util.List;

/**
 * Created by j on 2016/4/13.
 */
@Entity
public class Expert extends BaseModel {

    /**
     * 用户信息
     */
    @OneToOne
    public User user;
    /**
     * 所属分类
     */
    @ManyToOne
    public Category category;
    /**
     * 职称
     */
    @Column(length = 45)
    @Constraints.MaxLength(45)
    public String professional;
    /**
     * 职务
     */
    @Column(length = 45)
    @Constraints.MaxLength(45)
    public String duty;
    /**
     * 简介
     */
    @Column(length = 255)
    @Constraints.MaxLength(255)
    public String introduction;
    /**
     * 服务项目
     */
    @Column(columnDefinition = "TEXT")
    public String service;
    /**
     * 所在单位
     */
    @Column(length = 45)
    @Constraints.MaxLength(45)
    public String company;

    public static final Finder<Long, Expert> find = new Finder<Long, Expert>(
            Long.class, Expert.class);


    /**
     * 通过分类查找专家
     *
     * @param category
     * @return
     */
    public static List<Expert> findExpertsByCategory(final Category category, int page, int pageSize) {
        return find
                .where()
                .eq("category", category)
                .setOrderBy("whenCreated desc")
                .setFirstRow((page - 1) * pageSize)
                .setMaxRows(pageSize)
                .findList();
    }

    /**
     * 通过用户查找专家
     *
     * @param user
     * @return
     */
    public static Expert findExpertByUser(final User user) {
        return find
                .where()
                .eq("user", user)
                .findUnique();
    }

    public static List<Expert> findExperts(int page, int pageSize) {
        return find
                .where()
                .setOrderBy("whenCreated desc")
                .setFirstRow((page - 1) * pageSize)
                .setMaxRows(pageSize)
                .findList();
    }

    /**
     * 通过id查找专家
     *
     * @param id
     * @return
     */
    public static Expert findExpertById(final Long id) {
        return find
                .where()
                .eq("id", id)
                .findUnique();
    }


}
