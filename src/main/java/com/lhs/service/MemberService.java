package com.lhs.service;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.lhs.dto.MemberDto;
import com.lhs.exception.PasswordMissMatchException;
import com.lhs.exception.UserNotFoundException;

public interface MemberService {

	public ArrayList<HashMap<String, Object>> memberList(HashMap<String, Object> params);
	
	//총 회원수 for paging
	public int totalMemberCnt(HashMap<String, Object> params);

	// 회원가입
	public int join(MemberDto memberDto);
	
	// 회원가입시 중복 ID확인 (수정) 
	public int checkId(MemberDto memberDto);
	
	public HashMap<String, Object> login(HashMap<String, String> params) throws UserNotFoundException, PasswordMissMatchException;

	public int delMember(HashMap<String,Object> params);

	// 로그인시 ID와 비밀번호가 일치하는지 확인
	public boolean Loginchick(MemberDto memberDto);

	// 회원가입 완료하면 이메일로 환영문자 보내기
	public boolean Welcomeemail(String email, HttpServletRequest request);

	// 비밀번호 찾기
	public boolean Passwordchick(MemberDto memberDto);
}



