package com.lhs.service;

import java.util.HashMap;
import java.util.List;

import com.lhs.dto.BoardDto;
import com.lhs.dto.ReplyDto;

public interface ReplyService {

	// 1. 댓글 작성
	public int addReply(HashMap<String, Object> add);

	// 2. 댓글 내용 가져오기 (게시판의 모든 댓글)
	public List<ReplyDto> getReplyList(BoardDto boardDto);

	// 3. 해당 게시물의 특정 댓글 삭제하기
	public int deleteReply(ReplyDto replyDto);

	// 4. 해당 게시물의 특정 댓글 수정하기
	public int updateReply(ReplyDto replyDto);

}
