package com.lhs.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.lhs.dto.BoardDto;

public interface BoardDao {
	
	// 1. 모든 리스트  + Java 코드에서는 결과를 단일 객체가 아닌 리스트로 받아야 함
	public List<BoardDto> list(HashMap<String, Object> map);
	
	// 2. 총 게시물 수를 구하기 (자유게시판의 총 게시글만 얻어오기)
	public int getCount(String typeSeq);
	
	// 3. 자유 게시판 업로드
	public int write(BoardDto boardDto);
	
	// 자유게시판 글 조회
	public BoardDto read(BoardDto boardDto);
	
	/**
	 * 조회수 증가.
	 * @param params
	 * @return
	 */
	public int updateHits(HashMap<String, Object> params);
	
	/**
	 * 글 수정 update 
	 * @param params
	 * @return
	 */
	public int update(HashMap<String, Object> params);
	
	/**
	 * 모든 첨부파일 삭제시 has_file = 0 으로 수정 
	 * @param params
	 * @return
	 */
	public int updateHasFileToZero(HashMap<String, Object> params);

	 
	/** 글 삭제 delete 
	 * @param params
	 * @return
	 */
	public int delete(HashMap<String, Object> params);

}
