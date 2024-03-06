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
import com.lhs.dto.PageHandlerDto;
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

	// 1. 자유 게시판 - 페이징 : *****페이지 사이즈(pageSize)와 현재 페이지(page)는 쿼리 문자열로 가져와야 한다
	@RequestMapping("/board/list.do")
	public ModelAndView goLogin(BoardDto boardDto, PageHandlerDto pageHandlerDto){
		ModelAndView mv = new ModelAndView();
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		// 2. 현재 페이지가 파라미터로 들어오지 않을 경우 기본값으로 1 설정
	    int page = pageHandlerDto.getPage() <= 0 ? 1 : pageHandlerDto.getPage();
	    
		// 3. offset : 맨 처음부터 얼만큼 떨어져 있느냐, 수식 : (page-1)*10
		int offset = (page-1)*10;
		System.out.println("현재 페이지 : " + page); // 현재 페이지
		System.out.println("맨 처음부터 얼만큼 떨어져 있느냐 : " + offset); // offset : 90, 첫 페이지 : 91
		map.put("offset", offset);
		map.put("pageSize", pageHandlerDto.getPageSize());
		
	    // 4. 페이징 정보를 이용하여 특정 범위의 게시글 리스트 가져오기
	    boardDto.setTypeSeq(typeSeq); // 자유게시판2
	    
		List<BoardDto> boardlist = bService.list(boardDto, map); // 자유 게시판 2, [pageSize=10, currentPage=1]
		System.out.println("boardlist : " + boardlist); // 페이징한 페이지 리스트 가져오기
		
		// 5. 자유게시판의 총 게시물 수(totalCnt) 알아야 자유게시판 총 페이지 수(totalPage)를 알수 있다
		int totalCnt = bService.getCount(typeSeq); 
		System.out.println("총 게시물 수 : " + totalCnt); // 2595
		pageHandlerDto.setTotalCnt(totalCnt);
		
		// 5/5. totalPage값을 보내줘야하는데 안 담겨서 생성자 이용 (총 게시물 갯수, 현재 페이지, 한 페이지의 크기)
		PageHandlerDto pageHandler = new PageHandlerDto(totalCnt, page);
		
		// 6. jsp로 보내주는것
		mv.addObject("pageHandler", pageHandler); // 총 게시물 갯수(totalCnt), 전체 페이지의 갯수(totalPage)등 페이징에 필요한 값들을  jsp로 넘겨 주어야 한다
		// PageHandlerDto [totalCnt=2595, pageSize=10, naviSize=10, totalPage=260, 
		//                 page=1, beginPage=1, endPage=10, showPrev=false, showNext=true]
		System.out.println("pageHandler : " + pageHandler);
		
		mv.addObject("ph", pageHandler);
		mv.addObject("boardlist", boardlist); // 리스트 출력
		mv.addObject("msg", "자유게시판");
		mv.addObject("nextPage", "/board/list");
	    
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
	
	/*
	 	제목을 눌렀을때 읽기 페이지로
	 	boardSeq        board_seq
	*/
	@RequestMapping("/board/read.do")
	public ModelAndView read(BoardDto boardDto, PageHandlerDto pageHandlerDto) {
		ModelAndView mv = new ModelAndView();
		
		boardDto.setTypeSeq(typeSeq); // 자유게시판2
		System.out.println("게시글 번호 : " + boardDto.getBoardSeq());
		
		// 자유게시판 글 조회
		BoardDto read = bService.read(boardDto);
		System.out.println("글 조회 : " + read);
		// BoaedDto [boardSeq=10764, typeSeq=2, memberId=sinbumjun, memberNick=범그로, title=안녕하세요, 
		// 			 content=파일 업로드는 하지 않겠습니다, hasFile=null, hits=0, createDtm=20240304164152, updateDtm=20240304164152]
		
		// /board/list에서 가져온 값 확인
		System.out.println("boardDto : " + boardDto);
		// BoaedDto [boardSeq=8140, typeSeq=2, memberId=null, memberNick=null, title=null, 
		//           content=null, hasFile=null, hits=null, createDtm=null, updateDtm=null]
		System.out.println("pageHandlerDto : " + pageHandlerDto);
		// PageHandlerDto [totalCnt=0, pageSize=10, naviSize=10, totalPage=0, page=6, 
		//                 beginPage=0, endPage=0, showPrev=false, showNext=false]
		
		// 목록 버튼을 누르면 해당 페이지를 보여주는 게시판 위치로
		mv.addObject("ph", pageHandlerDto);
		
		mv.addObject("read", read);
		mv.addObject("nextPage", "/board/read");
		mv.addObject("msg", "자유게시판 게시글");
		return mv;
	}	
}










