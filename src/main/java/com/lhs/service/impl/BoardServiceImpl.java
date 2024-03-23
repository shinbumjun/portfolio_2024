package com.lhs.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.lhs.dao.AttFileDao;
import com.lhs.dao.BoardDao;
import com.lhs.dao.ReplyDao;
import com.lhs.dto.BoardDto;
import com.lhs.dto.SearchCondition;
import com.lhs.service.BoardService;
import com.lhs.util.FileUtil;

@Service
public class BoardServiceImpl implements BoardService{

	@Autowired BoardDao bDao;
	@Autowired AttFileDao attFileDao;
	@Autowired FileUtil fileUtil;
	@Autowired ReplyDao replyDao;
	
	@Value("#{config['project.file.upload.location']}") // 환경 변수에 있는 값을 저장해주는것
	private String saveLocation;
	
	// 1. 모든 리스트 -> 페이징한 리스트
	@Override
	public List<BoardDto> list(BoardDto boardDto, HashMap<String, Object> map) {
        
        map.put("typeSeq", boardDto.getTypeSeq()); // 2 자유게시판
        System.out.println("페이징한 리스트를 뽑기 위해 map에 담은 값들 : " + map); // {typeSeq=2, offset=0, pageSize=10}
        
		return bDao.list(map);
	}
	
	// 2. 총 게시물 수를 구하기 (자유게시판의 총 게시글만 얻어오기)
	@Override
	public int getCount(String typeSeq) {
		return bDao.getCount(typeSeq);
	}
	

	// 3. 자유 게시판 업로드
	@Override
	public int write(BoardDto boardDto, List<MultipartFile> mFiles, HttpServletRequest request) throws IOException {	
		HashMap<String, Object> map = new HashMap<String, Object>();
		HashMap<String, Object> update = new HashMap<String, Object>();
		
		// {memberNick=, is_ajax=true, memberIdx=, action=contact_send, title=신범준 제목, 
		//  content=신범준 내용, memberId=sinbumjun, typeSeq=2, boardSeq=9}
		int result = bDao.write(boardDto); 
		System.out.println(result); // 1
		if(result == 0) {
			 return -1;
		}
		
		boolean anyFileUploaded = false; // **두 파일 중 하나라도 업로드된 경우를 판별하기 위한 변수
		
		// MultipartHttpServletRequest), application/pdf, 오라클정리.pdf, attFiles, 2650256, application/octet-stream, attFiles, 0
		for(MultipartFile mFile : mFiles) {
			// fakename 만들기
			// to-do : smart_123.pdf -> (UUID).pdf
			// to-do : smart_123.456.pds -> (UUID).pdf
			// 
			String fakename = UUID.randomUUID().toString().replace("-", ""); // 이렇게 쓰면 확장자 날라감
			System.out.println("fakename : " + fakename); // 053360b594904fcbb1e6b1c5f93c5009
			
			try {
				fileUtil.copyFile(mFile, fakename); // 서버에 파일이 물리적으로 복사가 되어야 한다
				
				System.out.println("mFile : " + mFile);
				// MultipartFile[field="attFiles", filename=chrome.exe, contentType=application/x-msdownload, size=2754848]
				System.out.println(mFile.getContentType());
				System.out.println(mFile.getOriginalFilename());
				System.out.println(mFile.getName());
				System.out.println(mFile.getSize());
				
				map.put("type", mFile.getContentType());
				map.put("filename", mFile.getOriginalFilename());
				map.put("size", mFile.getSize());
				map.put("fakename", fakename);
				map.put("boardSeq", boardDto.getBoardSeq());
				map.put("typeSeq", boardDto.getTypeSeq());
				map.put("saveLocation", saveLocation);
				System.out.println("map : "+ map);
				
				// 파일 사이즈가 0이 아닐때만 insert함 
				if(mFile.getSize() != 0) {
					// 첨부 파일 추가
					int success = attFileDao.addAttFile(map);
					System.out.println("success : " + success);
					
					// 파일 업로드가 실패한 경우 예외 던지기
					if(success == 0) {
						throw new IOException("파일이 업로드 되지 않았습니다");
					}else {
						anyFileUploaded = true; // ***파일이 업로드된 경우 변수값을  true로 설정
					}
				}
				
			}catch(IOException e){
				// request 객체는 Spring MVC의 컨트롤러나 서블릿에서만 직접 사용할 수 있다 (컨트롤러에서 받아야 한다)
				request.setAttribute("errorMessage", e.getMessage()); // 에러메시지 받기 위해서 
			    // 파일 업로드 실패 예외 메시지 던지고 -1 반환
			    return 0;
			}
		}
		
		// ***has_file : 파일이 있냐 없냐에 따라서 Y/N -> BoardSeq, TypeSeq값을 가지고 hasFile 수정
		String hasFile = anyFileUploaded ? "Y" : "N";
		update.put("BoardSeq", boardDto.getBoardSeq());
		update.put("TypeSeq", boardDto.getTypeSeq());
		update.put("hasFile", hasFile);
		int updateResult = bDao.updateHasFile(update);
		
		if (updateResult == 0) {
	        throw new RuntimeException("has_file 칼럼 업데이트에 실패했습니다.");
	    }
		
		System.out.println("boardDto : " + boardDto);
		return 1; // 업로드를 하고 성공한 경우
	}

