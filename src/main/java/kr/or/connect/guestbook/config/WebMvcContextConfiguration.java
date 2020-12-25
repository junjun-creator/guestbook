package kr.or.connect.guestbook.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import kr.or.connect.guestbook.argumentresolver.HeaderMapArgumentResolver;
import kr.or.connect.guestbook.interceptor.LogInterceptor;

@Configuration  // 설정파일임을 알려주는것
@EnableWebMvc  // 웹 mvc를 자동으로 사용할수 있게끔 애노테이션 걸어줌
@ComponentScan(basePackages = {"kr.or.connect.guestbook.controller"}) // controller 패키지에 있는 파일들을 참조해서 view 할거다~
public class WebMvcContextConfiguration extends WebMvcConfigurerAdapter { 

	@Override // WebMvcConfigurerAdapter에서 상속받아서 오버라이딩 함. 작성된 내용들은 DispatcherServlet이 읽어들일 것임.
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/css/**").addResourceLocations("/css/").setCachePeriod(31556926);
		registry.addResourceHandler("/img/**").addResourceLocations("/img/").setCachePeriod(31556926);
        registry.addResourceHandler("/js/**").addResourceLocations("/js/").setCachePeriod(31556926);
	}
	@Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable(); //맵핑 정보가 없는 url요청이 들어오면 처리 해주는것.
    }
   
    @Override
    public void addViewControllers(final ViewControllerRegistry registry) {
    		System.out.println("addViewControllers가 호출됩니다. ");
        registry.addViewController("/").setViewName("index"); 
    }
    
    @Bean
    public InternalResourceViewResolver getInternalResourceViewResolver() { // view의 이름을 가지고 특정 view를 찾아내게 해줌.
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/"); // 경로와 확장자를 설정
        resolver.setSuffix(".jsp");
        return resolver;
    }
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LogInterceptor());
	}
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		System.out.println("아규먼트 리졸버 등록.");
		argumentResolvers.add(new HeaderMapArgumentResolver());
	}
	
	@Bean
    public MultipartResolver multipartResolver() {
        org.springframework.web.multipart.commons.CommonsMultipartResolver multipartResolver = new org.springframework.web.multipart.commons.CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(10485760); // 1024 * 1024 * 10 최대 10메가 사이즈로 지정(변경가능)
        return multipartResolver;
    }
	
    
}
