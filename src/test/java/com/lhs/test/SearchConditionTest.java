//package com.lhs.test;
//
//import java.util.List;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.lhs.dao.BoardDao;
//import com.lhs.dto.BoardDto;
//import com.lhs.dto.SearchCondition;
//
//class SearchConditionTest {
//
//	@Autowired
//	private BoardDao boardDao;
//	
//	// 검색 기능 테스트 
//	@Test
//	void SearchConditionTest() {
//		
//		// page, pageSize. keyword, option
//		SearchCondition sc = new SearchCondition(1, 10, "2", "T");
//		// SearchCondition [page=1, pageSize=10, offset=0, keyword=2, option=T]
//		System.out.println(sc);
//		
//		// BoardDao.java에 검색기능 테스트
//		List<BoardDto> list = boardDao.searchSelectPage(sc);
//		System.out.println(list);
//		
//	}
//
//}
//
//
//
//
//
//
//
//
