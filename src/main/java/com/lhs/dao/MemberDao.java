package com.lhs.dao;

import java.util.ArrayList;
import java.util.HashMap;

import com.lhs.dto.MemberDto;

public interface MemberDao {
	
	public ArrayList<HashMap<String, Object>> memberList(HashMap<String, Object> params);

	public int totalMemberCnt(HashMap<String, Object> params);

	// 회원가입
	public int join(HashMap<String, String> params);
	
	// 회원가입시 중복 ID확인
	public int checkId(HashMap<String, String> params);
	
	public HashMap<String, Object> getMemberById(HashMap<String, String> params);
	
	public String makeCipherText(HashMap<String, String> params);
	
	public int delMember(HashMap<String,Object> params);

	// 로그인시 ID와 비밀번호가 일치하는지 확인
	public MemberDto Loginchick(HashMap<String, String> params);
	
}
