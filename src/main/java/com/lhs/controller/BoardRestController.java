package com.lhs.controller;

import java.util.HashMap;

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
public class BoardRestController {

	@Autowired BoardService bService;
	@Autowired AttFileService attFileService;
	@Autowired FileUtil fileUtil;
	
	private String typeSeq = "2";
	
	@RequestMapping("/test.do")
	public ModelAndView test() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("test");
		return mv;
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
	
	// 수정  페이지로 	
	@RequestMapping("/board/goToUpdate.do")
	public ModelAndView goToUpdate(BoardDto boardDto, HttpSession session) {
		ModelAndView mv = new ModelAndView();

		boardDto.setTypeSeq(typeSeq); // 자유게시판2
		
		return mv;
	}
	
	// 수정하기
	@RequestMapping("/board/update.do")
	@ResponseBody // !!!!!!!!!!!! 비동기 응답 
	public HashMap<String, Object> update(BoardDto boardDto, 
			MultipartHttpServletRequest mReq) {

		boardDto.setTypeSeq(typeSeq); // 자유게시판2
		
		return null;
	}

	// 파일 삭제하기
	@RequestMapping("/board/deleteAttFile.do")
	@ResponseBody
	public HashMap<String, Object> deleteAttFile(BoardDto boardDto) {

		boardDto.setTypeSeq(typeSeq); // 자유게시판2
		
		return null;
	} 
}



