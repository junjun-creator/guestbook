package kr.or.connect.guestbook.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.or.connect.guestbook.dao.GuestbookDao;
import kr.or.connect.guestbook.dao.LogDao;
import kr.or.connect.guestbook.dto.Guestbook;
import kr.or.connect.guestbook.dto.Log;
import kr.or.connect.guestbook.service.GuestbookService;

@Service
public class GuestServiceimpl implements GuestbookService{
	@Autowired // bean을 자동으로 등록
	GuestbookDao guestbookDao;
	
	@Autowired // bean을 자동으로 등록
	LogDao logDao;

	@Override
	@Transactional // 읽어오기만 하는것이므로 이것 선언. 내부적으로 read only로 실행
	public List<Guestbook> getGuestbooks(Integer start) {
		List<Guestbook> list = guestbookDao.selectAll(start, GuestbookService.LIMIT);
		return list;
	}

	@Override
	@Transactional(readOnly=false)
	public int deleteGuestbook(Long id, String ip) {
		int deleteCount = guestbookDao.deleteById(id);
		Log log = new Log(); // 삭제를 수행한 후 로그를 남기기 위해 로그 객체 생성
		log.setIp(ip);
		log.setMethod("delete");
		log.setRegdate(new Date()); // 값 저장 후에
		logDao.insert(log); // 로그Dao insert 메소드로 저장
		return deleteCount;
	}

	@Override
	@Transactional(readOnly=false)
	public Guestbook addGuestbook(Guestbook guestbook, String ip) {
		guestbook.setRegdate(new Date());//클라이언트로부터 받아온 데이터에는 regdate에 정보가 없으므로 따로 넣어줌
		Long id = guestbookDao.insert(guestbook);
		guestbook.setId(id); // 삽입하면서 생성된 id값을 guestbook 정보에 입력함
		Log log = new Log(); // 삽입을 수행한 후 로그를 남기기 위해 로그 객체 생성
		log.setIp(ip);
		log.setMethod("insert");
		log.setRegdate(new Date()); // 값 저장 후에
		logDao.insert(log);
		return guestbook;
	}

	@Override
	public int getCount() {
		return guestbookDao.selectCount();
	}
	
	
}
