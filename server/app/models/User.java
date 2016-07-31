////////
// This sample is published as part of the blog article at www.toptal.com/blog
// Visit www.toptal.com/blog and subscribe to our newsletter to read great posts
////////

package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;
import javax.persistence.*;

import controllers.secured.AdminSecured;
import models.enums.UserType;
import play.data.validation.Constraints;

/**
 * Model representing a Blog user
 */
@Entity
public class User extends BaseModel {

    /**
     * 用户邮箱
     */
    @Column(length = 255, unique = true)
    @Constraints.MaxLength(255)
    @Constraints.Email
    private String email;

    /**
     * 用户密码
     */
    @Column(length = 64, nullable = false)
    private byte[] shaPassword;
    /**
     * 用户类型
     */
    @Column(length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    public UserType userType;
    /**
     * 用户地址
     */
    @Column(length = 255)
    @Constraints.MaxLength(255)
    public String address;
    /**
     * 用户真实姓名
     */
    @Column(length = 45)
    private String realName;
    /**
     * 用户电话
     */
    @Column(length = 11, unique = true, nullable = false)
    @Constraints.Required
    private String phone;
    /**
     * 用户名
     */
    @Column(length = 45, unique = true, nullable = false)
    public String name;
    /**
     * 用户头像
     */
    @Column(length = 255)
    public String avatar;
    /**
     * 用户经营的产业
     */
    @Column(length = 45)
    public String industry;
    /**
     * 用户产业规模
     */
    @Column(length = 45)
    public String scale;

    /**
     * 最后登录IP
     */
    @Column(length = 45, nullable = false)
    @Constraints.MaxLength(45)
    private String lastIp;

    /**
     * 微信openId
     */
    public String weChatOpenId;


    private String authToken;

    public String createToken() {
        if (authToken == null) {
            authToken = UUID.randomUUID().toString();
            save();
        }
        return authToken;
    }

    public void deleteAuthToken() {
        authToken = null;
        save();
    }

    public static User findByAuthToken(String authToken) {
        if (authToken == null) {
            return null;
        }
        try {
            return find.where().eq("authToken", authToken).findUnique();
        } catch (Exception e) {
            return null;
        }
    }

    public static User findExpertByAuthToken(String authToken) {
        if (authToken == null) {
            return null;
        }
        try {
            return find.where()
                    .eq("authToken", authToken)
                    .eq("userType", UserType.EXPERT.getName())
                    .findUnique();
        } catch (Exception e) {
            return null;
        }
    }

    public static User findExpertByWeChatOpenId(String openId) {
        if (openId == null) {
            return null;
        }
        try {
            return find.where()
                    .eq("weChatOpenId", openId)
                    .findUnique();
        } catch (Exception e) {
            return null;
        }
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setLastIp(String lastIp) {
        this.lastIp = lastIp;
    }

    public void setPassword(String password) {
        this.shaPassword = getSha512(password);
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    public String getEmail() {
        return email;
    }

    public String getRealName() {
        return realName;
    }

    public String getLastIp() {
        return lastIp;
    }

    public void setFieldSecurity() {
        this.phone = "****";
//        this.realName = "****";
        this.email = "****";
        this.lastIp = "****";
    }

    public static final Finder<Long, User> find = new Finder<Long, User>(
            Long.class, User.class);

    public static User findByEmailAndPassword(String email, String password) {
        return find
                .where()
                .eq("email", email.toLowerCase())
                .eq("shaPassword", getSha512(password))
                .findUnique();
    }

    public static User findByPhoneAndPassword(String phone, String password) {
        return find
                .where()
                .eq("phone", phone.toLowerCase())
                .eq("shaPassword", getSha512(password))
                .findUnique();
    }

    public static User findByNameAndPassword(String name, String password) {
        return find
                .where()
                .eq("name", name.toLowerCase())
                .eq("shaPassword", getSha512(password))
                .findUnique();
    }

    public static User findByEmail(String email) {
        return find
                .where()
                .eq("email", email.toLowerCase())
                .findUnique();
    }

    public static User findByPhone(String phone) {
        return find
                .where()
                .eq("phone", phone.toLowerCase())
                .findUnique();
    }

    public static User findById(Long userId) {
        return find
                .where()
                .eq("id", userId)
                .findUnique();
    }

    public static byte[] getSha512(String value) {
        try {
            return MessageDigest.getInstance("SHA-512").digest(value.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<User> findUsers(int page, int pageSize) {
        return find
                .where()
                .setOrderBy("whenCreated desc")
                .setFirstRow((page - 1) * pageSize)
                .setMaxRows(pageSize)
                .findList();
    }

    public static List<User> findUsersByRealName(String realName,int page, int pageSize) {
        return find
                .where()
                .like("realName","%" + realName + "%")
//                .or(Expr.like("realName", "%" + realName + "%"), Expr.like("realName", "%" + realName + "%"))
                .setOrderBy("whenCreated desc")
                .setFirstRow((page - 1) * pageSize)
                .setMaxRows(pageSize)
                .findList();
    }

    public static List<User> findUsersByUserName(String userName,int page, int pageSize) {
        return find
                .where()
                .like("name","%" + userName + "%")
//                .or(Expr.like("realName", "%" + realName + "%"), Expr.like("realName", "%" + realName + "%"))
                .setOrderBy("whenCreated desc")
                .setFirstRow((page - 1) * pageSize)
                .setMaxRows(pageSize)
                .findList();
    }

    public static List<User> findUsers2(int page, int pageSize) {
        return Ebean.find(User.class)
                .select("phone")
                .where()
                .setOrderBy("whenCreated desc")
                .setFirstRow((page - 1) * pageSize)
                .setMaxRows(pageSize)
                .findList();
    }

    public static List<User> findUsersByType(String userType, int page, int pageSize) {
        return find
                .where()
                .eq("userType", userType)
                .setOrderBy("whenCreated desc")
                .setFirstRow((page - 1) * pageSize)
                .setMaxRows(pageSize)
                .findList();
    }
}
