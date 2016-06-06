package models;

import models.enums.MessageType;
import play.data.validation.Constraints;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import java.util.List;

/**
 * Created by guodont on 16/6/4.
 * <p>
 * 通知消息
 */
public class Message extends BaseModel {

    /**
     * 关联数据id
     */
    private Long relationId;

    /**
     * 消息类型
     */
    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    /**
     * 所属用户
     */
    @ManyToOne
    private User user;

    /**
     * 消息标题
     */
    @Column(length = 255, nullable = false)
    @Constraints.MaxLength(255)
    @Constraints.Required
    private String title;

    /**
     * 消息备注
     */
    @Constraints.MaxLength(255)
    private String remark;

    /**
     * 是否已读
     */
    private boolean isMarkRead;


    public Long getRelationId() {
        return relationId;
    }

    public void setRelationId(Long relationId) {
        this.relationId = relationId;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isMarkRead() {
        return isMarkRead;
    }

    public void setMarkRead(boolean markRead) {
        isMarkRead = markRead;
    }

    public static final Finder<Long, Message> find = new Finder<Long, Message>(
            Long.class, Message.class);

    /**
     * 获取用户未读消息数
     *
     * @param user
     * @return
     */
    public static int unReadMessageCount(final User user) {
        return find
                .where()
                .eq("user", user)
                .eq("isMarkRead", 0)
                .findRowCount();
    }

    public static Message findById(final Long id) {
        return find
                .where()
                .eq("id", id)
                .findUnique();
    }

    /**
     * 获取用户未读消息
     *
     * @param user
     * @return
     */
    public static List<Message> getUnReadMessages(final User user, int page, int pageSize) {
        return find
                .where()
                .eq("user", user)
                .eq("isMarkRead", 0)
                .setOrderBy("whenCreated desc")
                .setFirstRow((page - 1) * pageSize)
                .setMaxRows(pageSize)
                .findList();
    }

    /**
     * 获取用户所有消息
     *
     * @param user
     * @return
     */
    public static List<Message> getAllMessages(final User user, int page, int pageSize) {
        return find
                .where()
                .eq("user", user)
                .setOrderBy("whenCreated desc")
                .setFirstRow((page - 1) * pageSize)
                .setMaxRows(pageSize)
                .findList();
    }

    public static Message findByIdAndUser(long id, final User user) {
        return find
                .where()
                .eq("id", id)
                .eq("user", user)
                .findUnique();
    }
}
