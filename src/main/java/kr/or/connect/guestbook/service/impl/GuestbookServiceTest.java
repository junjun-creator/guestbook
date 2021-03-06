package kr.or.connect.guestbook.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import kr.or.connect.guestbook.config.ApplicationConfig;
import kr.or.connect.guestbook.dto.Guestbook;
import kr.or.connect.guestbook.service.GuestbookService;

public class GuestbookServiceTest {

	public static void main(String[] args) {
		ApplicationContext ac = new AnnotationConfigApplicationContext(ApplicationConfig.class);
		GuestbookService guestbookService = ac.getBean(GuestbookService.class);
		
//		Guestbook guestbook = new Guestbook();
//		guestbook.setName("kang kyungmi22");
//		guestbook.setContent("반갑습니다. 여러분. 여러분이 재미있게 공부하고 계셨음 정말 좋겠네요^^22");
//		guestbook.setRegdate(new Date());
//		Guestbook result = guestbookService.addGuestbook(guestbook, "127.0.0.1");
//		System.out.println(result);
		
		List<Guestbook> list = guestbookService.getGuestbooks(0);
		for(Guestbook k : list) {
			System.out.println(k);
		}
		
	}

}
