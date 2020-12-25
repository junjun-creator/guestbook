package kr.or.connect.guestbook.argumentresolver;

import java.util.HashMap;
import java.util.Map;

public class HeaderInfo {
	
	//먼저 HeaderInfo를 담을 객체를 생성하고, 이 객체가 있을경우(즉,supportsParameter 메서드가 true를 리턴하면,
	//resolveArgument 메서드에서 요청으로 들어오는 모든 헤더값을 HeaderInfo 객체에 담아서 return 해준다.
	//이를 사용하기 위해서는 WebMvcController에서 등록을 해줘야한다.
	//등록 후 Controller에서 해당 객체를 통해 header 정보를 읽어온다.
	
	//이 예제의 경우 headerinfo를 넘겨주는 예제일 뿐, 다른 데이터를 넘겨주고자 할때(자주 쓰이는 데이터) 사용할 수 있다.
	
	private Map<String, String> map;
	
	public HeaderInfo() {
		map = new HashMap<>();
	}

	public void put(String name, String value) {
		map.put(name,  value);
	}
	
	public String get(String name) {
		return map.get(name);
	}
}
