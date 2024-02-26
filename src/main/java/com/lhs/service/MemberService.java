package com.lhs.service;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.lhs.exception.PasswordMissMatchException;
import com.lhs.exception.UserNotFoundException;

public interface MemberService {

	public ArrayList<HashMap<String, Object>> memberList(HashMap<String, Object> params);
	
	//총 회원수 for paging
	public int totalMemberCnt(HashMap<String, Object> params);

	// 회원가입
	public int join(HashMap<String, String> params);
	
	// 회원가입시 중복 ID확인
	public int checkId(HashMap<String, String> params);
	
	public HashMap<String, Object> login(HashMap<String, String> params) throws UserNotFoundException, PasswordMissMatchException;

	public int delMember(HashMap<String,Object> params);

	// 로그인시 ID와 비밀번호가 일치하는지 확인
	public boolean Loginchick(HashMap<String, String> params);
}
