package com.lhs.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.lhs.dto.AttFileDto;
import com.lhs.dto.BoardDto;



public interface AttFileDao {

	// 0. 파일 업로드
	public int addAttFile(HashMap<String, Object> params);
	
	// 1. 첨부파일 읽기1 : typeSeq, board_seq 통한 해당 게시글의 모든 첨부파일 불러오기
	public List<AttFileDto> readAttFiles(BoardDto read); // boardSeq=10776, typeSeq=2
	
	// 2. 첨부파일  다운로드2 : pk를 통해 해당 첨부파일 불러오기
	public HashMap<String, Object> readAttFileByPk(int fileIdx);
	
	
	
	
	
	/**첨부파일 삭제(수정 페이지에서 삭제버튼 눌러 삭제하는 경우임) 
	 * 
	 * @param params
	 * @return
	 */
	public int deleteAttFile(HashMap<String, Object> params);
	
	/** 첨부파일 삭제(글 삭제하는 경우 첨부파일도 삭제되어야함) 
	 * 
	 * @param params
	 * @return
	 */
	public int deleteAttFileByBoard(HashMap<String, Object> params);
	
	/**
	 * 모든 첨부파일 셀렉
	 */
	public List<HashMap<String, Object>> readAllAttFiles();

	/**
	 * 파일 없으면 컬럼 linked 값 수정 1건. 
	public int updateLinkedInfo(int fileIdx);
	*/
	
	/**
	 * 파일 없으면 컬럼 linked 값 수정 여러건. 
	 */
	public int updateLinkedInfos(ArrayList<Integer> fileIdxs);
	
	
	
}
