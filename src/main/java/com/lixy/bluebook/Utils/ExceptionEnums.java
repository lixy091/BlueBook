package com.lixy.bluebook.Utils;

public enum ExceptionEnums {
    IDT_CORE_300("idt-core-300", "您的访问来源不安全！"),
    IDT_CORE_400("idt-core-400", "请求失败"),
    IDT_CORE_401("idt-core-401", "类型不匹配异常"),
    IDT_CORE_402("idt-core-402", "缺少参数错误"),
    IDT_CORE_404("idt-core-404", "请求接口没有被找到"),
    IDT_CORE_405("idt-core-405", "请求类型不允许"),
    IDT_CORE_406("idt-core-406", "无法使用请求的内容特性响应请求的网页"),
    IDT_CORE_415("idt-core-415", "不支持的媒体类型"),
    IDT_CORE_500("idt-core-500", "内部服务错误"),
    IDT_CORE_501("idt-core-501", "空指针异常"),
    IDT_CORE_502("idt-core-502", "类型转换异常"),
    IDT_CORE_503("idt-core-503", "输入输出异常"),
    IDT_CORE_504("idt-core-504", "没有找到指定方法错误"),
    IDT_CORE_506("idt-core-506", "SQL异常"),
    IDT_CORE_507("idt-core-507", "Sql语法错误"),
    IDT_CORE_508("idt-core-508", "结果集访问异常"),
    IDT_CORE_509("idt-core-509", "主键重复"),
    IDT_CORE_510("idt-core-510", "数据完整性冲突异常"),
    IDT_CORE_511("idt-core-511", "数据访问权限被拒绝"),
    IDT_CORE_512("idt-core-512", "数据访问资源失败"),
    IDT_CORE_513("idt-core-513", "临时数据访问资源异常"),
    IDT_CORE_514("idt-core-514", "无法获取锁异常"),
    IDT_CORE_515("idt-core-515", "死锁"),
    IDT_CORE_516("idt-core-516", "无法序列化事务异常"),
    IDT_CORE_517("idt-core-517", "输入输出流异常"),
    IDT_CORE_518("idt-core-518", "数组下标越界异常"),
    IDT_CORE_519("idt-core-519", "上传文件大小超出限制"),
    IDT_CORE_520("idt-core-520", "您没有权限访问"),
    IDT_CORE_600("idt-core-600", "授权验证失败！"),
    IDT_CORE_601("idt-core-601", "参数包含不安全字符"),
    IDT_CORE_602("idt-core-602", "参数包含不安全表达式"),
    IDT_CORE_603("idt-core-603", "参数包含不安全脚本标签"),
    IDT_CORE_604("idt-core-604", "参数包含不安全标签事件"),
    IDT_CORE_605("idt-core-605", "参数包含不安全的外域引用地址"),
    SUCCESSFUL("idt-core-200", "操作成功"),
    FAILURE("idt-core-505", "操作失败"),
    TOKEN_FAILURE("idt-jwt-500", "token验证失败"),
    LICENCE_FAILURE("idt-lic-500", "授权失败");

    private final String code;
    private final String msg;

    private ExceptionEnums(String code, String message) {
        this.code = code;
        this.msg = message;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.msg;
    }

    @Override
    public String toString() {
        return "[" + this.code + "]" + this.msg;
    }

}
