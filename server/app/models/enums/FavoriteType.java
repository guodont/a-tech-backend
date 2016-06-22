package models.enums;

/**
 * Created by lzadmin on 2016/4/12 0012.
 * favorite表type类型枚举类
 */
public enum  FavoriteType implements ViewEnum{
//    收藏类型（QUESTION:问题、TRADE:交

    QUESTION("问题"),
    TRADE("交易");
    private  String value;

    FavoriteType(String value) {
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
