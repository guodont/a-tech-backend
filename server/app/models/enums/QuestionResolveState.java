package models.enums;

/**
 * @author guodont
 *
 * 问题状态
 */
public enum QuestionResolveState implements ViewEnum{


    RESOLVED("已解决"),
    WAIT_RESOLVE("待解决");

    private  String value;

    QuestionResolveState(String value) {
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
