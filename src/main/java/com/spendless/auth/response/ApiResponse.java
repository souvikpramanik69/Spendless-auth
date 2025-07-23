package com.spendless.auth.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T, E> {

    private int code;
    private E message;
    private T data;
    private Status status;
    private Pagination metaData;



    public enum Status {
        SUCCESS,
        ERROR
    }

    public ApiResponse(int code, E message, Status status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    public ApiResponse(int code, E message, Status status, T data) {
        this.code = code;
        this.message = message;
        this.status = status;
        this.data = data;
    }

    public ApiResponse(int code, E message, Status status, T data, Pagination metaData) {
        this.code = code;
        this.message = message;
        this.status = status;
        this.data = data;
        this.metaData = metaData;
    }
}
