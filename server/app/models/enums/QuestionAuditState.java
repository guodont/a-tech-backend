package models.enums;

/**
 * @author guodont
 *
 * 问题状态
 */
public enum QuestionAuditState implements ViewEnum{

    WAIT_AUDITED("待审核"),
    FAILED("未通过"),
    AUDITED("已通过");

    private  String value;

    QuestionAuditState(String value) {
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
