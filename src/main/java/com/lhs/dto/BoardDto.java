package com.lhs.dto;

public class BoardDto {
	
	private Integer boardSeq; // 번호
	private String typeSeq; // 카테고리 : 1공지사항, 2자유게시판
	private String memberId; // 글쓴이
	private String memberNick; // 별명
	private String title; // 제목
	private String content; // 내용
	private String hasFile; // 첨부파일
	private String hits; // 조회수
	private String createDtm; // 작성일
	private String updateDtm; // 수정일
	
	public BoardDto() {} // 기본 생성자

	public Integer getBoardSeq() {
		return boardSeq;
	}

	public void setBoardSeq(Integer boardSeq) {
		this.boardSeq = boardSeq;
	}

	public String getTypeSeq() {
		return typeSeq;
	}

	public void setTypeSeq(String typeSeq) {
		this.typeSeq = typeSeq;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getMemberNick() {
		return memberNick;
	}

	public void setMemberNick(String memberNick) {
		this.memberNick = memberNick;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getHasFile() {
		return hasFile;
	}

	public void setHasFile(String hasFile) {
		this.hasFile = hasFile;
	}

	public String getHits() {
		return hits;
	}

	public void setHits(String hits) {
		this.hits = hits;
	}

	public String getCreateDtm() {
		return createDtm;
	}

	public void setCreateDtm(String createDtm) {
		this.createDtm = createDtm;
	}

	public String getUpdateDtm() {
		return updateDtm;
	}

	public void setUpdateDtm(String updateDtm) {
		this.updateDtm = updateDtm;
	}

	@Override
	public String toString() {
		return "BoaedDto [boardSeq=" + boardSeq + ", typeSeq=" + typeSeq + ", memberId=" + memberId + ", memberNick="
				+ memberNick + ", title=" + title + ", content=" + content + ", hasFile=" + hasFile + ", hits=" + hits
				+ ", createDtm=" + createDtm + ", updateDtm=" + updateDtm + "]";
	}
	
}
