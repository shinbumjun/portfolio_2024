package com.lhs.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lhs.dao.AttFileDao;
import com.lhs.dao.BoardDao;
import com.lhs.service.BoardService;
import com.lhs.util.FileUtil;

@Service
public class BoardServiceImpl implements BoardService{

	@Autowired BoardDao bDao;
	@Autowired AttFileDao attFileDao;
	@Autowired FileUtil fileUtil;
	
	@Value("#{config['project.file.upload.location']}") // 환경 변수에 있는 값을 저장해주는것
	private String saveLocation;
	
	@Override
	public ArrayList<HashMap<String, Object>> list(HashMap<String, String> params) {
		return bDao.list(params);
	}

	@Override
	public int getTotalArticleCnt(HashMap<String, String> params) {
		return bDao.getTotalArticleCnt(params);
	}

	// 자유 게시판 업로드
	@Override
	public int write(HashMap<String, Object> params, List<MultipartFile> mFiles) {	
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		// {memberNick=, is_ajax=true, memberIdx=, action=contact_send, title=신범준 제목, content=신범준 내용, memberId=sinbumjun, typeSeq=2, boardSeq=9}
		int count = bDao.write(params); 
		System.out.println("params : " + params.get("boardSeq")); // 12
		System.out.println(count); // 1
		
		//1. board DB에 글 정보등록 + hasFile 
		int write = 0;
		//return write;
		
//		MultipartHttpServletRequest)
//		application/pdf
//		오라클정리.pdf
//		attFiles
//		2650256
//		application/octet-stream
//
//		attFiles
//		0
		for(MultipartFile mFile : mFiles) {
			// fakename 만들기
			// to-do : smart_123.pdf -> (UUID).pdf
			// to-do : smart_123.456.pds -> (UUID).pdf
			// 
			String fakename = UUID.randomUUID().toString().replace("-", ""); // 이렇게 쓰면 확장자 날라감
			System.out.println("fakename : " + fakename); // 053360b594904fcbb1e6b1c5f93c5009
			
			try {
				fileUtil.copyFile(mFile, fakename); // 서버에 파일이 물리적으로 복사가 되어야 한다
				
				System.out.println("mFile : " + mFile);
				// MultipartFile[field="attFiles", filename=chrome.exe, contentType=application/x-msdownload, size=2754848]
				System.out.println(mFile.getContentType());
				System.out.println(mFile.getOriginalFilename());
				System.out.println(mFile.getName());
				System.out.println(mFile.getSize());
				
				map.put("type", mFile.getContentType());
				map.put("filename", mFile.getOriginalFilename());
				// map.put("Name", mFile.getName()); //
				map.put("size", mFile.getSize());
				map.put("fakename", fakename);
				map.put("boardSeq", params.get("boardSeq"));
				map.put("typeSeq", params.get("typeSeq"));
				map.put("saveLocation", saveLocation);
				System.out.println("map : "+ map);
				
				int count2 = attFileDao.addAttFile(map); // 예외가 나올경우
				System.out.println("count2 : " + count2);
				
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		
		
		System.out.println("params : " + params);
		return 0;
	}

	

	//글 조회 
	@Override
	public HashMap<String, Object> read(HashMap<String, Object> params) {
		return bDao.read(params);
	}

	@Override
	public int update(HashMap<String, Object> params, List<MultipartFile> mFiles) {
		if(params.get("hasFile").equals("Y")) { // 첨부파일 존재시 			
			// 파일 처리
		}	
		// 글 수정 dao 
		return bDao.update(params);
	}

	@Override
	public int delete(HashMap<String, Object> params) {
		if(params.get("hasFile").equals("Y")) { // 첨부파일 있으면 		
			 // 파일 처리
		}
		return bDao.delete(params);
	}

	@Override
	public boolean deleteAttFile(HashMap<String, Object> params) {
		boolean result = false;		
		return result;
	}

}





