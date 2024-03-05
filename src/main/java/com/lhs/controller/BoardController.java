package com.lhs.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.lhs.dto.BoardDto;
import com.lhs.dto.PagingDto;
import com.lhs.dto.TotalCountAndMaxPageDto;
import com.lhs.service.AttFileService;
import com.lhs.service.BoardService;
import com.lhs.util.FileUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class BoardController {

	@Autowired BoardService bService;
	@Autowired AttFileService attFileService;
	@Autowired FileUtil fileUtil;

	private String typeSeq = "2";

	// 자유 게시판 (페이징 : 페이지 사이즈와 현재 페이지는 쿼리 문자열로 가져와야 한다)
	@RequestMapping("/board/list.do")
	public ModelAndView goLogin(BoardDto boardDto, PagingDto pagingDto, @RequestParam(defaultValue = "1") int currentPage){
		ModelAndView mv = new ModelAndView();
		
//		// 페이징 구현하기
//		System.out.println("페이징을 하기 위해서 만든 Dto : " + pagingDto); // PagingDto [pageSize=10, currentPage=null]
//		
//		// 페이징을 하기 위해서는 페이지 사이즈(pageSize=10), 현재 페이지(currentPage)를 요청 파라미터로 받아와야 한다
//		pagingDto.setCurrentPage(currentPage);
//		System.out.println("현재 페이지 : " + pagingDto.getCurrentPage()); // 처음 값이 안들어오면 1
//		
//		// 총 페이지 수를 가져오기 위해서 작성 (수정)
//		// 총 게시글 수도 필요할 것 같다
//		int totalCount = bService.MaxPage(pagingDto);
//		System.out.println("총 게시글 수 : " + totalCount); // 총 게시글 수 : 5166
//		int maxPage = (int) Math.ceil((double) totalCount / pagingDto.getPageSize()); // 총 페이지 수 계산
//	    System.out.println("총 페이지 : " + maxPage); // 총 페이지 : 517
//	    
//	    mv.addObject("currentPage", pagingDto.getCurrentPage()); // 현재 페이지		
//	    mv.addObject("MaxPage", maxPage); // 총 페이지		
	    
		// 총 페이지 수와 총 게시글 수를 가져오기
		TotalCountAndMaxPageDto totalCount = bService.getTotalCountAndMaxPage(pagingDto);
		System.out.println("총 게시글c : " + totalCount.getTotalCount());  // 총 게시글c : 5166
		System.out.println("총 페이지c : " + totalCount.getMaxPage()); // 총 페이지c : 517
		
	    // 리스트 가져오기
	    // 페이징 정보를 이용하여 특정 범위의 게시글 리스트 가져오기
		pagingDto.setCurrentPage(currentPage);
	    boardDto.setTypeSeq(typeSeq); // 자유게시판2
	    
		List<BoardDto> boardlist = bService.list(boardDto, pagingDto); // 자유 게시판 2, [pageSize=10, currentPage=1]
		System.out.println("boardlist : " + boardlist); // boardlist : [BoaedDto [boardSeq=10763,... 10개 가져오기
		
		mv.addObject("boardlist", boardlist); // 리스트 출력
		mv.addObject("msg", "자유게시판");
		mv.addObject("nextPage", "/board/list");
		
		// 페이징 정보 전달
	    mv.addObject("currentPage", currentPage); // currentPage=1 : 현재 페이지
	    mv.addObject("maxPage", totalCount.getMaxPage()); // maxPage=517 : 총 페이지 수
	    
	    // 컨트롤러에서 PagingDto를 사용하여 startPage와 endPage 값을 계산하고 모델에 추가
	    PagingDto paging = new PagingDto();
	    paging.setCurrentPage(currentPage);
	    int startPage = paging.calculateStartPage();
	    int endPage = paging.calculateEndPage(totalCount.getMaxPage());

	    // 모델에 startPage와 endPage 추가
	    mv.addObject("startPage", startPage);
	    mv.addObject("endPage", endPage);
	    
		System.out.println("jsp에 보내는 내용들 : " + mv);
		return mv;
	}
	
	// 파일 다운로드
	@RequestMapping("/board/download.do")
	@ResponseBody
	public byte[] downloadFile(@RequestParam int fileIdx, HttpServletResponse rep) {
		
		//1.받아온 파람의 파일 pk로 파일 전체 정보 불러온다. -attFilesService필요! 
		HashMap<String, Object> fileInfo = null;
		
		//2. 받아온 정보를 토대로 물리적으로 저장된 실제 파일을 읽어온다.
		byte[] fileByte = null;
		
		if(fileInfo != null) { //지워진 경우 
			//파일 읽기 메서드 호출 
			fileByte = fileUtil.readFile(fileInfo);
		}
		
		//돌려보내기 위해 응답(httpServletResponse rep)에 정보 입력. **** 응답사용시 @ResponseBody 필요 ! !
		//Response 정보전달: 파일 다운로드 할수있는 정보들을 브라우저에 알려주는 역할 
		rep.setHeader("Content-Disposition", "attachment; filename=\""+fileInfo.get("file_name") + "\""); //파일명
		rep.setContentType(String.valueOf(fileInfo.get("file_type"))); // content-type
		rep.setContentLength(Integer.parseInt(String.valueOf(fileInfo.get("file_size")))); // 파일사이즈 
		rep.setHeader("pragma", "no-cache");
		rep.setHeader("Cache-Control", "no-cache");
		
		return fileByte;
		// /board/download.do?fileIdx=1
		
	}
	
	/*
	  	3. 자유 게시판 업로드
	  					       board_seq
	  					       type_seq
	 	memberId               member_id
	  	title				   title
	  	memberNick             member_nick
	  	content				   content
	  		                   hits
	  	attFiles(파일)
	  	attFiles(파일)
	 */
	@RequestMapping("/board/write.do")
	@ResponseBody
	public HashMap<String, Object> write(BoardDto boardDto, MultipartHttpServletRequest mReq, HttpServletRequest request) throws IOException{
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		boardDto.setTypeSeq(typeSeq); // 자유게시판2
		
		int result = bService.write(boardDto, mReq.getFiles("attFiles"), request); // 1. 타입에 들어있는 리스트로 만들어서 보냄
		System.out.println("result : " + result); // 1
		
		if(result == -1) { // 자유게시판 업로드가 되지 않았다면 
			map.put("msg", "자유 게시판이 업로드 되지 않았습니다."); // 실패 메시지 설정
			// map.put("resultMsg", request.getAttribute("errorMessage"));
		}
		
		System.out.println("boardDto : " + boardDto);
		// {memberNick=, is_ajax=true, memberIdx=, action=contact_send, title=신범준 제목, content=신범준 내용, memberId=sinbumjun, typeSeq=2, boardSeq=9}
		System.out.println("mReq : " + mReq);
		// org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest@66de9ccc

		map.put("msg", "게시판이 성공적으로 등록되었습니다");
		map.put("result",result);
		
		return map;
	}
}
