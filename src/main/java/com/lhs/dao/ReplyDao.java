package com.lhs.dao;

import java.util.HashMap;
import java.util.List;

import com.lhs.dto.BoardDto;
import com.lhs.dto.ReplyDto;

public interface ReplyDao {
	
	// 1. 댓글 작성
	public int addReply(HashMap<String, Object> add);

	// 2. 댓글 내용 가져오기 (게시판의 모든 댓글)
	public List<ReplyDto> getReplyList(BoardDto boardDto);

}
