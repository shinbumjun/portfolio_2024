package com.lhs.dto;

import org.springframework.web.util.UriComponentsBuilder;

/*
	검색 조건 : 제공해야 하는 값이 
	keyword, offset, pageSize

 */
public class SearchCondition {
	
	// 여기 변수들을 컨트롤러가 매개변수로 받는다 
	private Integer page = 1; // *현재 페이지(처음은 기본값으로 줌)
	private Integer pageSize = 10; // *한 페이지의 크기(처음은 기본값으로 줌)
	// private Integer offset = 0; // 맨 처음부터 얼만큼 떨어져 있느냐
	private String keyword = ""; // 키워드
	private String option = ""; // 제목+내용, 제목, 내용 검색
	
	
	public SearchCondition() {} // 기본 생성자

	// offset 제외
	public SearchCondition(Integer page, Integer pageSize, String keyword, String option) {
		this.page = page;
		this.pageSize = pageSize;
		this.keyword = keyword;
		this.option = option;
	}

    // 이 메서드가 하는 일은 쿼리 문자열을 만들어준다 (페이지를 지정해주면 그 페이지를 사용하고)
    public String getQueryString(Integer page) {
    	// 검색을 한 페이지에 들어가고 목록을 누르면 전애 있던 /board/list로 이동하기 위해서 만듬
    	// ?page=1&pageSize=10&option=T&keyword="title"
    	return UriComponentsBuilder.newInstance()
    			.queryParam("page", page) // 현재 페이지로 세팅
    			.queryParam("pageSize", pageSize)
    			.queryParam("option", option)
    			.queryParam("keyword",keyword)
    			.build().toString();
    }
       
    public String getQueryString() { // 페이지를 지정해주지 않으면 sc.getPage() 사용
    	return getQueryString(page);
    }
    
	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getOffset() {
		return (page-1) * pageSize;
	}


	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	@Override
	public String toString() {
		return "SearchCondition [page=" + page + ", pageSize=" + pageSize + ", offset=" + getOffset() + ", keyword="
				+ keyword + ", option=" + option + "]";
	}

}
