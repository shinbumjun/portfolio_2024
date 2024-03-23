package com.lhs.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lhs.dao.BoardDao;
import com.lhs.dao.MemberDao;
import com.lhs.dao.ReplyDao;
import com.lhs.dto.BoardDto;
import com.lhs.dto.MemberDto;
import com.lhs.dto.ReplyDto;
import com.lhs.service.MemberService;
import com.lhs.service.ReplyService;

@Service
public class ReplyServiceImpl implements ReplyService {

	@Autowired ReplyDao replyDao;
	@Autowired MemberDao mDao; 

	/*
	  	1댓글 작성
	  	받은 내용 : replyContent, boardSeq, memberId, typeSeq 
	  	
	  	필요한 값 : type_seq(o), board_seq(o), reply_content(o), member_idx(o), create_dtm(o), member_type(o)
	  	
	 */
	@Override
	public int addReply(HashMap<String, Object> add) {
		
		System.out.println("add정보 : " + add); 
		// {typeSeq=2, replyContent=안녕하세요, boardSeq=10784, memberId=bum.1005}
		
		// Loginchick 메서드를 이용해서 맴버 정보 가져오기
		String memberId = (String)add.get("memberId");
		System.out.println("댓글 작성자 : " + memberId);
		MemberDto member = mDao.Loginchick(memberId);
		System.out.println("댓글 작성자의 정보 : " + member);
		// MemberDto [memberIdx=40, typeSeq=1, memberId=bum.1005, memberPw=4242cd3...
		
		add.put("memberIdx", member.getMemberIdx());
		add.put("memberType", member.getTypeSeq());
		
		System.out.println("댓글 작성에 필요한 정보 : " + add);
		// {typeSeq=2, memberIdx=40, replyContent=첫 댓글 작성, boardSeq=10784, memberType=1, memberId=bum.1005}
		
		return replyDao.addReply(add);
	}

	// 2. 댓글 내용 가져오기 (게시판의 모든 댓글)
	@Override
	public List<ReplyDto> getReplyList(BoardDto boardDto) {
		System.out.println("게시물의 댓글 가져오기 위한 정보 : " + boardDto);
		return replyDao.getReplyList(boardDto);
	}

	// 3. 해당 게시물의 특정 댓글 삭제하기
	@Override
	public int deleteReply(ReplyDto replyDto) {
		
		return replyDao.deleteReply(replyDto);
	}

	// 4. 해당 게시물의 특정 댓글 수정하기
	@Override
	public int updateReply(ReplyDto replyDto) {

		return replyDao.updateReply(replyDto);
	}
	
}





