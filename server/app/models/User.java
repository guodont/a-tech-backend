////////
// This sample is published as part of the blog article at www.toptal.com/blog
// Visit www.toptal.com/blog and subscribe to our newsletter to read great posts
////////

package models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.persistence.*;

import models.enums.UserType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import play.data.validation.Constraints;

/**
 * Model representing a Blog user
 */
@Entity
public class User extends BaseModel implements UserDetails {

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
    @Column(length = 45)
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
    @Column(length = 45,nullable = false)
    @Constraints.MaxLength(45)
    private String lastIp;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    public List<BlogPost> posts;

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public static User findByUserNameAndPassword(String userName, String password) {
        return find
                .where()
                .eq("name", userName.toLowerCase())
                .eq("shaPassword", getSha512(password))
                .findUnique();
    }

    public static User findByUserName(String userName) {
        return find
                .where()
                .eq("name", userName.toLowerCase())
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

    public static byte[] getSha512(String value) {
        try {
            return MessageDigest.getInstance("SHA-512").digest(value.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return String.valueOf(shaPassword);
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
