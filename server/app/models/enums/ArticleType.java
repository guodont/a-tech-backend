package models.enums;

/**
 * Created by lzadmin on 2016/4/12 0012.
 * 文章类型枚举类
 */
public enum  ArticleType implements ViewEnum {

    WEB("网站"),
    ARTICLE("专家文章"),
    ACCOMPLISH("成果");

    public  String value;

    ArticleType(String value) {
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
