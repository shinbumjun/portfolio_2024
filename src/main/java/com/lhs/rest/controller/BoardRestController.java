package com.lhs.rest.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.lhs.dto.AttFileDto;
import com.lhs.dto.BoardDto;
import com.lhs.dto.SearchCondition;
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
	  	3.  업로드
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
	 	게시글 삭제
	 	boardSeq           board_seq
	 	typeSeq            type_seq
	 	option
	 	keyword
	*/
	@PostMapping("/board/delete.do")
	@ResponseBody
	public HashMap<String, Object> delete(BoardDto boardDto, SearchCondition sc, HttpSession session) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		// ModelAndView mv = new ModelAndView(); 
	
		boardDto.setTypeSeq(typeSeq); // 자유게시판2
		System.out.println("boardDto : " + boardDto);
		// BoaedDto [boardSeq=10764, typeSeq=2, memberId=null, memberNick=null, title=null, 
		//           content=null, hasFile=null, hits=null, createDtm=null, updateDtm=null]
		
		System.out.println("pageHandlerDto : " + sc);
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
	 	게시글 수정
	 	boardSeq
	 	typeSeq
	 	
	 	title           title
	 	content         content
	 	attFiles
	*/
	@RequestMapping("/board/update.do")
	@ResponseBody // !!!!!!!!!!!! 비동기 응답 
	public HashMap<String, Object> update(BoardDto boardDto, SearchCondition sc, MultipartHttpServletRequest mReq) {
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
			map.put("ph", sc); // PageHandlerDto 
			map.put("nextPage", "/board/list"); // 페이지
			map.put("msg", "게시글 수정에 성공하였습니다");
		}else {
			map.put("update", update); // BoaedDto
			map.put("ph", sc); // PageHandlerDto 
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


