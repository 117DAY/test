package io.renren.common.utils;


/**
 * gwy的常量类
 * 10开头参数错误
 * 20开头用户错误
 * 30开头课程错误
 *
 * @author qiaoh
 */
public class GwyConstant {
    public enum BizCode {
        /**
         * 数据校验失败
         */
        VALID_EXCEPTION(10001, "参数格式校验失败"),
        /**
         * 系统未知异常
         */
        UNKNOWN_EXCEPTION(10000, "系统未知异常"),

        USER_NOT_EXIST(20000, "用户不存在"),

        USER_ACCOUNT_DISABLE(20001, "用户已注销"),

        LOGIN_EXCEPTION(20002, "登陆失败"),

        PASSWORD_ERROR(20003, "密码错误"),

        ACCESS_DENIED(20004, "无权访问"),
        CODE_EXPIRED(20005, "验证码过期"),
        CODE_ERROR(20006, "验证码错误"),

        USERNAME_EXIST(20007, "用户名已存在"),
        USER_NOT_LOGIN(20008, "用户未登录"),
        LESSON_NO_EXIST(30001, "课程不存在"),
        LESSON_NOT_BUY(30002, "请先购买课程"),
        LESSON_KIND_NOT_EXIST(30003, "没有这种类型的课程"),
        ;


        private final Integer code;
        private final String message;

        public Integer getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        BizCode(Integer code, String message) {
            this.code = code;
            this.message = message;
        }
    }


    /**
     * 试卷类型的枚举
     */
    public enum ExamType {
        /**
         * linetest
         */
        LT(1, "行测"),
        /**
         * argument
         */
        AT(2, "申论"),
        /**
         * textinterview
         */
        TIT(3, "材料面试"),
        /**
         * interview
         */
        IT(4, "问题面试"),

        /**
         * synthesis
         */
        ST(5, "综合"),

        /**
         * writing
         */
        WT(6, "写作"),

        /**
         * 普通试卷
         */
        NORMAL(0, "NORMAL"),

        /**
         * 自定义试卷
         */
        CUSTOM(1, "CUSTOM");


        public Integer getType() {
            return type;
        }

        public String getName() {
            return name;
        }

        private final Integer type;
        private final String name;

        ExamType(Integer type, String name) {
            this.type = type;
            this.name = name;
        }

    }


    public enum RoleConstant {
        /**
         * 老师
         */
        TEACHER(2, "teacher"),
        /**
         * 学生
         */
        STUDENT(3, "student"),
        /**
         * tempteach
         */
        TEMPTEACH(6, "tempTeach"),
        /**
         * 直播
         */
        LIVE(5, "live");

        public Integer id;
        public String roleName;

        RoleConstant(Integer id, String roleName) {
            this.id = id;
            this.roleName = roleName;
        }

        public Integer getId() {
            return id;
        }

        public String getRoleName() {
            return roleName;
        }
    }

    /**
     * 订单状态
     * 订单状态【0新建未支付，1已支付，2申请退款，3已退款】
     *
     * @author qiaoh
     * @date 2021/12/04
     */
    public enum OrderStatus {
        /**
         * 新建未支付
         */
        NEW(0, "新建未支付"),
        /**
         * 已支付
         */
        PAID(1, "已支付"),
        /**
         * 申请退款
         */
        APPLY_REFUND(2, "申请退款"),
        /**
         * 已退款
         */
        REFUNDED(3, "已退款");
        final Integer code;
        final String name;

        OrderStatus(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        public Integer getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }


    /**
     * 课程类型
     * 课程类型【0录播课，1直播课】
     *
     * @author qiaoh
     * @date 2021/12/04
     */
    public enum CourseType {

        /**
         * 录播
         */
        RECORDED(0, "录播"),
        /**
         * 生活
         */
        LIVE(1, "直播");
        final Integer type;
        final String name;

        CourseType(Integer type, String name) {
            this.type = type;
            this.name = name;
        }

        public Integer getType() {
            return type;
        }

        public String getName() {
            return name;
        }
    }

    public enum RefundStatus {
        /**
         * 申请退款
         */
        APPLY_REFUND(0, "申请退款"),
        /**
         * 同意退款
         */
        AGREE_REFUND(1, "同意退款"),
        /**
         * 拒绝退款
         */
        REFUSE_REFUND(2, "拒绝退款");
        final Integer code;
        final String msg;

        RefundStatus(Integer code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public Integer getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }
}
