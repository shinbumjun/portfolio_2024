package com.lhs.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lhs.dao.AttFileDao;
import com.lhs.dto.AttFileDto;
import com.lhs.dto.BoardDto;
import com.lhs.service.AttFileService;

@Service
public class AttFileServiceImpl implements AttFileService{

	@Autowired AttFileDao attFileDao;

	// 1. 첨부파일 읽기1 : 해당게시글(typeSeq, board_seq)의 모든첨부파일(다중이니까)
	@Override
	public List<AttFileDto> readAttFiles(BoardDto read) {
		System.out.println("해당게시물의 모든 첨부파일 : " + read); // boardSeq=10776, typeSeq=2
		return attFileDao.readAttFiles(read);
	}

	// 2. 첨부파일  다운로드2 : pk를 통해 해당 첨부파일 불러오기
	@Override
	public HashMap<String, Object> readAttFileByPk(int fileIdx) {
		return attFileDao.readAttFileByPk(fileIdx);
	}

	@Override
	public int updateLinkedInfo() {
		// TODO Auto-generated method stub
		return 0;
	}

}
