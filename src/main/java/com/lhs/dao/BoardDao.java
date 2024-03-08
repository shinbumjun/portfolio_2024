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
	
	// ***has_file : 파일이 있냐 없냐에 따라서 Y/N -> BoardSeq, TypeSeq값을 가지고 hasFile 수정
	public int updateHasFile(HashMap<String, Object> update);
	
	
	
	
	
	// 첨부파일 먼저 삭제
	public void deleteAttachedFiles(BoardDto boardDto);
	
	// 게시글 삭제
	public int delete(BoardDto boardDto);
	
	// 혹시라도 첨부파일이 있을 수 있기 때문에 확인
	public boolean hasAttachedFiles(Integer boardSeq);
	
	// 게시판 삭제시  HasFile 값이 무엇인지 가져오기
	public String getHasFile(BoardDto boardDto);
	
	
	
	
	// 게시글 수정
	public int update(BoardDto boardDto);
	
	
	
	/**
	 * 조회수 증가.
	 * @param params
	 * @return
	 */
	public int updateHits(HashMap<String, Object> params);
	
	

	

	

	
	
	

}
