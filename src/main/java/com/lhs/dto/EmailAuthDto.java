package com.lhs.dto;

public class EmailAuthDto {
	
	private Integer authIdx; // PK
	private Integer memberIdx; // 회원정보 식별하는 외래 키
	private Integer memberTypeSeq; // 회원의 유형 외래키
	private String memberId; // 아이디
	private String email; // 이메일
	private String link; // 이메일 인증 링크
	private String expireDtm; // 이메일 인증 링크의 유효 기간이 종료되는 일시 (1분)
	private String sendDtm; // 이메일 인증 링크를 전송한 일시

	public EmailAuthDto() {}

	public Integer getAuthIdx() {
		return authIdx;
	}

	public void setAuthIdx(Integer authIdx) {
		this.authIdx = authIdx;
	}

	public Integer getMemberIdx() {
		return memberIdx;
	}

	public void setMemberIdx(Integer memberIdx) {
		this.memberIdx = memberIdx;
	}

	public Integer getMemberTypeSeq() {
		return memberTypeSeq;
	}

	public void setMemberTypeSeq(Integer memberTypeSeq) {
		this.memberTypeSeq = memberTypeSeq;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getExpireDtm() {
		return expireDtm;
	}

	public void setExpireDtm(String expireDtm) {
		this.expireDtm = expireDtm;
	}

	public String getSendDtm() {
		return sendDtm;
	}

	public void setSendDtm(String sendDtm) {
		this.sendDtm = sendDtm;
	}

	@Override
	public String toString() {
		return "EmailAuthDto [authIdx=" + authIdx + ", memberIdx=" + memberIdx + ", memberTypeSeq=" + memberTypeSeq
				+ ", memberId=" + memberId + ", email=" + email + ", link=" + link + ", expireDtm=" + expireDtm
				+ ", sendDtm=" + sendDtm + "]";
	}

}
