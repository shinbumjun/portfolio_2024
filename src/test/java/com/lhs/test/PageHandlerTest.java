package com.lhs.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import com.lhs.dto.PageHandlerDto;

class PageHandlerTest {
	// 첫 페이지, 마지막 페이지 테스트 하기!!! JUnit으로 TDD 테스트 코드 작성하기
	@Test
	void testPageHandlerDtoIntInt() {
		// int totalCnt(총 게시물 갯수),  int page(현재 페이지), 자동 pageSize = 10
		PageHandlerDto ph = new PageHandlerDto(250, 1);
		ph.print();
		System.out.println("ph = " + ph);
		// 1. 주어진 조건이 참인지를 확인
		assertTrue(ph.getBeginPage()==1); // 내비게이션의 첫번째 페이지 = 1?
		assertTrue(ph.getEndPage()==10); // 내비게이션의 마지막 페이지 = 10?
		// 2. 예상값과 실제값을 직접 비교
		assertEquals(1, ph.getBeginPage());
		assertEquals(10, ph.getEndPage());
		System.out.println();
	}

	@Test
	void testPageHandlerDtoIntInt2() {
		// int totalCnt(총 게시물 갯수),  int page(현재 페이지), 자동 pageSize = 10
		PageHandlerDto ph = new PageHandlerDto(250, 11);
		ph.print();
		System.out.println("ph = " + ph);
		// 1. 주어진 조건이 참인지를 확인
		assertTrue(ph.getBeginPage()==11); // 첫페
		assertTrue(ph.getEndPage()==20); // 마페
		System.out.println();
	}
	
	@Test
	void testPageHandlerDtoIntInt3() {
		// int totalCnt(총 게시물 갯수),  int page(현재 페이지), 자동 pageSize = 10
		PageHandlerDto ph = new PageHandlerDto(255, 25);
		ph.print();
		System.out.println("ph = " + ph);
		// 1. 주어진 조건이 참인지를 확인
		assertTrue(ph.getBeginPage()==21); // 첫페
		assertTrue(ph.getEndPage()==26); // 마페
		System.out.println();
	}
	
	@Test // 에러!! 현재 페이지가 10이면 1~9가 나와야하는데 안나옴 -> beginPage 페이지 계산 다시하기
	void testPageHandlerDtoIntInt4() {
		// int totalCnt(총 게시물 갯수),  int page(현재 페이지), 자동 pageSize = 10
		PageHandlerDto ph = new PageHandlerDto(255, 11);
		ph.print();
		System.out.println("ph = " + ph);
		// 1. 주어진 조건이 참인지를 확인
		assertTrue(ph.getBeginPage()==11); // 첫페
		assertTrue(ph.getEndPage()==20); // 마페
		System.out.println();
	}
	
	@Test // 에러!! 끝도 없이 다음 링크로 이동해서 확인
	void testPageHandlerDtoIntInt5() {
		// int totalCnt(총 게시물 갯수),  int page(현재 페이지), 자동 pageSize = 10
		PageHandlerDto ph = new PageHandlerDto(5166, 260);
		ph.print();
		System.out.println("ph = " + ph);
		// 1. 주어진 조건이 참인지를 확인
		assertTrue(ph.getBeginPage()==251); // 첫페
		assertTrue(ph.getEndPage()==260); // 마페
		System.out.println();
	}
//
//	@Test
//	void testPrint() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testToString() {
//		fail("Not yet implemented");
//	}

}
