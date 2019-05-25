package org.air.bigearth.apps.base;

/**
 * 分页对象
 * 
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-25
 */
public class Page {
    /**
     * 偏移量
     */
    private int offset;
    /**
     * 每页显示的条数
     */
    private int pageSize;
    /**
     * 当前页
     */
    private int currentPage;
    /**
     * 总条数
     */
    private int total;
    /**
     * 总页数
     */
    private int totalNum;

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }
}
