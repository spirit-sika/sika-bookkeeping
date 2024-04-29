package cc.sika.bookkeeping.constant;

/**
 * 定义登录设备类型常量
 */
public enum DeviceType {
    /**
     *  PC端登录类型, 为 C/S 架构登录设备类型保留
     */
    PC(1, "PC"),
    /**
     *  APP设备登录类型
     */
    APP(2, "APP"),
    /**
     * 小程序端登录类型
     */
    MINI_PROGRAM(3, "MINI_PROGRAM"),
    /**
     * WEB端登录类型, 包括H5
     */
    WEB(4, "WEB");

    private final Integer value;
    private final String type;

    DeviceType(Integer value, String type) {
        this.value = value;
        this.type = type;
    }

    public Integer getValue() {
        return this.value;
    }
    public String getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return this.type;
    }
}
