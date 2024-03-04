package com.lhs.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import com.lhs.dto.BoardDto;

public interface BoardService {

	// 참고 : ArrayList<HashMap<String, Object>> : HashMap을 요소로 가지고 있는 배열 리스트
	
	// 1. 모든 리스트
	public List<BoardDto> list(BoardDto boardDto);
	
	public int getTotalArticleCnt(HashMap<String, String> params);
	
	// 3. 자유 게시판 업로드
	public int write(BoardDto boardDto, List<MultipartFile> mFiles, HttpServletRequest request) throws IOException;

	/**
	 * 글 조회  
	 */
	public HashMap<String, Object> read(HashMap<String, Object> params);
	/**
	 * 글 수정 update 
	 * @param params
	 * @return
	 */
	public int update(HashMap<String, Object> params, List<MultipartFile> mFiles);
	
	/**첨부파일 삭제(수정 페이지에서 삭제버튼 눌러 삭제하는 경우임) 
	 * 
	 * @param params
	 * @return
	 */
	public boolean deleteAttFile(HashMap<String, Object> params);
	
	/** 글 삭제 delete 
	 * @param params
	 * @return
	 */
	public int delete(HashMap<String, Object> params);

}
