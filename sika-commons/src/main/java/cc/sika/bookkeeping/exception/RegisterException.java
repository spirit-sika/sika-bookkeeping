package cc.sika.bookkeeping.exception;

public class RegisterException extends RuntimeException{
    public RegisterException(String message) {
        super(message);
    }

    public RegisterException() {
        super("注册账户失败, 请检查信息是否有误");
    }
}
