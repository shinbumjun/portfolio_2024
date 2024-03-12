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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.lhs.dto.AttFileDto;
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

	*/
	@RequestMapping("/board/list.do")
	public ModelAndView goLogin(HttpServletRequest request, HttpServletResponse response, BoardDto boardDto, PageHandlerDto pageHandlerDto){
		ModelAndView mv = new ModelAndView();
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		
		
		// http://localhost:8088/lhs/board/list.do 
		System.out.println("request.getRequestURL() : " + request.getRequestURL()); // 현재 요청의 URL을 나타내는 메소드

		// 세션에서 memberId 가져오기
	    HttpSession session = request.getSession();
	    String memberId = (String) session.getAttribute("memberId");
        System.out.println("자유게시판 이동시 로그인이 되어 있는지 확인 : " + session.getAttribute("memberId")); // 로그인이 안되어 있으면 null
        
        // 로그인이 되어 있지 않은 경우
//        if(memberId == null) {
//        	System.out.println("로그인이 되어 있지 않다");
//        	// 자유게시판으로의 요청이 발생한 URL을 세션에 저장
//        	mv.addObject("toURL", request.getRequestURL().toString());
//        	mv.addObject("msg", "로그인 페이지");
//    		mv.addObject("nextPage", "/member/goLoginPage.go"); // javascript:movePage('/member/goLoginPage.do')
//    		
//    		mv.addObject("msg", "자유게시판");
//    		mv.addObject("nextPage", "/board/list");
//    		return mv;
//        }
		
		
        
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
	public byte[] downloadFile(@RequestParam int fileIdx, HttpServletResponse response) throws UnsupportedEncodingException{
		
		//1.받아온 사람의 파일 pk로 파일 전체 정보 불러온다. -attFilesService필요! 
		HashMap<String, Object> fileInfo = attFileService.readAttFileByPk(fileIdx);
		System.out.println("받아온 사람의 파일 pk로 파일 전체 정보 : " + fileInfo);
		// {board_seq=10778, save_loc=c:/dev/tmp/, file_name=master_preferences, file_type=application/octet-stream, 
		// file_idx=38, create_dtm=20240311183340, file_size=135568, type_seq=2, fake_filename=fb7ad3649c0340d0a1b66d08535dbd72}
		
		//2. 받아온 정보를 토대로 물리적으로 저장된 실제 파일을 읽어온다.
		// byte[] fileByte = null;
		
		if(fileInfo == null) {
			// 파일 정보가 없는 경우, 적절한 HTTP 상태 코드 반환
            return null;
		}
		
		//돌려보내기 위해 응답(httpServletResponse response)에 정보 입력. **** 응답사용시 @ResponseBody 필요 ! !
		//Response 정보전달: 파일 다운로드 할수있는 정보들을 브라우저에 알려주는 역할 
		
		// 파일 이름에 공백이나 특수 문자가 포함되어 있더라도 올바르게 작동하도록 보다 안전하게 처리
		response.setContentType((String) fileInfo.get("file_type")); // 응답 컨텐츠 타입을 결정
		response.setHeader("Content-Disposition", "attachment; filename=\"" +  URLEncoder.encode((String) fileInfo.get("file_name") , "UTF-8").replaceAll("\\+", "%20")+ "\"");
		// URLEncoder.encode((String) fileInfo.get("file_name") , "UTF-8").replaceAll("\\+", "%20") : 파일 이름을 UTF-8로 인코딩
		// response.setHeader("Content-Disposition", "attachment; filename=\"" + ... + "\""); : 다운로드할 파일의 이름을 설정
		
		return fileUtil.readFile(fileInfo); // FileUtil.java 사용
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
	 	page
	 	pageSize
	*/
	@RequestMapping("/board/read.do")
	public ModelAndView read(BoardDto boardDto, PageHandlerDto pageHandlerDto, HttpServletRequest request) {
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
		System.out.println("pageHandlerDto : " + pageHandlerDto);
		// PageHandlerDto [totalCnt=0, pageSize=10, naviSize=10, totalPage=0, page=6, 
		//                 beginPage=0, endPage=0, showPrev=false, showNext=false]
		
		
		// *게시판 읽기 로그인한 사용자 비교하기 위해서
		HttpSession session = request.getSession();
        session.getAttribute("memberId");
		System.out.println("게시판 읽기 로그인한 사용자 : " + session.getAttribute("memberId")); // sinbumjun
        mv.addObject("memberId", session.getAttribute("memberId"));
		
		// *게시판 읽기 글을 작성한 사용자 비교하기 위해서
		// jsp 단에서 ${read.memberId}로 확인이 됨
		
		// 목록 버튼을 누르면 해당 페이지를 보여주는 게시판 위치로
		mv.addObject("ph", pageHandlerDto);
		
		// 1. 첨부파일 읽기1
		mv.addObject("attFiles", attFiles);
		
		mv.addObject("read", read);
		mv.addObject("nextPage", "/board/read");
		mv.addObject("msg", "자유게시판 게시글");
		return mv;
	}	
	
	/*
	 	게시글 삭제
	 	boardSeq           board_seq
	 	typeSeq            type_seq
	*/
	@RequestMapping("/board/delete.do")
	@ResponseBody
	public HashMap<String, Object> delete(BoardDto boardDto, PageHandlerDto pageHandlerDto, HttpSession session) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		// ModelAndView mv = new ModelAndView(); 

		boardDto.setTypeSeq(typeSeq); // 자유게시판2
		System.out.println("boardDto : " + boardDto);
		// BoaedDto [boardSeq=10764, typeSeq=2, memberId=null, memberNick=null, title=null, 
		//           content=null, hasFile=null, hits=null, createDtm=null, updateDtm=null]
		
		System.out.println("pageHandlerDto : " + pageHandlerDto);
		// PageHandlerDto [totalCnt=0, pageSize=10, naviSize=10, totalPage=0, page=1, 
		//                 beginPage=0, endPage=0, showPrev=false, showNext=false]		
		// 게시판 삭제하기 
		int result = bService.delete(boardDto);
		System.out.println("게시판 삭제 : " + result); // 1
		
		if(result == 1) {
			//map.put("page", pageHandlerDto.getPage()); // 원래 페이지로
			//map.put("pageSize", pageHandlerDto.getPageSize());
			map.put("nextPage", "/board/list");
			map.put("msg", "게시글 삭제에 성공하였습니다");
		}else {
			//map.put("ph", pageHandlerDto); // 원래 페이지로
			map.put("nextPage", "/board/list");
			map.put("msg", "게시글 삭제에 실패하였습니다");
		}
		return map;
	}
	
	/*
	  	수정  페이지로 이 값들을 담아서 보냄, jsp에서 el문법으로 사용
	  	boardSeq                update.boardSeq
	  	page                    ph.page
	  	pageSize                ph.pageSize
	 */
	@RequestMapping("/board/goToUpdate.do")
	public ModelAndView goToUpdate(BoardDto boardDto, PageHandlerDto pageHandlerDto, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();

		boardDto.setTypeSeq(typeSeq); // 자유게시판2
		
		System.out.println("수정 페이지에 있는 boardDto 값 : " + boardDto);
		// BoaedDto [boardSeq=8186, typeSeq=2, memberId=null, memberNick=null, title=null, 
		//           content=null, hasFile=null, hits=null, createDtm=null, updateDtm=null]
		System.out.println("수정 페이지에 있는 pageHandlerDto 값 : " + pageHandlerDto);
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
		mv.addObject("ph", pageHandlerDto); // PageHandlerDto 
		mv.setViewName("/board/update"); // 페이지
		mv.addObject("msg", "수정 페이지");
		
		return mv;
	}
	
	/*
	 	게시글 수정
	 	boardSeq
	 	typeSeq
	 	
	 	title           title
	 	content         content
	 	attFiles
	*/
	@RequestMapping("/board/update.do")
	@ResponseBody // !!!!!!!!!!!! 비동기 응답 
	public HashMap<String, Object> update(BoardDto boardDto, PageHandlerDto pageHandlerDto, MultipartHttpServletRequest mReq) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		boardDto.setTypeSeq(typeSeq); // 자유게시판2
		System.out.println("수정 한것은 update하기 위한 값 : " + boardDto);
		// BoaedDto [boardSeq=8186, typeSeq=2, memberId=sinbumjun, memberNick=범그로, title=제목 수정1, 
		//           content=제목 수정2, hasFile=, hits=null, createDtm=null, updateDtm=null]
		
		// 제목, 내용, 파일 수정하기
		int update = bService.update(boardDto, mReq);
		System.out.println("수정이 잘 되었는지 확인하기!!! : " + update);
		
		// 수정에 성공? 실패?
		if(update == 1) {
			map.put("result", update); // BoaedDto
			map.put("ph", pageHandlerDto); // PageHandlerDto 
			map.put("nextPage", "/board/list"); // 페이지
			map.put("msg", "게시글 수정에 성공하였습니다");
		}else {
			map.put("update", update); // BoaedDto
			map.put("ph", pageHandlerDto); // PageHandlerDto 
			map.put("nextPage", "/board/list"); // 페이지
			map.put("msg", "게시글 수정에 실패하였습니다");
		}
		return map;
	}
	
	// 파일 삭제하기
	@RequestMapping("/board/deleteAttFile.do")
	@ResponseBody
	public HashMap<String, Object> deleteAttFile(BoardDto boardDto, AttFileDto attFileDto) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		boardDto.setTypeSeq(typeSeq); // 자유게시판2
		System.out.println("게시판 정보 : " + boardDto);
		System.out.println("첨부파일 정보 : " + attFileDto);
		
		return map;
	} 
	
}













