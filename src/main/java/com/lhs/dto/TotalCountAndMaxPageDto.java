package com.lhs.dto;

public class TotalCountAndMaxPageDto {

	private int totalCount; // 총 게시글 수
    private int maxPage; // 총 페이지 수
    
    public TotalCountAndMaxPageDto() {} // 기본 생성자 

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getMaxPage() {
		return maxPage;
	}

	public void setMaxPage(int maxPage) {
		this.maxPage = maxPage;
	}

	@Override
	public String toString() {
		return "TotalCountAndMaxPageDto [totalCount=" + totalCount + ", maxPage=" + maxPage + "]";
	}
}
