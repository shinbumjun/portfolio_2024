package com.lhs.dto;

public class ReplyDto {
	// reply 테이블
	private Integer replySeq; // 댓글 번호
	
	// board 테이블
	private Integer typeSeq; // 자유게시판 2 FK
	private Integer boardSeq; // 해당 게시판 번호 FK
	
	// member 테이블
	private Integer memberIdx; // 작성자 번호 FK
	private Integer memberType; // 작성자 타입 FK

	private String replyContent; // 댓글 내용
	private Integer pcno; // 대댓글
	
	private String createDtm; // 작성일
	private String upDate; // 수정일
	
	public ReplyDto() {} // 기본 생성자

	public ReplyDto(Integer typeSeq, Integer boardSeq, Integer memberIdx, Integer memberType, String replyContent,
			Integer pcno) {
		super();
		this.typeSeq = typeSeq;
		this.boardSeq = boardSeq;
		this.memberIdx = memberIdx;
		this.memberType = memberType;
		this.replyContent = replyContent;
		this.pcno = pcno;
	}
	
	public Integer getReplySeq() {
		return replySeq;
	}

	public void setReplySeq(Integer replySeq) {
		this.replySeq = replySeq;
	}

	public Integer getTypeSeq() {
		return typeSeq;
	}

	public void setTypeSeq(Integer typeSeq) {
		this.typeSeq = typeSeq;
	}

	public Integer getBoardSeq() {
		return boardSeq;
	}

	public void setBoardSeq(Integer boardSeq) {
		this.boardSeq = boardSeq;
	}

	public Integer getMemberIdx() {
		return memberIdx;
	}

	public void setMemberIdx(Integer memberIdx) {
		this.memberIdx = memberIdx;
	}

	public Integer getMemberType() {
		return memberType;
	}

	public void setMemberType(Integer memberType) {
		this.memberType = memberType;
	}

	public String getReplyContent() {
		return replyContent;
	}

	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}

	public Integer getPcno() {
		return pcno;
	}

	public void setPcno(Integer pcno) {
		this.pcno = pcno;
	}

	public String getCreateDtm() {
		return createDtm;
	}

	public void setCreateDtm(String createDtm) {
		this.createDtm = createDtm;
	}

	public String getUpDate() {
		return upDate;
	}

	public void setUpDate(String upDate) {
		this.upDate = upDate;
	}

	@Override
	public String toString() {
		return "ReplyDto [replySeq=" + replySeq + ", typeSeq=" + typeSeq + ", boardSeq=" + boardSeq + ", memberIdx="
				+ memberIdx + ", memberType=" + memberType + ", replyContent=" + replyContent + ", pcno=" + pcno
				+ ", createDtm=" + createDtm + ", upDate=" + upDate + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((boardSeq == null) ? 0 : boardSeq.hashCode());
		result = prime * result + ((memberIdx == null) ? 0 : memberIdx.hashCode());
		result = prime * result + ((memberType == null) ? 0 : memberType.hashCode());
		result = prime * result + ((pcno == null) ? 0 : pcno.hashCode());
		result = prime * result + ((replyContent == null) ? 0 : replyContent.hashCode());
		result = prime * result + ((replySeq == null) ? 0 : replySeq.hashCode());
		result = prime * result + ((typeSeq == null) ? 0 : typeSeq.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReplyDto other = (ReplyDto) obj;
		if (boardSeq == null) {
			if (other.boardSeq != null)
				return false;
		} else if (!boardSeq.equals(other.boardSeq))
			return false;
		if (memberIdx == null) {
			if (other.memberIdx != null)
				return false;
		} else if (!memberIdx.equals(other.memberIdx))
			return false;
		if (memberType == null) {
			if (other.memberType != null)
				return false;
		} else if (!memberType.equals(other.memberType))
			return false;
		if (pcno == null) {
			if (other.pcno != null)
				return false;
		} else if (!pcno.equals(other.pcno))
			return false;
		if (replyContent == null) {
			if (other.replyContent != null)
				return false;
		} else if (!replyContent.equals(other.replyContent))
			return false;
		if (replySeq == null) {
			if (other.replySeq != null)
				return false;
		} else if (!replySeq.equals(other.replySeq))
			return false;
		if (typeSeq == null) {
			if (other.typeSeq != null)
				return false;
		} else if (!typeSeq.equals(other.typeSeq))
			return false;
		return true;
	}

}


