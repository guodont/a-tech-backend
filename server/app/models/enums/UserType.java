package models.enums;

/**
 * 用户类型明细
 * Created by llz on 2016/4/12.
 */
public enum UserType implements ViewEnum {

    PUBLIC("普通用户"),
    EXPERT("专家用户");

    public String value;

    UserType(String value) {
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
