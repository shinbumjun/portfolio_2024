package com.lhs.dto;

public class PageHandlerDto {
	
	/*
  		게시물 목록 페이징 이론
		
		pageSize;         한 페이지의 크기
		navisize = 10;    네비 사이즈
		page              현재페이지
		beginPage 예시 11   첫 페이지
		endPage   예시 20   마지막 페이지
		
		
		1. 게시글 제목에 이런 링크를 달아야 한다 그래야 읽을 수 있다
		/board/list.do?board_seq=10000&page=16&pageSize=10
		
		2. 앞에 화살표, 제일 첫번째 페이지보다 하나 적은 값이 들어가야 한다
		/board/list.do?page=10&pageSize=10
		
		3. 현재 페이지는 페이지에 맞게끔 링크
		/board/list.do?page=16&pageSize=10
		
		4. 뒤에 화살표, 제일 마지막 페이지보다 하나 높은 값이 들어가야 한다
		/board/list.do?page=21&pageSize=10
		
		5. 테이블에 들어있는 데이터를 페이지 별로 가져 올려면 select문에 LIMIT 사용
		LIMIT[offset,] row_count 
		offset : 맨 처음부터 얼만큼 떨어져 있느냐
		row_count : 읽어올 row수
		
		예시) LIMIT 10 10 이렇게 할 경우
		데이터 전체에서 10번째(offset) 행을 건너뛰고 그 다음부터(row_count) 읽어온다
		
		예시)
		page   offset   (page-1)*10
		1      0        
		2      10
		3      20
		...
	*/
	
	private int totalCnt; // *총 게시물 갯수
	private int pageSize = 10; // *한 페이지의 크기
    private int naviSize = 10; // 페이지 내비게이션의 크기
    private int totalPage; // 전체 페이지의 갯수
    private int page; // *현재 페이지
    private int beginPage; // 내비게이션의 첫번째 페이지
    private int endPage; // 내비게이션의 마지막 페이지
    private boolean showPrev; // 이전 페이지로 이동하는 링크를 보여줄 것인지의 여부
    private boolean showNext; // 다음 페이지로 이동하는 링크를 보여줄 것인지의 여부
    
    public PageHandlerDto() {} // 기본 생성자
    
    // 페이징되는데 필요한 값이 2개일 경우
    public PageHandlerDto(int totalCnt,  int page) {
    	this(totalCnt, page, 10);
    }
    
    // 페이징되는데 필요한 값이 3가지 그리고 나머지는 계산을 해야한다
    public PageHandlerDto(int totalCnt, int page, int pageSize) {
    	this.totalCnt = totalCnt;
    	this.page = page;
    	this.pageSize = pageSize;
    	
    	// 전체 페이지 갯수 = 총 게시물 갯수 / 한 페이지의 크기(행)
        totalPage = (int)Math.ceil(totalCnt / (double)pageSize); // 올림
        
        /*
                           내비 첫번째 페이지 = 현재 페이지 / 10 * 10 + 1
         	page   beginPage   page/10*10+1 
			5      1
			15     11
			11     11
         */
        beginPage = (page-1) / naviSize * naviSize + 1;
        
        // 내비 마지막 페이지 = 내비게이션의 첫번째 페이지 + naviSize - 1
        endPage = Math.min(beginPage + naviSize - 1, totalPage); // 작은 값 쓰기
        
        // 이전 페이지로 링크 = 내비 첫 페이지 != 1
        showPrev = beginPage != 1;
        
        // 다음 페이지로 링크 = 내비 마지막 페이지 != 전체 페이지 갯수
        showNext = endPage != totalPage; // 다음 링크로 계속 이동해서 수정
        // showNext = endPage != totalPage && totalPage != 0; // 수정된 부분
        
    }
    
    public int getTotalCnt() {
		return totalCnt;
	}

	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getNaviSize() {
		return naviSize;
	}

	public void setNaviSize(int naviSize) {
		this.naviSize = naviSize;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getBeginPage() {
		return beginPage;
	}

	public void setBeginPage(int beginPage) {
		this.beginPage = beginPage;
	}

	public int getEndPage() {
		return endPage;
	}

	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}

	public boolean isShowPrev() {
		return showPrev;
	}

	public void setShowPrev(boolean showPrev) {
		this.showPrev = showPrev;
	}

	public boolean isShowNext() {
		return showNext;
	}

	public void setShowNext(boolean showNext) {
		this.showNext = showNext;
	}

	public void print(){
        // 현재 페이지
        System.out.println("page = " + page);
        // 이전 페이지로 이동하는 링크를 보여줄 것인지의 여부
        System.out.print(showPrev ? "[PREV] " : "");
        // 내비 첫 - 내비 마
        for(int i = beginPage; i <= endPage; i++){
            System.out.print(i + " ");
        }
        // 다음 페이지 링크
        System.out.println(showNext ? "[NEXT]" : "");
    }

	@Override
	public String toString() {
		return "PageHandlerDto [totalCnt=" + totalCnt + ", pageSize=" + pageSize + ", naviSize=" + naviSize
				+ ", totalPage=" + totalPage + ", page=" + page + ", beginPage=" + beginPage + ", endPage=" + endPage
				+ ", showPrev=" + showPrev + ", showNext=" + showNext + "]";
	}
}
