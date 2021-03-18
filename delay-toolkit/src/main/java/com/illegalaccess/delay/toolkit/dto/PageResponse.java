package com.illegalaccess.delay.toolkit.dto;

import java.io.Serializable;

public class PageResponse implements Serializable {

    private static final long serialVersionUID = -1L;
    /**
     * 总页数
     */
    private int totalPages;

    /**
     * 元素总数
     */
    private int totalElement;

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalElement() {
        return totalElement;
    }

    public void setTotalElement(int totalElement) {
        this.totalElement = totalElement;
    }
}