	// 자유게시판 글 조회
	@Override
	public BoardDto read(BoardDto boardDto) {
		
		// 게시글 글 조회시 조회수 +1
		System.out.println("조회수 : " + boardDto);
		// BoaedDto [boardSeq=8186, typeSeq=2, memberId=null, memberNick=null, title=null, 
		// content=null, hasFile=null, hits=null, createDtm=null, updateDtm=null]
		
		return bDao.read(boardDto);
	}

	/* 
	 	게시판 삭제하기
	 	boardSeq           board_seq
	 	typeSeq            type_seq
	 	
	 	추가로 HasFile 값이 무엇인지 가져오기
	 */
	@Override
	@Transactional // 트랜잭션 사용
	public int delete(BoardDto boardDto) {
	    try {
	    	// 5. 게시글에 있는 댓글 모두 삭제
	    	int delete = replyDao.AlldeleteReply(boardDto);
	    	System.out.println("삭제된 댓글 수: " + delete);
	    	// 삭제된 댓글 수
	    	
	        // 게시글 삭제시 가지고 있는 값 출력
	        System.out.println("게시글 삭제시 가지고 있는 값: " + boardDto); // boardSeq, typeSeq
	        
	        // 게시판 삭제시 HasFile 값 가져오기
	        String hasFile = bDao.getHasFile(boardDto); 
	        System.out.println("게시글에 첨부파일이 있는지 확인: " + hasFile); // 첨부파일 있으면 Y, 없으면 N
	        
	        // 첨부파일이 있는 경우
	        if (hasFile != null && hasFile.equals("Y")) {  
	            System.out.println("첨부파일이 있는 게시글 삭제");

	            // 여기서 첨부파일 삭제 등의 추가 작업 수행
	            // 파일 처리 코드 작성

	            bDao.deleteAttachedFiles(boardDto); // 첨부파일 먼저 삭제
	            bDao.delete(boardDto); // 게시글 삭제

	            return 1; // 정상적으로 삭제되었음을 반환
	        } else { // 첨부파일이 없는 경우
	            System.out.println("첨부파일이 없는 게시글 삭제");
	            
	            // 게시글 번호(board_seq)에 혹시라도 첨부파일이 있을수도 있어서 확인하고 삭제 
	            if (bDao.hasAttachedFiles(boardDto.getBoardSeq())) {
	            	System.out.println("혹시했는데 첨부파일이 있었다");
	                bDao.deleteAttachedFiles(boardDto); // 첨부파일 먼저 삭제
	            }
	            
	            // 게시글 삭제
	            return bDao.delete(boardDto); // 첨부파일이 없는 게시글 삭제
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        // 예외 처리 또는 로깅 등을 수행할 수 있습니다.
	        return 0; // 삭제에 실패했음을 반환
	    }
	}
	
	// 게시글 수정
	@Override
	public int update(BoardDto boardDto, MultipartHttpServletRequest mFiles) {
		
//		if(params.get("hasFile").equals("Y")) { // 첨부파일 존재시 			
//			// 파일 처리
//		}	
		
		// 글 수정 dao 
		return bDao.update(boardDto);
	}

	// 해당 게시물 조회수 +1
	@Override
	public void updateHits(BoardDto read) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		System.out.println("해당 게시물 조회수 올리기 : " + read);
		// BoaedDto [boardSeq=8186, typeSeq=2, memberId=sinbumjun, memberNick=범그로, title=수정하기2, 
		//           content=test2, hasFile=, hits=1, createDtm=null, updateDtm=20240308163520]	
		map.put("boardSeq", read.getBoardSeq());
		map.put("typeSeq", read.getTypeSeq());
		
		bDao.updateHits(map);
	}
		

	@Override
	public boolean deleteAttFile(HashMap<String, Object> params) {
		boolean result = false;		
		return result;
	}

	
	/*
	 	검색기능 
	 	keyword ="" (초기값), offset=0 (초기값), pageSize=10 전달
	 */
	@Override
	public List<BoardDto> getSearchResultPage(SearchCondition sc){
		System.out.println("sc정보2 : " + sc); // SearchCondition [page=1, pageSize=10, offset=0, keyword=, option=]
		return bDao.searchSelectPage(sc);
	};
		
	// 검색 후 페이징 
	@Override
	public int getSearchResultCnt(SearchCondition sc) {
		return bDao.searchResyltCnt(sc);
	};

}





