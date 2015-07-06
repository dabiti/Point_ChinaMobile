package com.point.web.util;

/**
 * 分页实体类
 * 
 * @author think
 * 
 */
public class Pager {
	private long rowCount;// 总记录数
	private int rowPerPage;// 每页记录数
	private int currentPage;// 当前页码
	private int pageCount;// 总页数

	public Pager(int currentPage, int rowPerPage) {
		this.currentPage = currentPage;
		this.rowPerPage = rowPerPage;
	}

	public Pager(long rowCount, int currentPage) {
		this(rowCount, 10, currentPage);
	}

	public Pager(long rowCount, int rowPerPage, int currentPage) {
		this.rowCount = rowCount;
		this.rowPerPage = rowPerPage;
		this.currentPage = currentPage;
		this.pageCount = (int) Math.ceil((float) rowCount / rowPerPage);
	}

	public long getRowCount() {
		return rowCount;
	}

	public void setRowCount(long rowCount) {
		this.rowCount = rowCount;
	}

	public int getRowPerPage() {
		return rowPerPage;
	}

	public void setRowPerPage(int rowPerPage) {
		this.rowPerPage = rowPerPage;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageCount() {
		this.pageCount = (int) Math.ceil((float) this.rowCount
				/ this.rowPerPage);
		return this.pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getFirstRow() {
		return (this.currentPage - 1) * this.rowPerPage;
	}

}
