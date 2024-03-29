package com.lhs.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.lhs.dto.BoardDto;
import com.lhs.dto.SearchCondition;

public interface BoardService {

	// 참고 : ArrayList<HashMap<String, Object>> : HashMap을 요소로 가지고 있는 배열 리스트
	
	// 1. 모든 리스트 -> 페이징한 리스트
	public List<BoardDto> list(BoardDto boardDto, HashMap<String, Object> map);

	// 2. 총 게시물 수를 구하기 (자유게시판의 총 게시글만 얻어오기)
	public int getCount(String typeSeq);
		
	// public int getTotalArticleCnt(HashMap<String, String> params);
	
	// 3. 자유 게시판 업로드
	public int write(BoardDto boardDto, List<MultipartFile> mFiles, HttpServletRequest request) throws IOException;

	// 자유게시판 글 조회
	public BoardDto read(BoardDto boardDto);
	
	// 게시판 삭제하기 
	public int delete(BoardDto boardDto);
	
	// 게시글 수정
	public int update(BoardDto boardDto, MultipartHttpServletRequest mFiles);
	
	// 해당 게시물 조회수 +1
	public void updateHits(BoardDto read);
		
		
	/**첨부파일 삭제(수정 페이지에서 삭제버튼 눌러 삭제하는 경우임) 
	 * 
	 * @param params
	 * @return
	 */
	public boolean deleteAttFile(HashMap<String, Object> params);

	// *****검색기능
	List<BoardDto> getSearchResultPage(SearchCondition sc);

	// *****검색 후 페이징 
	int getSearchResultCnt(SearchCondition sc);

	

}
