package com.kx.promote.bean;

import java.util.HashMap;
import java.util.Map;

public class Msg {

    //状态码  100-成功  200-失败
    private int code;
    //提示信息
    private String msg;
    //用户返回给浏览器的数据
    private Map<String,Object> data = new HashMap<String, Object>();

    public Msg() {
    	this.set(1, "操作失败！");
    }
    public Msg(int code,String message) {
    	this.set(code, message);
    }
    public Msg(int code,String[] messages) {
    	this.set(code, messages[code]);
    }
    public static Msg result(int code,String message){
        Msg result = new Msg();
        result.setCode(code);
        result.setMsg(message);
        return result;
    }
    public static Msg result(boolean success,String message){
        Msg result = new Msg();
        result.setCode(success?1:0);
        result.setMsg(message);
        return result;
    }
    public static Msg result(boolean success){
        return success?Msg.success():Msg.fail();
    }
    public static Msg success(String message){
        return Msg.result(0,message);
    }
    public static Msg success(){
        return Msg.success("操作成功！");
    }
    public static Msg fail(int code,String message){
        return Msg.result(code, message);
    }
    public static Msg fail(String message){
        return Msg.fail(1, message);
    }
    public static Msg fail(int code){
        return Msg.fail(code, "操作失败！");
    }
    public static Msg fail() {
        return Msg.fail(1);
    }
    public Msg add(String key,Object value){
        this.getData().put(key,value);
        return this;
    }


    public int getCode() {
        return code;
    }
    public Msg set(int code,String msg) {
        this.code = code;
        this.msg = msg;
        return this;
    }
    public Msg set(int code,String[] msgs) {
        this.code = code;
        this.msg = msgs[code];
        return this;
    }
    public Msg set(boolean success,String[] msgs) {
        return this.set(success?0:1,msgs);
    }

    public Msg setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public Msg setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Msg pushMsg(String msg) {
    	if(this.msg==null || this.msg.isEmpty()) {
    		this.msg = msg;
    	}
    	else {
        	this.msg += msg+"\n";    		
    	}
    	return this;
    }
    
    public Map<String, Object> getData() {
        return data;
    }
    public Object get(Object key) {
    	return data.get(key);
    }
    public Msg setData(Map<String, Object> data) {
        this.data = data;
        return this;
    }
}
