package com.chat.util.map;

import java.util.List;

public class Page {
	/**
	 * page 总长度
	 */
	private Integer tPageLength;
	private Integer tPageNo;
	private Integer tPageSize;
	private Integer tPageStart;
	private List<?> tList;
	public Integer gettPageLength() {
		return tPageLength;
	}
	public void settPageLength(Integer tPageLength) {
		this.tPageLength = tPageLength;
	}
	public Integer gettPageNo() {
		return tPageNo;
	}
	public void settPageNo(Integer tPageNo) {
		this.tPageNo = tPageNo;
	}
	public Integer gettPageSize() {
		return tPageSize;
	}
	public void settPageSize(Integer tPageSize) {
		this.tPageSize = tPageSize;
	}
	public List<?> gettList() {
		return tList;
	}
	public void settList(List<?> tList) {
		this.tList = tList;
	}
	public Integer gettPageStart() {
		return tPageStart;
	}
	public void settPageStart(Integer tPageStart) {
		this.tPageStart = tPageStart;
	}
}
