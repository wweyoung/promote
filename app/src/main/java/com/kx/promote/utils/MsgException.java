package com.kx.promote.utils;

public class MsgException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8963656997377544349L;
	
	
	private String msg;
	private Integer code;
	public MsgException() {
		this.code=1;
		this.msg="操作失败！";
	}
	public MsgException(String msg) {
		this.code=1;
		this.msg = msg;
	}
	public MsgException(int code,String msg) {
		this.code = code;
		this.msg = msg;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public Msg get() {
		return new Msg(this.code,this.msg);
	}
	public Msg assign(Msg msg) {
		return msg.set(code, this.msg);
	}
}
