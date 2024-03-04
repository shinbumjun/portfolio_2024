package com.lhs.controller;

import java.io.IOException;
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

import com.lhs.dto.BoardDto;
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

	@RequestMapping("/board/list.do")
	public ModelAndView goLogin(BoardDto boardDto){
		ModelAndView mv = new ModelAndView();
		
		// 1. 모든 리스트
		boardDto.setTypeSeq(typeSeq); // 자유게시판2
		List<BoardDto> boardlist = bService.list(boardDto);
		System.out.println("boardlist : " + boardlist);
		// [BoaedDto [boardSeq=1, typeSeq=null, memberId=, memberNick=null, title=123, content=null, 
		// hasFile=null, hits=0, createDtm=20240303155648, updateDtm=null], ... 
		
		mv.addObject("boardlist", boardlist); // 리스트 출력
		mv.addObject("msg", "자유게시판");
		mv.addObject("nextPage", "/board/list");
		
		System.out.println("boardlist : " + boardlist);
		return mv;
	}

	@RequestMapping("/test.do")
	public ModelAndView test() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("test");
		return mv;
	}

	
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
		
		return map;
	}

	@RequestMapping("/board/read.do")
	public ModelAndView read(@RequestParam HashMap<String, Object> params) {
		if(!params.containsKey("typeSeq")) {
			params.put("typeSeq", this.typeSeq);
		}
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/board/read");
		return mv;
	}	


	//수정  페이지로 	
	@RequestMapping("/board/goToUpdate.do")
	public ModelAndView goToUpdate(@RequestParam HashMap<String, Object> params, HttpSession session) {
		ModelAndView mv = new ModelAndView();

		if(!params.containsKey("typeSeq")) {
			params.put("typeSeq", this.typeSeq);
		}
		
		return mv;

	}

	@RequestMapping("/board/update.do")
	@ResponseBody // !!!!!!!!!!!! 비동기 응답 
	public HashMap<String, Object> update(@RequestParam HashMap<String,Object> params, 
			MultipartHttpServletRequest mReq) {

		if(!params.containsKey("typeSeq")) {
			params.put("typeSeq", this.typeSeq);
		}

		return null;
	}

	@RequestMapping("/board/delete.do")
	@ResponseBody
	public HashMap<String, Object> delete(@RequestParam HashMap<String, Object> params, HttpSession session) {

		if(!params.containsKey("typeSeq")) {
			params.put("typeSeq", this.typeSeq);
		}
		return null; // 비동기: map return 
	}

	@RequestMapping("/board/deleteAttFile.do")
	@ResponseBody
	public HashMap<String, Object> deleteAttFile(@RequestParam HashMap<String, Object> params) {

		if(!params.containsKey("typeSeq")) {
			params.put("typeSeq", this.typeSeq);
		}
		return null;
	} 

	

}
