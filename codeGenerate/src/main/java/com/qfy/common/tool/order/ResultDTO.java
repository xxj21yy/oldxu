package com.qfy.common.tool.order;

import com.qfy.common.tool.PageDto;

import java.util.ArrayList;
import java.util.List;

public class ResultDTO<T> extends PageDto{

	private static final long serialVersionUID = -4978902632950689204L;

	private boolean result; // 操作结果

	/** message（消息） */
	private String message; // 操作结果信息说明

	/** 状态码 */
	private int code; // 代码


	/** ListData（数据集合） */
	private List<T> ListData;

	/** string（html代码段） */
	private String html;
	
	public List<T> getListData() {
		return ListData;
	}

	public void setListData(List<T> listData) {
		ListData = listData;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public static <S> ResultDTO<S> getInstance() {
		return new ResultDTO<S>();
	}

	public void add(T t) {
		if (null == ListData) {
			ListData = new ArrayList<T>(0);
		}
		ListData.add(t);
	}

	/**
	 * 获取失败结果集
	 * 
	 * @param msg
	 * @param code
	 * @return
	 */
	public static <T> ResultDTO<T> getFaildInstance(String msg, int code) {
		ResultDTO<T> r = new ResultDTO<T>();
		r.setResult(false);
		r.setMessage(msg);
		r.setCode(code);
		return r;
	}

	/**
	 * 获取成功结果集
	 * 
	 * @param list
	 * @param msg
	 * @param code
	 * @return
	 */
	public static <T> ResultDTO<T> getSuccessInstance(List<T> list, String msg, int code) {
		ResultDTO<T> r = new ResultDTO<T>();
		r.setResult(true);
		r.setMessage(msg);
		r.setCode(code);
		r.setListData(list);
		return r;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}
}
