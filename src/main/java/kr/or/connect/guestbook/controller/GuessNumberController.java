package kr.or.connect.guestbook.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GuessNumberController {
	@GetMapping("/guess")
	public String guess(@RequestParam(name="number", required=false) Integer number, HttpSession session, ModelMap model) {
		//spring mvc에서 HttpSession 객체가 자동으로 클라이언트에 해당하는 세션을 생성해줌. 즉, request.getSession을 할 필요가 없어짐.
		String message = null;
		if(number == null) {//number 값이 없는경우(즉, 처음 페이지가 실행 된 경우)
			System.out.println(session.isNew());
			session.setAttribute("count", 0);//시도 횟수를 0으로 초기화 해서 세션에 저장
			session.setAttribute("randomNumber",(int)(Math.random()*100)+1);//난수 발생시켜 세션에 저장
			message = "내가 생각한 숫자를 맞춰보세요";
		}else {
			int count = (Integer)session.getAttribute("count");
			int randomNumber = (Integer)session.getAttribute("randomNumber");
			if(number < randomNumber) {
				message = "입력한 값은 내가 생각하고 있는 숫자보다 작습니다";
				session.setAttribute("count", ++count);
			}else if(number > randomNumber) {
				message = "입력한 값은 내가 생각하고 있는 숫자보다 큽니다";
				session.setAttribute("count", ++count);
			}else {
				message = "OK ^^"+ ++count + "번째에 맞췄습니다. 내가 생각한 숫자는"+ number +"입니다.";
				session.removeAttribute("count");
				session.removeAttribute("randomNumber");
			}
		}
		
		model.addAttribute("message", message);
		
		return "guess";
	}
}
