package com.lhs.dto;

public class PagingDto {
    
    private Integer pageSize = 10; // 페이지 사이즈
    private Integer currentPage; // 현재 페이지

    public PagingDto() {} // 기본 생성자

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

    @Override
    public String toString() {
        return "PagingDto [pageSize=" + pageSize + ", currentPage=" + currentPage + "]";
    }

    // startPage 계산 메서드
    public int calculateStartPage() {
        return ((currentPage - 1) / pageSize) * pageSize + 1;
    }

    // endPage 계산 메서드
    public int calculateEndPage(int maxPage) {
        return Math.min(calculateStartPage() + pageSize - 1, maxPage);
    }
}
