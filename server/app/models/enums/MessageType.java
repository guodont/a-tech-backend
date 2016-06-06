package models.enums;

/**
 * 消息类型
 */
public enum MessageType implements ViewEnum{

    QUESTION("问题"),
    RELATION("关系"),
    SYSTEM("系统"),
    TRADE("交易");
    private  String value;

    MessageType(String value) {
        this.value = value;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getValue() {
        return value;
    }
}
