package com.lhs.dao;

import java.util.ArrayList;
import java.util.HashMap;

import com.lhs.dto.MemberDto;

public interface MemberDao {
	
	public ArrayList<HashMap<String, Object>> memberList(HashMap<String, Object> params);

	public int totalMemberCnt(HashMap<String, Object> params);

	// 회원가입
	public int join(MemberDto memberDto);
	
	// 회원가입시 중복 ID확인
	public int checkId(MemberDto memberDto); // String memberId로 하면 에러가 남
	
	public HashMap<String, Object> getMemberById(HashMap<String, String> params);
	
	public String makeCipherText(HashMap<String, String> params);
	
	public int delMember(HashMap<String,Object> params);

	// 로그인시 ID와 비밀번호가 일치하는지 확인
	public MemberDto Loginchick(String memberId);

	// 회원가입 완료하면 이메일로 환영문자 보내기
	public MemberDto Welcomeemail(MemberDto memberDto);

	// 비밀번호 찾기
	public MemberDto Passwordchick(HashMap<String, String> member);

	// 비밀번호 변경
	public int pwchange(MemberDto memberDto);

	// 별명가져오기
	public MemberDto Nick(MemberDto memberDto);

	// 3. 각 댓글의 작성자 이름 가져오기
	public String getReplyName(int memberIdx);
}



