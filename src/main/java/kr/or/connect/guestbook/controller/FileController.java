package kr.or.connect.guestbook.controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileController {
	// get방식으로 요청이 올 경우 업로드 폼을 보여줍니다.
		@GetMapping("/uploadform")
		public String uploadform() {
			return "uploadform";
		}
		
		@PostMapping("/upload")
		public String upload(@RequestParam("file") MultipartFile file) {
			
			System.out.println("파일 이름 : " + file.getOriginalFilename());
			System.out.println("파일 크기 : " + file.getSize());
			
	        try(
	                // 맥일 경우 
	                //FileOutputStream fos = new FileOutputStream("/tmp/" + file.getOriginalFilename());
	                // 윈도우일 경우
	                FileOutputStream fos = new FileOutputStream("c:/tmp/" + file.getOriginalFilename());//저 위치에 filename으로 저장하겠다!
	        																			//중복 방지를 위해 어떠한 방식이 있는지 알아보기
	                InputStream is = file.getInputStream();
	        ){
	        	    int readCount = 0;
	        	    byte[] buffer = new byte[1024];
	            while((readCount = is.read(buffer)) != -1){//배열의 크기만큼 읽어서 스트림의 끝에 도달하면 -1을 반환
	                fos.write(buffer,0,readCount);//내용을 파일에 기록함
	                //지정된 바이트 배열로부터의 오프셋(offset) 위치 off(0)로 부터 시작되는 len(readCount) 바이트를 이 파일 출력 스트림에 출력합니다.
	            }
	        }catch(Exception ex){
	            throw new RuntimeException("file Save Error");
	        }
			
			
			return "uploadok";
		}
		
		@GetMapping("/download")
		public void download(HttpServletResponse response) {

	        // 직접 파일 정보를 변수에 저장해 놨지만, 이 부분이 db에서 읽어왔다고 가정한다.
			String fileName = "redmoon.png";//다운로드 할때 파일 이름 설정
			String saveFileName = "c:/tmp/단순 레이싱 게임.png"; // 맥일 경우 "/tmp/connect.png" 로 수정
			String contentType = "image/png";
			int fileLength = 1242155;
			
	        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\";");
	        response.setHeader("Content-Transfer-Encoding", "binary");
	        response.setHeader("Content-Type", contentType);
	        response.setHeader("Content-Length", "" + fileLength);
	        response.setHeader("Pragma", "no-cache;");
	        response.setHeader("Expires", "-1;");
	        
	        try(
	                FileInputStream fis = new FileInputStream(saveFileName);
	                OutputStream out = response.getOutputStream();
	        ){
	        	    int readCount = 0;
	        	    byte[] buffer = new byte[1024];
	            while((readCount = fis.read(buffer)) != -1){
	            		out.write(buffer,0,readCount);
	            }
	        }catch(Exception ex){
	            throw new RuntimeException("file Save Error");
	        }
		}
}
