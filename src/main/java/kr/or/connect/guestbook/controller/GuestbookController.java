package kr.or.connect.guestbook.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.or.connect.guestbook.argumentresolver.HeaderInfo;
import kr.or.connect.guestbook.dto.Guestbook;
import kr.or.connect.guestbook.service.GuestbookService;

@Controller
public class GuestbookController {
	@Autowired //서비스를 사용할 것이므로 선언
	GuestbookService guestbookService;
	
	@GetMapping(path="/list")
	public String list(@RequestParam(name="start", required=false, defaultValue="0") int start,
					   ModelMap model, @CookieValue(value="count", defaultValue="0", required=true) String value,
					   HttpServletResponse response, HeaderInfo headerInfo) {
		
		//컨트롤러 메소드에서 자주 사용되는 값이 있을 경우 아규먼트 리졸버를 만들어서 넘겨주도록 하면 편리하게 사용할 수 있다.
		
		System.out.println("---------------------------------");
		System.out.println(headerInfo.get("user-agent"));
		System.out.println("---------------------------------");
		/* spring mvc 사용해서 @CookieValue 사용해서 쿠키가 있는지 없는지 검사할 필요가 없어짐.
		String value = null;
		boolean find = false;
		Cookie[] cookies = request.getCookies();//쿠키들을 request에서 모두 받아옴(배열)
		if(cookies != null) {//쿠키가 비어있지 않다면
			for(Cookie cookie : cookies) {//cookies 배열의 모든 인덱스를 하나씩 꺼내어서 검사
				if("count".equals(cookie.getName())) {//쿠키들 중 count라는 이름의 쿠키가 있다면
					find = true;//find 값을 true로 설정한다.(즉 이전에 방문한 적이 있다!)
					value = cookie.getValue();//그리고 value(여기서는 방문 횟수가 된다)값을 저장한다.
					break;
				}
			}
		}
		
		if(!find) {//find가 false일때, 즉 방문한 적이 없어서 쿠키가 없을때
			value = "1";//첫 방문이므로 value를 1로 설정한다. 쿠키값은 문자열 형태로 들어가므로 "1"로 설정한다.
		}else {//방문한 적이 있다면, 즉 find가 true 일때
			try {
				int i = Integer.parseInt(value);//정수형 i 변수에 value 값을 저장하고
				value = Integer.toString(++i);// 이전에 방문 한 적이 있으므로(쿠키가 존재하므로) value 값을 1 증가시켜서 저장한다.
			}catch(Exception e) {//혹시나 하는 예외상황을 고려하여 예외 처리를 해준다.
				value = "1";
			}
		}*/
		
		
		try {//@CookieValue 애노테이션 사용으로 쿠키 존재 유무를 확인하는 코드는 필요 없이 바로 값을 변경하는 코드만 있으면 된다
			int i = Integer.parseInt(value);//정수형 i 변수에 value 값을 저장하고
			value = Integer.toString(++i);// 이전에 방문 한 적이 있으므로(쿠키가 존재하므로) value 값을 1 증가시켜서 저장한다.
		}catch(Exception e) {//혹시나 하는 예외상황을 고려하여 예외 처리를 해준다.
			value = "1";
		}
		
		Cookie cookie = new Cookie("count",value);// 위의 과정을 거쳐서 나온 value 값을 count 라는 이름의 쿠키에 저장해준다.
		cookie.setMaxAge(60*60*24*365);//쿠키의 생명주기를 일단 1년으로 설정했다.
		cookie.setPath("/");//경로 이하에 모두 쿠키 적용.
		response.addCookie(cookie);// 응답으로 클라이언트에 전해준다. 변경된 값을 클라이언트에 적용해줘야한다 꼭!! 
		
		// start로 시작하는 방명록 목록 구하기
		List<Guestbook> list = guestbookService.getGuestbooks(start);
		
		// 전체 페이지수 구하기
		int count = guestbookService.getCount();
		int pageCount = count / GuestbookService.LIMIT;
		if(count % GuestbookService.LIMIT > 0)
			pageCount++;
		
		// 페이지 수만큼 start의 값을 리스트로 저장
		// 예를 들면 페이지수가 3이면
		// 0, 5, 10 이렇게 저장된다.
		// list?start=0 , list?start=5, list?start=10 으로 링크가 걸린다.
		List<Integer> pageStartList = new ArrayList<>();
		for(int i = 0; i < pageCount; i++) {
			pageStartList.add(i * GuestbookService.LIMIT);
		}
		
		model.addAttribute("list", list);
		model.addAttribute("count", count);
		model.addAttribute("pageStartList", pageStartList);
		model.addAttribute("cookieCount",value);//쿠키의 value 값을 model에 담아서 전송
		return "list";
	}
	
	@PostMapping(path="/write")
	public String write(@ModelAttribute Guestbook guestbook,
						HttpServletRequest request) {
		String clientIp = request.getRemoteAddr();
		System.out.println("clientIp : " + clientIp);
		System.out.println(guestbook);
		guestbookService.addGuestbook(guestbook, clientIp);
		return "redirect:list";
	}
	
	@GetMapping(path="/delete")
	public String delete(@RequestParam(name="id", required=true) Long id, 
			             @SessionAttribute("isAdmin") String isAdmin,
			             HttpServletRequest request,
			             RedirectAttributes redirectAttr) {
		if(isAdmin == null || !"true".equals(isAdmin)) { // 세션값이 true가 아닐 경우
			redirectAttr.addFlashAttribute("errorMessage", "로그인을 하지 않았습니다.");
			return "redirect:loginform";
		}
		String clientIp = request.getRemoteAddr();
		guestbookService.deleteGuestbook(id, clientIp);
		return "redirect:list";		
	}
}
