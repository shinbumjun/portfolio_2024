package com.lhs.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.lhs.dto.AttFileDto;
import com.lhs.dto.BoardDto;
import com.lhs.dto.PageHandlerDto;
import com.lhs.dto.SearchCondition;
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

	// 2. 글쓰기 페이지로 	
	@RequestMapping("/board/goToWrite.do")
	public ModelAndView goToWrite(BoardDto boardDto) {
		ModelAndView mv = new ModelAndView();
		
		boardDto.setTypeSeq(typeSeq); // 자유게시판2

		mv.addObject("typeSeq", boardDto.getTypeSeq()); // 2값 보냄
		mv.setViewName("/board/write");
		return mv;
	}
	
	/* 
	 	1. 자유 게시판 - 페이징 : *****페이지 사이즈(pageSize)와 현재 페이지(page)는 쿼리 문자열로 가져와야 한다
		page
		pageSize
		option
	 	keyword
	*/
	@RequestMapping("/board/list.do") // PageHandlerDto pageHandlerDto -> SearchCondition sc
	public ModelAndView goLogin(HttpServletRequest request, HttpServletResponse response, BoardDto boardDto, SearchCondition sc){
		ModelAndView mv = new ModelAndView();
		HashMap<String, Object> map = new HashMap<String, Object>();
        
		System.out.println("boardDto 정보 : " + boardDto);
		// BoaedDto [boardSeq=null, typeSeq=null, memberId=null, memberNick=null, title=null, content=null, hasFile=null, hits=null, createDtm=null, updateDtm=null]
		System.out.println("sc 정보 : " + sc); 
		// 제목+내용, 신범준 입력시 -> SearchCondition [page=1, pageSize=10, offset=0, keyword=신범준, option=A]
		
		
		// 내부에서 처리
		// 2. 현재 페이지가 파라미터로 들어오지 않을 경우 기본값으로 1 설정
	    int page = sc.getPage() <= 0 ? 1 : sc.getPage();
	    System.out.println("page정보 : " + page); 
	    
		// 3. offset : 맨 처음부터 얼만큼 떨어져 있느냐, 수식 : (page-1)*10
		int offset = (page-1)*10;
		System.out.println("현재 페이지 : " + page); // 현재 페이지
		System.out.println("맨 처음부터 얼만큼 떨어져 있느냐 : " + offset); // offset : 90, 첫 페이지 : 91
		// sc.setOffset(offset); // 페이지 네비 클릭시 이동 시킬려면 넣어야함
		
	    // 4. 페이징 정보를 이용하여 특정 범위의 게시글 리스트 가져오기
	    boardDto.setTypeSeq(typeSeq); // 자유게시판2
	    
	    
		List<BoardDto> boardlist = bService.getSearchResultPage(sc); // 자유 게시판 2, ***offset
		// list(boardDto, map) -> getSearchResultCnt(sc)
		System.out.println("boardlist정보 : " + boardlist); // 검색 페이징한 페이지 리스트 가져오기
		
		// 5. 자유게시판의 총 게시물 수(totalCnt) 알아야 자유게시판 총 페이지 수(totalPage)를 알수 있다
		int totalCnt = bService.getSearchResultCnt(sc); 
		System.out.println("총 게시물 수 : " + totalCnt); // 2547
		
		// 5/5. totalPage값을 보내줘야하는데 안 담겨서 생성자 이용 (총 게시물 갯수, 현재 페이지, 한 페이지의 크기)
		PageHandlerDto pageHandler = new PageHandlerDto(totalCnt, sc);

		// 6. jsp로 보내주는것
		// mv.addObject("pageHandler", pageHandler); // 총 게시물 갯수(totalCnt), 전체 페이지의 갯수(totalPage)등 페이징에 필요한 값들을  jsp로 넘겨 주어야 한다
		// PageHandlerDto [totalCnt=2595, pageSize=10, naviSize=10, totalPage=260, 
		//                 page=1, beginPage=1, endPage=10, showPrev=false, showNext=true]
		System.out.println("pageHandler정보 : " + pageHandler);
		// PageHandlerDto [sc=SearchCondition [page=1, pageSize=10, offset=0, keyword=, option=], totalCnt=2547, 
		//                 naviSize=10, totalPage=255, beginPage=1, endPage=10, showPrev=false, showNext=true]
		
		mv.addObject("ph", pageHandler); // pageHandler -> pageHandler.getSc()
		
		mv.addObject("boardlist", boardlist); // 리스트 출력
		mv.addObject("msg", "자유게시판");
		mv.addObject("nextPage", "/board/list");
	    
		System.out.println("jsp에 보내는 내용들 : " + mv);
		return mv;
	}
	
	/*
	 	제목을 눌렀을때 읽기 페이지로
	 	boardSeq        board_seq
	 	page
	 	pageSize
	 	
	 	option
	 	keyword
	*/
	@RequestMapping("/board/read.do")
	public ModelAndView read(BoardDto boardDto, SearchCondition sc, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		
		boardDto.setTypeSeq(typeSeq); // 자유게시판2
		System.out.println("게시글 번호 : " + boardDto.getBoardSeq());
		
		// 자유게시판 글 조회
		BoardDto read = bService.read(boardDto);
		System.out.println("글 조회 : " + read);
		// BoaedDto [boardSeq=10764, typeSeq=2, memberId=sinbumjun, memberNick=범그로, title=안녕하세요, 
		// 			 content=파일 업로드는 하지 않겠습니다, hasFile=null, hits=0, createDtm=20240304164152, updateDtm=20240304164152]
		
		
		
		// 1. 첨부파일 읽기1 : 해당게시글(typeSeq, board_seq)의 모든첨부파일(다중이니까)
		List<AttFileDto> attFiles = attFileService.readAttFiles(read); // boardSeq=10776, typeSeq=2
		System.out.println("해당 게시물의 모든 첨부파일 : " + attFiles); 
		// [BoardAttachDto [fileIdx=34, typeSeq=2, boardSeq=10776, fileName=지원1.pdf, ...
		
		

		
		
		// 조회수 +1
		bService.updateHits(read);
		
		// /board/list에서 가져온 값 확인
		System.out.println("boardDto : " + boardDto);
		// BoaedDto [boardSeq=8140, typeSeq=2, memberId=null, memberNick=null, title=null, 
		//           content=null, hasFile=null, hits=null, createDtm=null, updateDtm=null]
		
		
		// 읽기에서도 검색 기능과 페이징을 하기 위해서는 list에 코드를 그대로 가져와 봤다
		System.out.println("pageHandlerDto : " + sc);
		// SearchCondition [page=1, pageSize=10, offset=0, keyword=신범준, option=A]
		
//		int totalCnt = bService.getSearchResultCnt(sc); 
//		System.out.println("총 게시물 수 : " + totalCnt); // 2547
//		PageHandlerDto pageHandler = new PageHandlerDto(totalCnt, sc);		
//		System.out.println("pageHandler정보2 : " + pageHandler);
				
		
		// PageHandlerDto [sc=SearchCondition [page=1, pageSize=10, offset=0, keyword=, option=], totalCnt=2540, 
		//                 naviSize=10, totalPage=254, beginPage=1, endPage=10, showPrev=false, showNext=true]
		
		
		// *게시판 읽기 로그인한 사용자 비교하기 위해서
		HttpSession session = request.getSession();
        session.getAttribute("memberId");
		System.out.println("게시판 읽기 로그인한 사용자 : " + session.getAttribute("memberId")); // sinbumjun
        mv.addObject("memberId", session.getAttribute("memberId"));
		
		// *게시판 읽기 글을 작성한 사용자 비교하기 위해서
		// jsp 단에서 ${read.memberId}로 확인이 됨
		
		// 목록 버튼을 누르면 해당 페이지를 보여주는 게시판 위치로
		mv.addObject("ph", sc);
		
		// 1. 첨부파일 읽기1
		mv.addObject("attFiles", attFiles);
		
		mv.addObject("read", read);
		mv.addObject("nextPage", "/board/read");
		mv.addObject("msg", "자유게시판 게시글");
		return mv;
	}	
	
	
	
	/*
	  	수정  페이지로 이 값들을 담아서 보냄, jsp에서 el문법으로 사용
	  	boardSeq                update.boardSeq
	  	page                    ph.page
	  	pageSize                ph.pageSize
	 */
	@RequestMapping("/board/goToUpdate.do")
	public ModelAndView goToUpdate(BoardDto boardDto, SearchCondition sc, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();

		boardDto.setTypeSeq(typeSeq); // 자유게시판2
		
		System.out.println("수정 페이지에 있는 boardDto 값 : " + boardDto);
		// BoaedDto [boardSeq=8186, typeSeq=2, memberId=null, memberNick=null, title=null, 
		//           content=null, hasFile=null, hits=null, createDtm=null, updateDtm=null]
		System.out.println("수정 페이지에 있는 pageHandlerDto 값 : " + sc);
		// PageHandlerDto [totalCnt=0, pageSize=10, naviSize=10, totalPage=0, page=1, 
		//                 beginPage=0, endPage=0, showPrev=false, showNext=false]
		
		// 자유게시판 수정 페이지에 해당해당하는 값들을 출력해주기 위해 기존에 사용한 read메서드 호출
		BoardDto update = bService.read(boardDto);
		System.out.println("update페이지에 뿌려주는 값: " + update);
		// BoaedDto [boardSeq=8186, typeSeq=2, memberId=sinbumjun, memberNick=범그로, title=eum...
		
		// request.getSession();
		//request.getSession().getAttribute("memberId"); // 아이디
		//request.getSession().getAttribute("memberNick"); // 별명
		HttpSession session = request.getSession();
		session.getAttribute("memberId");
		session.getAttribute("memberNick");
		
		System.out.println("현재 로그인한 사용자 정보 : " + request.getSession());
		
		
		// 1. 첨부파일 수정1 update (typeSeq, board_seq)의 모든첨부파일(다중이니까) -> attFiles
		List<AttFileDto> attFiles = attFileService.readAttFiles(boardDto); // boardSeq=10776, typeSeq=2
		mv.addObject("attFiles", attFiles); // 첨부파일 정보
		
		mv.addObject("update", update); // BoaedDto
		mv.addObject("ph", sc); // PageHandlerDto 
		mv.setViewName("/board/update"); // 페이지
		mv.addObject("msg", "수정 페이지");
		
		return mv;
	}
}