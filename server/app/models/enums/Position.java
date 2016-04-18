package models.enums;

/**
 * Created by llz on 2016/4/13.
 * 广告位置详细描述
 */
public enum Position implements ViewEnum {
    FLOAT("幻灯片"),
    AMONG("顶部"),
    TOP("模块中间");

    public String value;

    Position(String value) {
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
