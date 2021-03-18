package com.illegalaccess.delay.toolkit.dto;


import java.io.Serializable;

public class PageRequest implements Serializable {

    private static final long serialVersionUID = -1L;

    private Integer offset = 0;

    private Integer limit = 10;

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
