package models;

import org.joda.time.DateTime;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by llz on 2016/4/12.
 */
@Entity
public class Admin extends BaseModel {

    /**
     * 管理员姓名
     */
    public String name;
    /**
     * 管理员邮箱
     */
    @Column(length = 255, unique = true, nullable = false)
    @Constraints.MaxLength(255)
    @Constraints.Required
    @Constraints.Email
    public String email;
    /**
     * 管理员密码
     */
    @Column(length = 64, nullable = false)
    public byte[] shaPassword;
    /**
     * 管理员电话
     */
    @Column(length = 11, unique = true, nullable = false)
    @Constraints.MaxLength(11)
    @Constraints.Required
    public String phone;

    /**
     * 最后登录IP
     */
    @Column(length = 45, unique = true, nullable = false)
    @Constraints.MaxLength(45)
    public String lastIp;


    /**
     * Finder 数据库查询操作
     */
    public static final Finder<Long, Admin> find = new Finder<Long, Admin>(
            Long.class, Admin.class);

    public void setPassword(String password) {
        this.shaPassword = getSha512(password);
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    /**
     * 通过邮箱密码查找管理员
     * @param email
     * @param password
     * @return
     */
    public static Admin findByEmailAndPassword(String email, String password) {
        return find
                .where()
                .eq("email", email.toLowerCase())
                .eq("shaPassword", getSha512(password))
                .findUnique();
    }

    /**
     * 通过邮箱查找管理员
     * @param email
     * @return
     */
    public static Admin findByEmail(String email) {
        return find
                .where()
                .eq("email", email.toLowerCase())
                .findUnique();
    }

    /**
     * 加密算法
     * @param value
     * @return
     */
    public static byte[] getSha512(String value) {
        try {
            return MessageDigest.getInstance("SHA-512").digest(value.getBytes("UTF-8"));
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

}
