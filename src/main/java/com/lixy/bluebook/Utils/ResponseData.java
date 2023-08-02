package com.lixy.bluebook.Utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiModelProperty;

import java.io.IOException;
import java.io.Serializable;

public class ResponseData{
    @ApiModelProperty(
            value = "具体状态码",
            example = "xbb-core-200"
    )
    private String code = "";
    @ApiModelProperty("提示信息")
    private String message = "";
    private Object data = null;
    @ApiModelProperty(
            value = "操作状态",
            example = "success|failure"
    )
    private String status = "";

    private ResponseData() {
    }

    private ResponseData(String code, String message) {
        this.code = code;
        this.message = message;
        this.status = "success";
    }

    private ResponseData(String code, String message, String status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return this.message;
    }

    public String getCode() {
        return this.code;
    }

    public String getStatus() {
        return this.status;
    }

    public Object getData() {
        return this.data;
    }

    public ResponseData setData(Object value) {
        this.data = value;
        return this;
    }

    public static  ResponseData getInstance(String code, String msg) {
        return new ResponseData(code, msg);
    }

    public static  ResponseData getInstance(String code, String msg, String status) {
        return new ResponseData(code, msg, status);
    }

    @Override
    public String toString() {
        try {
            return (new ObjectMapper()).writeValueAsString(this);
        } catch (IOException var2) {
            return super.toString();
        }
    }
}
