package cc.sika.bookkeeping.exception;

public class RegisterException extends RuntimeException{

    private static final String FAIL_BIND_ROLE = "绑定用户角色失败, 请联系管理人员!";
    private static final String PHONE_EXITS = "手机号码已被注册";
    private static final String ERROR_MESSAGE = "注册账户失败, 请检查信息是否有误";


    public RegisterException(String message) {
        super(message);
    }

    public RegisterException() {
        super(ERROR_MESSAGE);
    }

    public static RegisterException PHONE_EXITS() {
        return new RegisterException(PHONE_EXITS);
    }

    public static RegisterException FAIL_BIND_ROLE() {
        return new RegisterException(FAIL_BIND_ROLE);
    }

    /**
     * 插入用户信息失败
     * @return RegisterException
     */
    public static RegisterException CREATE_ERROR() {
        return new RegisterException();
    }
}
