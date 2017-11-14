package com.qfy.common.tool;

import com.qfy.base.dto.BaseDto;

public class Base extends BaseDto {

	private static final long serialVersionUID = 3929359826362059606L;
	
	public static final int DEFAULT_PAGE = 1;
	
	public static final int DEFAULT_PAGE_SIZE = 8;
	
	private int page = DEFAULT_PAGE;
	
	private int pageSize = DEFAULT_PAGE_SIZE;
	
	private int start = 0;

	public int getStart() {
		this.start = (this.getPage() - 1) * this.getPageSize();
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

}
