package com.lhs.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lhs.dao.AttFileDao;
import com.lhs.dao.BoardDao;
import com.lhs.dto.BoardDto;
import com.lhs.dto.PagingDto;
import com.lhs.dto.TotalCountAndMaxPageDto;
import com.lhs.service.BoardService;
import com.lhs.util.FileUtil;

@Service
public class BoardServiceImpl implements BoardService{

	@Autowired BoardDao bDao;
	@Autowired AttFileDao attFileDao;
	@Autowired FileUtil fileUtil;
	
	@Value("#{config['project.file.upload.location']}") // 환경 변수에 있는 값을 저장해주는것
	private String saveLocation;
	
	// 1. 모든 리스트 -> 페이징한 리스트
	@Override
	public List<BoardDto> list(BoardDto boardDto, PagingDto pagingDto) {
		HashMap<String, Object> map = new HashMap<String, Object>(); // 필요한 값들 map에 담아서 리턴하려고
		
		System.out.println("페이징을 하기 위해서 만든 Dto : " + pagingDto); // PagingDto [pageSize=10, currentPage=1]
		
		//  해당 페이지에서 시작  = (현재 페이지-1) * 페이지 사이즈 +1;
		int startRow = (pagingDto.getCurrentPage() - 1) * pagingDto.getPageSize() + 1; // 1
		System.out.println("해당 페이지에서 시작 : " + startRow);
		
		// 해당 페이지에서 끝 = 시작 + 페이지 사이즈 - 1;
        int endRow = startRow + pagingDto.getPageSize() - 1; // 10
        System.out.println("해당 페이지에서 끝 : " + endRow);
        
        map.put("typeSeq", boardDto.getTypeSeq()); // 2 자유게시판
        map.put("startRow", startRow); // startRow 시작
        map.put("endRow", endRow);
        map.put("pageSize", pagingDto.getPageSize()); // pageSize 페이지 사이즈
        
		return bDao.list(map);
	}
	
	
	
	// 총 게시글 수 totalCount, 총 페이지 수 maxPage를 가져오기 위해서 작성해야함
	@Override
	public TotalCountAndMaxPageDto getTotalCountAndMaxPage(PagingDto pagingDto) {
		// HashMap<String, Object> map = new HashMap<String, Object>(); 
		
        int totalCount = bDao.getTotalCount(pagingDto); 
        System.out.println("총 게시글 수 가져오기 : " + totalCount); // 총 게시글 수 가져오기 : 5166
        
        int maxPage = (int) Math.ceil((double) totalCount / pagingDto.getPageSize());
        System.out.println("총 페이지 수 계산  : " + maxPage); // 총 페이지 수 계산 : 517
        
        TotalCountAndMaxPageDto resultDto = new TotalCountAndMaxPageDto();
        
        resultDto.setTotalCount(totalCount);
        resultDto.setMaxPage(maxPage);
        
        // map.put("totalCount", totalCount); // 총 게시글
        // map.put("maxPage", maxPage); // 총 페이지 
        
        return resultDto; // 반환값이 명확해지고, 코드의 가독성과 유지보수성이 향상
	}
	
	
	
	@Override
	public int getTotalArticleCnt(HashMap<String, String> params) {
		return bDao.getTotalArticleCnt(params);
	}

	// 3. 자유 게시판 업로드
	@Override
	public int write(BoardDto boardDto, List<MultipartFile> mFiles, HttpServletRequest request) throws IOException {	
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		// {memberNick=, is_ajax=true, memberIdx=, action=contact_send, title=신범준 제목, content=신범준 내용, memberId=sinbumjun, typeSeq=2, boardSeq=9}
		int result = bDao.write(boardDto); 
		System.out.println("boardDto : " + boardDto.getTypeSeq()); // 12
		System.out.println(result); // 1
		
		if(result == 0) {
			 return -1;
		}
//		MultipartHttpServletRequest)
//		application/pdf
//		오라클정리.pdf
//		attFiles
//		2650256
//		application/octet-stream
//
//		attFiles
//		0
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
				// map.put("Name", mFile.getName()); //
				map.put("size", mFile.getSize());
				map.put("fakename", fakename);
				map.put("boardSeq", boardDto.getBoardSeq());
				map.put("typeSeq", boardDto.getTypeSeq());
				map.put("saveLocation", saveLocation);
				System.out.println("map : "+ map);
				
				// 파일 사이즈가 0이 아닐때만 inset함 
				if(mFile.getSize() != 0) {
					// 첨부 파일 추가
					int success = attFileDao.addAttFile(map);
					System.out.println("success : " + success);
					
					// 파일 업로드가 실패한 경우 예외 던지기
					if(success == 0) {
						throw new IOException("파일이 업로드 되지 않았습니다");
					}
				}
				
			}catch(IOException e){
				// request 객체는 Spring MVC의 컨트롤러나 서블릿에서만 직접 사용할 수 있다 (컨트롤러에서 받아야 한다)
				request.setAttribute("errorMessage", e.getMessage()); // 에러메시지 받기 위해서 
			    // 파일 업로드 실패 예외 메시지 던지고 -1 반환
			    return 0;
			}
		}
		
		System.out.println("boardDto : " + boardDto);
		return 1; // 업로드를 하고 성공한 경우
	}

	

	//글 조회 
	@Override
	public HashMap<String, Object> read(HashMap<String, Object> params) {
		return bDao.read(params);
	}

	@Override
	public int update(HashMap<String, Object> params, List<MultipartFile> mFiles) {
		if(params.get("hasFile").equals("Y")) { // 첨부파일 존재시 			
			// 파일 처리
		}	
		// 글 수정 dao 
		return bDao.update(params);
	}

	@Override
	public int delete(HashMap<String, Object> params) {
		if(params.get("hasFile").equals("Y")) { // 첨부파일 있으면 		
			 // 파일 처리
		}
		return bDao.delete(params);
	}

	@Override
	public boolean deleteAttFile(HashMap<String, Object> params) {
		boolean result = false;		
		return result;
	}

}





