package com.lhs.rest.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lhs.dto.BoardDto;
import com.lhs.dto.MemberDto;
import com.lhs.dto.ReplyDto;
import com.lhs.service.AttFileService;
import com.lhs.service.BoardService;
import com.lhs.service.MemberService;
import com.lhs.service.ReplyService;
import com.lhs.util.FileUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ReplyRestController {
	
	@Autowired
    private ReplyService replyService;
	
	@Autowired
	MemberService mService;
	
	private String typeSeq = "2";
	
	@PostMapping("/reply/add.do")
	public HashMap<String, Object> addReply(
					@RequestParam("replyContent") String replyContent,
					@RequestParam("memberId") String memberId,
					@RequestParam("boardSeq") Integer boardSeq,
					BoardDto boardDto){
		HashMap<String, Object> map = new HashMap<String, Object>();
		HashMap<String, Object> add = new HashMap<String, Object>();
		
		add.put("replyContent", replyContent);
		add.put("memberId", memberId);
		add.put("boardSeq", boardSeq);
		add.put("typeSeq", typeSeq);
		
		System.out.println("replyContent정보 : " + replyContent); // 안녕하세요
		System.out.println("memberId정보 : " + memberId); // bum.1005
		System.out.println("boardSeq정보 : " + boardSeq); // 10784
		
		// 1. 댓글 작성
		int result = replyService.addReply(add);
		System.out.println("댓글 작성 1성공 0실패 : " + result); // 1
		
		// 2. 댓글 내용 가져오기 (게시판의 모든 댓글)
		boardDto.setTypeSeq(typeSeq);
		System.out.println("boardDto정보 : " + boardDto);
		// BoaedDto [boardSeq=10784, typeSeq=2, ...
		List<ReplyDto> ReplyList = replyService.getReplyList(boardDto);
		System.out.println("게시판의 모든 댓글 : " + ReplyList);
		// ReplyDto [replySeq=7, typeSeq=2, boardSeq=10784, memberIdx=40, memberType=1, replyContent=자유게시글+게시글 ...
		
		// 3. 각 댓글의 작성자 이름 가져오기
		List<String> ReplyNames = new ArrayList<>(); // 댓글 작성자 이름을 담을 리스트 생성
		for (ReplyDto reply : ReplyList) {
		    // 댓글의 작성자 식별자(memberIdx) 가져오기
		    int memberIdx = reply.getMemberIdx();
		    // 댓글의 작성자 식별자를 이용하여 작성자 이름 가져오기
		    String ReplyName = mService.getReplyName(memberIdx);
		    // 작성자 이름을 리스트에 추가
		    ReplyNames.add(ReplyName);
		}
		System.out.println("게시판의 댓글 작성자들 : " + ReplyNames);
		
		/*
		 	여기서 출력해줄 필요가 없는것같다 
		 	이유 : 댓글을 추가할 때 출력하는게 아니라 해당 게시물을 보여줄 때 댓글이 나와야 하기 때문에
		 */
		// map.put("ReplyList", ReplyList); // 댓글
		// map.put("ReplyNames", ReplyNames); // 댓글 작성자
		if(result == 1) {
			map.put("msg", "댓글이 작성 되었습니다");
		}else {
			map.put("msg", "댓글이 실패 되었습니다");
		}
		
		return map;
	}
}








