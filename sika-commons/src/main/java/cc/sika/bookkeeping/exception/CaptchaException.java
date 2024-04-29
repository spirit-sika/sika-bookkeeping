package cc.sika.bookkeeping.exception;

public class CaptchaException extends RuntimeException {
    public CaptchaException(String message) {
        super(message);
    }
}
