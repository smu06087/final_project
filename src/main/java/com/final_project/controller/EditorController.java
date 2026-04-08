package com.final_project.controller;

import com.final_project.dto.CodeRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.io.*;
import java.nio.file.*;
import java.util.*;

@RestController
@RequestMapping("/api") // 이 컨트롤러의 모든 주소는 /api로 시작합니다.
public class EditorController {

    /**
     * 1. 에디터 화면을 띄워주는 메서드
     * 접속 주소: http://localhost:8080/api/test
     */
    @GetMapping("/test")
    public ModelAndView testPage() {
        // templates/editor.html 파일을 찾아서 화면으로 보여줍니다.
        return new ModelAndView("editor");
    }

    /**
     * 2. 작성된 코드를 받아 실행하고 결과를 반환하는 API
     * 호출 주소: POST http://localhost:8080/api/run
     */
    @PostMapping("/run")
    public Map<String, Object> runCode(@RequestBody CodeRequest request) {
        Map<String, Object> response = new HashMap<>();
        String code = request.getCode();
        
        // 파일명은 Main.java로 고정 (사용자 코드의 클래스명도 Main이어야 함)
        Path path = Paths.get("Main.java");
        try {
            // 1. 소스코드 파일 저장
            Files.write(path, code.getBytes());

            // 2. 컴파일 실행 (javac Main.java)
            Process compileProcess = new ProcessBuilder("javac", "Main.java").start();
            
            // 컴파일 에러가 있다면 읽어오기 위해 에러 스트림 확인
            String compileError = readStream(compileProcess.getErrorStream());
            int compileExitCode = compileProcess.waitFor();

            // 컴파일 실패 시 에러 메시지 반환
            if (compileExitCode != 0) {
                response.put("status", "error");
                response.put("result", "컴파일 에러 발생:\n" + compileError);
                return response;
            }

            // 3. 자바 프로그램 실행 (java Main)
            Process runProcess = new ProcessBuilder("java", "Main").start();
            
            // 4. 실행 결과(표준 출력) 및 에러(런타임 에러) 읽기
            String output = readStream(runProcess.getInputStream());
            String runtimeError = readStream(runProcess.getErrorStream());

            response.put("status", "success");
            // 런타임 에러가 있다면 에러를, 없다면 정상 출력을 담음
            response.put("result", runtimeError.isEmpty() ? output : runtimeError);

        } catch (Exception e) {
            response.put("status", "error");
            response.put("result", "서버 내부 오류: " + e.getMessage());
        } finally {
            // 5. 사용한 임시 파일(.java, .class) 즉시 삭제
            new File("Main.java").delete();
            new File("Main.class").delete();
        }

        return response;
    }

    /**
     * 프로세스의 출력 스트림을 읽어서 문자열로 변환하는 유틸리티 메서드
     */
    private String readStream(InputStream is) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        }
    }
}