package models.enums;

/**
 * Created by lzadmin on 2016/4/12 0012.
 * trade表中type属性的枚举类
 */
public enum  TradeType implements ViewEnum{

    SUPPLY("供应"),
    DEMAND("需求");
    private String value;

    TradeType(String value) {
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
