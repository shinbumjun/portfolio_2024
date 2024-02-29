package com.lhs.service.impl;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.lhs.dao.MemberDao;
import com.lhs.dto.EmailDto;
import com.lhs.dto.MemberDto;
import com.lhs.exception.PasswordMissMatchException;
import com.lhs.exception.UserNotFoundException;
import com.lhs.service.MemberService;
import com.lhs.util.EmailUtil;
import com.lhs.util.PasswordUtil;
import com.lhs.util.Sha512Encoder;



@Service
public class MemberServiceImpl implements MemberService {
	
	@Autowired MemberDao mDao; 

	// 이메일을 발송하기 위해서 주입
	@Autowired
	private EmailUtil emailUtil;
	
	@Override
	public ArrayList<HashMap<String, Object>> memberList(HashMap<String, Object> params) {
		return mDao.memberList(params);
	}
	
	//총회원수
	@Override
	public int totalMemberCnt(HashMap<String, Object> params) {
		return mDao.totalMemberCnt(params);
	}

	/* 
 		회원가입 : register.jsp
		memberPw     --->     member_pw
	*/
	@Override
	public int join(MemberDto memberDto) {
		
		// *비밀번호 암호화
		Sha512Encoder encoder = Sha512Encoder.getInstance(); // 1. SHA-512 암호화를 위한 인스턴스를 얻는다
		System.out.println("encoder : " + encoder); // com.feb.test.util.Sha512Encoder@7c3a98fc
		
		String passwd = memberDto.getMemberPw(); // 2. 브라우저에서 입력한 비밀번호
		System.out.println("memberPw : " + memberDto.getMemberPw()); // 123
		
		String encodeTxt = encoder.getSecurePassword(passwd); // 3. 비밀번호 암호화
		System.out.println("memberPw : " + encodeTxt); // 3c9909afec25354d551dae21590...
		
		memberDto.setMemberPw(encodeTxt); // 4. 암호화한 패쓰워드로 저장
		
		return mDao.join(memberDto);
	}

	// 회원가입시 중복 ID확인
	@Override
	public int checkId(MemberDto memberDto) {
		// [memberIdx=null, typeSeq=null, memberId=sinbumjun, memberPw=null, memberName=null, memberNick=null, email=null, createDtm=null, updateDtm=null, membercol=null]
		System.out.println("memberDto : " + memberDto); // 브라우저에 찍힌 값만 저장
		return mDao.checkId(memberDto);
	}

	@Override
	public HashMap<String, Object> login(HashMap<String, String> params) throws UserNotFoundException, PasswordMissMatchException {
		HashMap<String, Object> member = mDao.getMemberById(params);
		return member;
	}

	@Override
	public int delMember(HashMap<String, Object> params) {	
		return mDao.delMember(params);
	}
	
	// 로그인시 ID와 비밀번호가 일치하는지 확인
	@Override
	public boolean Loginchick(MemberDto memberDto) {
		
		// 1. id비교
		String memberId = memberDto.getMemberId(); // 브라우저에서 입력한 ID
		System.out.println("id비교 : " + memberId); // sinbumjun
		// tip : select시에 값이 없으면 null인데 JDBC는 tye-catch문으로 했었는데 mybatis에서는 if문으로 처리 가능하다
		MemberDto Member = mDao.Loginchick(memberId); // 동일한 id가 없을 경우 null, 동일한 id가 있을경우 MemberDto 반환
		System.out.println("동일한 id가 없을 경우 null : " + Member.getMemberId());
		
		if(ObjectUtils.isEmpty(Member.getMemberId())) { // null이면 true 반환
			return false;
		}
		
		// 2. 비밀번호 비교
		String passwd = memberDto.getMemberPw(); // 사용자가 입력한 비밀번호
		System.out.println("사용자가 입력한 비밀번호 : " + passwd); // 123
		
		String memberPw = Member.getMemberPw(); // DB에 저장되어 있는 암호화 된 비밀번호 
		System.out.println("DB에 저장되어 있는 암호화 된 비밀번호  : " + memberPw); // 3c9909afec25354...
		
		Sha512Encoder encoder = Sha512Encoder.getInstance();
		System.out.println("encoder  : " + encoder); // com.lhs.util.Sha...
		
		String encodeTxt = encoder.getSecurePassword(passwd); // 사용자가 입력한 값을 암호화한 거다 
		System.out.println("사용자가 입력한 값을 암호화한 거다  : " + encodeTxt);	 // 3c9909afec25354...
		
		return StringUtils.pathEquals(memberPw, encodeTxt); // 비교해서 틀리면 false반환
	}

