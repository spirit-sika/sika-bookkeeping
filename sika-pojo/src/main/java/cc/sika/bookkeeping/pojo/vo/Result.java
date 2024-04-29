package cc.sika.bookkeeping.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Result<T> implements Serializable {
    private Integer code;
    private String message;
    private T data;

    /* ******************** 响应成功消息 ******************** */
    public static <T> Result<T> success(Integer code, String message, T data) {
        return new Result<T>(code, message, data);
    }

    public static <T> Result<T> success(T data) {
        return success(200, "success", data);
    }

    public static <T> Result<T> success(String message, T data) {
        return success(200, message, data);
    }

    /* ******************** 响应错误消息 ******************** */
    public static <T> Result<T> error(Integer code, String message, T data) {
        return new Result<T>(code, message, data);
    }

    public static <T> Result<T> error(T data) {
        if (data instanceof String) {
            return error(200, (String) data, null);
        }
        return error(200, "error", data);
    }

    public static <T> Result<T> error(Integer code, T data) {
        if (data instanceof String) {
            return error(code, (String) data, null);
        }
        return error(code, "error", data);
    }

}
