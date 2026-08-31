package com.cws.shop.dto.response;

import java.util.List;


public class PagedResponse<T> {

    // the actual items for this page
    private List<T> content;

    // current page number (0-based)
    private int page;

    // items per page
    private int size;

    // total records in DB matching the query
    private long totalElements;

    // total number of pages
    private int totalPages;

    // true if this is the last page
    private boolean last;


    public PagedResponse() {
    }

    public PagedResponse(List<T> content, int page, int size,
                         long totalElements, int totalPages, boolean last) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.last = last;
    }


    // Getters and Setters

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

}