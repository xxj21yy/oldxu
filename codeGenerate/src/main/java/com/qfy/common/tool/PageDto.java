package com.qfy.common.tool;

import com.qfy.base.dto.BaseDto;

/**
 * 
 * @author     : 肖九龙(NineDragon)
 * @createDate ：2016年8月25日 上午10:09:51
 * @description: 分页
 */
@SuppressWarnings("serial")
public class PageDto extends BaseDto {

	private static final int DEFAULT_PAGE = 1;
	
	private static final int DEFAULT_PAGE_SIZE = 8;
	
	private long total = -1;//总条数

	private int page = DEFAULT_PAGE;//当前页
	
	private int pageSize = DEFAULT_PAGE_SIZE;//每页数
	
	private long totalPage;//总页数

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		if(total<0){
			this.total=0;
		}else{
		this.total = total;}
		// 通过总条数和每页大小得到总页数
		if (total <= 0) { // 如果总数为负数, 表未设置
			this.totalPage = 0;
		} else { // 计算总页数
			this.totalPage =  ((total / pageSize) + (total % pageSize == 0 ? 0 : 1));
		}
	}

	public int getPage() {
		if (page == 0) {
			return DEFAULT_PAGE;
		}
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		if (pageSize == 0) {
			return DEFAULT_PAGE_SIZE;

		}
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public long getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(long totalPage) {
		this.totalPage = totalPage;
	}
}
