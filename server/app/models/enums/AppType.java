package models.enums;

/**
 * app类型
 */
public enum AppType implements ViewEnum{

    ANDROID("安卓版"),
    IOS("苹果版");
    private  String value;

    AppType(String value) {
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
