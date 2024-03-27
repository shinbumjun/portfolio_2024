package com.lhs.dao;

import com.lhs.dto.EmailAuthDto;
import com.lhs.dto.MemberDto;

public interface EmailAuthDao {

	// 1. 이메일 인증 정보 insert
	public int recordEmailAuth(EmailAuthDto emailAuthDto);

}
