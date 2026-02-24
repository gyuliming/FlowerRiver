package com.wms.flowerwms.global.paging;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PageResponse<T> {
    private final List<T> items;
    private final int page;       // 페이지는 1부터 시작
    private final int size;
    private final long total;
    private final int totalPages;

    public PageResponse(Page<T> pageObj) {
        this.items = pageObj.getContent();
        this.page = pageObj.getNumber() + 1;
        this.size = pageObj.getSize();
        this.total = pageObj.getTotalElements();
        this.totalPages = pageObj.getTotalPages();
    }
}