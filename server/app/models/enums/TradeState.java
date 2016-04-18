package models.enums;

/**
 * Created by lzadmin on 2016/4/12 0012.
 * trade表中state属性的枚举类
 */
public enum  TradeState implements ViewEnum{

    WAIT_AUDITED("待审核"),
    FAILED("未通过"),
    AUDITED("已通过");

    private  String value;

    TradeState(String value) {
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
