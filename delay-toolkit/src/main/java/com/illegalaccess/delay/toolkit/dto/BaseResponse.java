package com.illegalaccess.delay.toolkit.dto;

import com.illegalaccess.delay.toolkit.enums.BaseStatusEnum;
import com.illegalaccess.delay.toolkit.enums.DelayStatusEnums;
import com.illegalaccess.delay.toolkit.json.JsonTool;

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

    public boolean OK() {
        return DelayStatusEnums.SUCCESS.getStatus().equals(this.errorCode);
    }


    public BaseResponse() {
    }

    public BaseResponse(String errorCode, String errorMsg, T content) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.content = content;
    }

    public static final <T> BaseResponse<T> build(String errorCode, String errorMsg, T content) {
        BaseResponse<T> response = new BaseResponse();
        response.errorCode = errorCode;
        response.errorMsg = errorMsg;
        response.content = content;
        return response;
    }

    public static final <T> BaseResponse<T> build(BaseStatusEnum status, T content) {
        BaseResponse<T> response = new BaseResponse();
        response.errorCode = status.getStatus();
        response.errorMsg = status.getMessage();
        response.content = content;
        return response;
    }

    public static final <T> BaseResponse<T> fail(String errorCode, String errorMsg) {
        return (BaseResponse<T>) build(errorCode, errorMsg, (Object)null);
    }

    public static final <T> BaseResponse<T> fail(String errorCode, String errorMsg, T content) {
        return build(errorCode, errorMsg, content);
    }

    public static final <T> BaseResponse<T> fail(BaseStatusEnum status) {
        return (BaseResponse<T>) build(status, (Object)null);
    }

    public static final <T> BaseResponse<T> success(T content) {
        return build(DelayStatusEnums.SUCCESS.getStatus(), DelayStatusEnums.SUCCESS.getMessage(), content);
    }

    public static final <T> BaseResponse<T> fail(BaseStatusEnum status, T content) {
        return build(status, content);
    }

//    public static final <E> BaseResponse<E> success(E content) {
//        return build(CommonStatusEnum.SUCCESS, content);
//    }
//
//    public static final <E> BaseResponse<E> success() {
//        return build(CommonStatusEnum.SUCCESS, (Object)null);
//    }

    public String toJsonString() {
        return JsonTool.toJsonString(this);
    }
}
