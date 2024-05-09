package cc.sika.bookkeeping.exception;

public class LedgerException extends RuntimeException{

    private static final String DEFAULT_MESSAGE = "创建账本失败, 请联系管理人员!";
    private static final String FAIL_BIND_USER = "账本与用户绑定失败, 请联系管理人员!";

    public LedgerException() {
        super(DEFAULT_MESSAGE);
    }

    public LedgerException(String message) {
        super(message);
    }

    /**
     * 创建账本失败
     * @return LedgerException
     */
    public static LedgerException CREATE_ERROR() {
        return new LedgerException();
    }

    /**
     * 账本与用户绑定失败
     * @return LedgerException
     */
    public static LedgerException FAIL_BIND_USER() {
        return new LedgerException(FAIL_BIND_USER);
    }
}
