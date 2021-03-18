package com.illegalaccess.delay.client.dto;

import java.io.Serializable;

/**
 *
 * @author Jimmy Li
 * @date 2021-03-10 21:16
 */
public class BaseResponse<T> implements Serializable {

    private static final long serialVersionUID = -1L;

    private String errorCode;

    private String errorMsg;

    private T content;

    public BaseResponse() {
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public T getContent() {
        return this.content;
    }

    public void setContent(T content) {
        this.content = content;
    }

//    public boolean OK() {
//        return CommonStatusEnum.SUCCESS.getStatus().equals(this.errorCode);
//    }
//
//    public static final <E> BaseResponse<E> build(String errorCode, String errorMsg, E content) {
//        BaseResponse<E> response = new BaseResponse();
//        response.errorCode = errorCode;
//        response.errorMsg = errorMsg;
//        response.content = content;
//        return response;
//    }
//
//    public static final <E> BaseResponse<E> build(BaseStatusEnum status, E content) {
//        BaseResponse<E> response = new BaseResponse();
//        response.errorCode = status.getStatus();
//        response.errorMsg = status.getMessage();
//        response.content = content;
//        return response;
//    }
//
//    public static final <E> BaseResponse<E> fail(String errorCode, String errorMsg) {
//        return build(errorCode, errorMsg, (Object)null);
//    }
//
//    public static final <E> BaseResponse<E> fail(String errorCode, String errorMsg, E content) {
//        return build(errorCode, errorMsg, content);
//    }
//
//    public static final <E> BaseResponse<E> fail(BaseStatusEnum status) {
//        return build(status, (Object)null);
//    }
//
//    public static final <E> BaseResponse<E> fail(BaseStatusEnum status, E content) {
//        return build(status, content);
//    }
//
//    public static final <E> BaseResponse<E> success(E content) {
//        return build(CommonStatusEnum.SUCCESS, content);
//    }
//
//    public static final <E> BaseResponse<E> success() {
//        return build(CommonStatusEnum.SUCCESS, (Object)null);
//    }

    public String toJsonString() {
        return JsonTool
    }
}