	// 회원가입 완료하면 이메일로 환영문자 보내기
	@Override
	public boolean Welcomeemail(String email, HttpServletRequest request) {
		
		MemberDto emailchick = mDao.Welcomeemail(email); // null이면 발송x
		
		if(emailchick != null) { // null이 아니면 true
			EmailDto emailDto = new EmailDto();
			emailDto.setFrom("sinbumjun123@naver.com"); // 이메일을 발신하는 사람의 이메일 주소
			emailDto.setReceiver(email); // 이메일을 수신하는 사람의 이메일 주소, 회원가입한 이메일로 메일 발송
			
			// 이메일 본문에 링크 추가 (*추가한 내용)
	        // String link = "<a href='https://www.notion.so/portfolio_2024-accdde7bd46a43f6b405d7453738f13b?pvs=4'> 신범준의 노션 링크입니다</a>"; // 이메일 본문에 추가할 링크
			String link = "https://www.notion.so/portfolio_2024-accdde7bd46a43f6b405d7453738f13b?pvs=4"; // 이메일 본문에 추가할 링크
			String text = "스프링 프로젝트 :  " + link; // 이메일 본문 내용에 링크 추가
	        emailDto.setText(text); // 이메일의 본문 내용 설정
	        
			emailDto.setSubject("가입을 축하합니다!!!"); // 이메일의 제목
			System.out.println("emailDto 내용 확인 : " + emailDto.toString()); // com.lhs.dto.EmailDto@22ee9ac6
			
			try {
				// 이메일 발송 실패 시 예외처리 
				emailUtil.sendMail(emailDto); // @Autowired 타입으로 주입받고 사용
				// emailUtil.sendHtmlMail(emailDto);
				
				// 어떤 값으로 이루어진 이메일을 발송했는지 확인해보기
				System.out.println("이메일을 발신하는 사람의 이메일 주소 : " + emailDto.getFrom()); // sinbumjun123@naver.com
				System.out.println("이메일을 수신하는 사람의 이메일 주소 : " + emailDto.getReceiver()); // sinbumjun123@naver.com
				System.out.println("이메일의 본문 내용 설정 : " + emailDto.getText()); // <a href='https://www.notion ...
				System.out.println("이메일의 제목 : " + emailDto.getSubject()); // 가입을 축하합니다!!!
			}catch (Exception e){
				request.setAttribute("errorMessage", "이메일 발송에 실패헸습니다");
				System.out.println("이메일 발송 실패 : " + emailUtil.toString());
				return false;
			}
		}	
		return true;
	}

	// 비밀번호 찾기
	@Override
	public String Passwordchick(MemberDto memberDto, Model model, HttpServletRequest request) {
		
		// MemberDto member = mDao.Passwordchick(memberDto);
		HashMap<String, String> member = new HashMap<String,String>(); // DTO를 사용하지 않고 HashMap 형식으로 사용해보고 싶어서 사용
		member.put("memberId", memberDto.getMemberId()); // 
		member.put("email", memberDto.getEmail()); // 
		
		MemberDto PasswordEmailOK = mDao.Passwordchick(member); // null값이면 일치하지 않는다
		
		if(PasswordEmailOK != null) { // 일치하면 6자리 인증번호 발송
			String number = PasswordUtil.generateRandomPassword();
			System.out.println("임의 숫자 6자리 : " + number); // 임의 숫자 6자리
			
			EmailDto emailDto = new EmailDto();
			emailDto.setFrom("sinbumjun123@naver.com"); // 이메일을 발신하는 사람의 이메일 주소
			emailDto.setReceiver(memberDto.getEmail()); // 이메일을 수신하는 사람의 이메일 주소, 회원가입한 이메일로 메일 발송
			System.out.println("비밀번호를 찾는 사람의 이메일 주소 : " + memberDto.getEmail());
			emailDto.setSubject("비밀번호 찾기를 위한 인증번호입니다"); // 이메일의 제목
			emailDto.setText("인증번호 : " + number); // 이메일의 본문 내용 설정
			
			try {
				emailUtil.sendMail(emailDto); // 이메일 발송

				// ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
				// *세션에 암호화된 인증번호 저장
	            HttpSession session = request.getSession();
				// *비밀번호 암호화
				//Sha512Encoder encoder = Sha512Encoder.getInstance(); // SHA-512 암호화를 위한 인스턴스를 얻는다
				
				//String encodeTxt = encoder.getSecurePassword(number); // 비밀번호 암호화
	            //System.out.println("인증번호 암호화 한것 : " + encodeTxt); // 3fbc8bfd4be1413f
	            
	            session.setAttribute("number", number); // 인증번호 암호화 한 것 전달, password.jsp에 값 찍어보기
	            model.addAttribute("number", number);
				// setAttribute("number", number); // 인증번호를 request에 담아서 보내본다
				// ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
	            
				//return true; // 발송한 인증번호는 세션에 저장하는게 맞는거 같다
				return "success"; // 인증번호 발송 성공
				
			}catch (Exception e){
				model.addAttribute("errorMessage", "인증번호 발송 실패헸습니다");
				System.out.println("인증번호 발송 실패헸습니다 : " + emailUtil.toString());
				
				//return false;
				return "error"; // 인증번호 발송 실패
			}
		}else {
			// 사용자가 존재하지 않는 경우
			model.addAttribute("errorMessage", "입력한 아이디 또는 이메일이 존재하지 않습니다.");
	        
	        // return false; // 사용자가 존재하지 않음
	        return "error"; // 사용자가 존재하지 않음
		}
	}

	// 비밀번호 변경
	@Override
	public int pwchange(MemberDto memberDto) {
		
		// *비밀번호 암호화
		Sha512Encoder encoder = Sha512Encoder.getInstance(); // 1. SHA-512 암호화를 위한 인스턴스를 얻는다
		System.out.println("encoder : " + encoder); 
		
		String passwd = memberDto.getMemberPw(); // 2. 브라우저에서 입력한 비밀번호
		System.out.println("memberPw : " + memberDto.getMemberPw()); 
		
		String encodeTxt = encoder.getSecurePassword(passwd); // 3. 비밀번호 암호화
		System.out.println("memberPw : " + encodeTxt); 
		
		memberDto.setMemberPw(encodeTxt); // 4. 암호화한 패쓰워드로 저장
		
		return mDao.pwchange(memberDto); 
	}
}



