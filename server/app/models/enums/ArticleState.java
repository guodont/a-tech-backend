package models.enums;

/**
 * Created by lzadmin on 2016/4/12 0012.
 * 文章提交状态类
 */
public enum  ArticleState implements ViewEnum {


    /*提交状态NOT_AUDITED：未审核WAIT_AUDITED：待
            审
    核AUDITED：审核成功FAILED_AUDITED：未通过*/
    NOT_AUDITED("未审核"),
    WAIT_AUDITED("待审核"),
    AUDITED("已审核"),
    FAILED_AUDITED("未通过");
    private  String value;

    ArticleState(String value) {
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
