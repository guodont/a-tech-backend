package models.enums;

/**
 *
 * 文章推送到app状态
 *
 * @author guodont
 */
public enum ArticlePushState implements ViewEnum {

    IS_PUSH("推送"),
    NO_PUSH("不推送");

    private  String value;

    ArticlePushState(String value) {
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
