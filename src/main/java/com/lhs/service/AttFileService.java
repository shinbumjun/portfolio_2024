package com.lhs.service;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.lhs.dto.AttFileDto;
import com.lhs.dto.BoardDto;

public interface AttFileService {
	
	// 1. 첨부파일 읽기1 : 해당게시글(typeSeq, board_seq)의 모든첨부파일(다중이니까)
	public List<AttFileDto> readAttFiles(BoardDto read);
	
	// 2. 첨부파일  다운로드2 : pk를 통해 해당 첨부파일 불러오기
	public HashMap<String, Object> readAttFileByPk(int fileIdx);
	
	
	//첨부파일 테이블에 있는 정복와 물리적 파일간 연결끊긴 데이터 찾아서 특정클럼(linked)에 표시(update)하라.

	/* 파일 없으면 컬럼 linked 값 수정 1건. */
	public int updateLinkedInfo();
		
}
