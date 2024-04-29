package cc.sika.bookkeeping.exception;

public class LoginException extends RuntimeException{
    public LoginException() {
        super("登录异常, 请联系管理人员");
    }

    public LoginException(String message) {
        super(message);
    }
}
