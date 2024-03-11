package com.lhs.dto;

public class AttFileDto {

	private Integer fileIdx; // pk
	private Integer typeSeq; // 외래키
	private Integer boardSeq; // 외래키
	private String fileName; // 파일 이름 고유해야함
	private String fakeFilename; // 파일 이름 -> 페이크 이름
	private String fileSize; // 파일 사이즈
	private String fileType; // 파일 타입
	private String saveLoc; // 물리적 저장 위치
	private String createDtm; // 생성 날짜
	
	public AttFileDto() {}

	public Integer getFileIdx() {
		return fileIdx;
	}

	public void setFileIdx(Integer fileIdx) {
		this.fileIdx = fileIdx;
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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFakeFilename() {
		return fakeFilename;
	}

	public void setFakeFilename(String fakeFilename) {
		this.fakeFilename = fakeFilename;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getSaveLoc() {
		return saveLoc;
	}

	public void setSaveLoc(String saveLoc) {
		this.saveLoc = saveLoc;
	}

	public String getCreateDtm() {
		return createDtm;
	}

	public void setCreateDtm(String createDtm) {
		this.createDtm = createDtm;
	}

	@Override
	public String toString() {
		return "BoardAttachDto [fileIdx=" + fileIdx + ", typeSeq=" + typeSeq + ", boardSeq=" + boardSeq + ", fileName="
				+ fileName + ", fakeFilename=" + fakeFilename + ", fileSize=" + fileSize + ", fileType=" + fileType
				+ ", saveLoc=" + saveLoc + ", createDtm=" + createDtm + "]";
	}
	
}
