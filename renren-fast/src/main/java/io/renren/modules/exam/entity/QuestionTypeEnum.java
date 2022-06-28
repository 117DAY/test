package io.renren.modules.exam.entity;

/**
 * @author jljy
 */

@SuppressWarnings("AlibabaEnumConstantsMustHaveComment")
public enum QuestionTypeEnum {
    TOPIC(1, "【题文】"),
    DATA(2, "【资料】"),
    CHOICE(3, "【选项】"),
    ANSWER(4, "【答案】"),
    ANALYSIS(5, "【解析】"),
    FINISH(6, "【结束】"),
    TEXT(7, "【材料】"),
    QUESTION(8, "【问题】"),
    //标识符正则
    IDENTIFIER_REGEX(9, "【题文】|【选项】|【答案】|【解析】|【结束】"),
    //大题正则,如一、常识判断(每题0.8分)
    QUESTION_TYPE_REGEX(10, "([一二三四五六七八九十]{1,3})([、.]{1})(常识判断|言语理解与表达|数量关系|判断推理|资料分析)([（(])(每题\\d+(\\.\\d+)?分)([)）])"),
    //分数正则,如0.8
    SCORE_REGEX(11, "\\d+(\\.\\d+)?"),
    //题目类型正则
    TYPE_REGEX(12, "常识判断|言语理解与表达|数量关系|判断推理|资料分析");


    private Integer code;
    private String type;

    QuestionTypeEnum(Integer code, String type) {
        this.code = code;
        this.type = type;
    }

    public Integer getCode() {
        return code;
    }

    public String getType() {
        return type;
    }
}
