package org.levy.filecollector.entity;

public class Result<T> {
    private final int code;
    private final String message;
    private final T data;

    public static final int SUCCESS = 1;
    public static final int FAILURE = 0;

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <R> Result<R> success(R data) {
        return new Result<>(SUCCESS, "success", data);
    }


    public static <R> Result<R> fail(String message) {
        return new Result<>(FAILURE, message, null);
    }


    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public boolean isSuccess() {
        return this.code == SUCCESS;
    }
}
