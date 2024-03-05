package com.lhs.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.lhs.dto.BoardDto;
import com.lhs.service.AttFileService;
import com.lhs.service.BoardService;
import com.lhs.util.FileUtil;

@Controller
public class NoticeRestController {

	@Autowired BoardService bService;
	@Autowired AttFileService attFileService;
	@Autowired FileUtil fileUtil;
	
	private String typeSeq = "1";
	
	@RequestMapping("/notice/list.do")
	public ModelAndView goLogin(BoardDto boardDto){
		ModelAndView mv = new ModelAndView();
		
		boardDto.setTypeSeq(typeSeq); // 공지사항1
		
		mv.setViewName("notice/list");
		return mv;
	}
	
	//글쓰기 페이지로 	
	@RequestMapping("/notice/goToWrite.do")
	public ModelAndView goToWrite(BoardDto boardDto) {
		ModelAndView mv = new ModelAndView();
		
		boardDto.setTypeSeq(typeSeq); // 공지사항1
		
		mv.setViewName("/notice/write");
		return mv;
	}
}
