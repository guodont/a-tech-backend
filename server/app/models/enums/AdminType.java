package models.enums;

/**
 * 管理员类型
 */
public enum AdminType implements ViewEnum {

    MASTER("超级管理员"),
    COMMON("普通管理员");

    public String value;

    AdminType(String value) {
        this.value = value;
    }

    @Override
    public String getName() {
        return toString();
    }

    @Override
    public String getValue() {
        return value;
    }
}
