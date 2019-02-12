package com.zcx.zhihuCrawler.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 处理返回给页面的json结果.
 *
 * <pre>
 * 	处理返回给页面的json结果，封装好默认的成功的状态
 * </pre>
 *
 * @author 杨雷
 * @since 1.0
 */
public class JsonResult {

    private int status;
    private String msg;
    private Object data;

    public JsonResult() {

    }

    public JsonResult(Object data) {
        this.data = data;
    }

    public JsonResult(Object data, String msg) {

//		if(data == null) {
//			this.data = null;
//		} else {
        this.data = data;
//		}

        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String toString() {
        Gson gson = new Gson();

        return gson.toJson(this);
    }

    public String toString(String format) {
        Gson gson = new GsonBuilder().setDateFormat(format).create();

        return gson.toJson(this);
    }

    public JsonResult success() {

        this.status = 0;

        if (this.msg == null || "".equals(this.msg)) {
            this.msg = "success";
        }

        return this;
    }

    public JsonResult failure() {

        this.status = -1;

        if (this.msg == null || "".equals(this.msg)) {
            this.msg = "failure";
        }

        return this;
    }
}
