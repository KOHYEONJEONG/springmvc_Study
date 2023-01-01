package hello.springmvc.basic.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

@Slf4j
@Controller
public class RequestBodyStringController {//HTTP 메시지 바디 - 단순 Text(문자) 테스트

    //✌ 요청 파라미터와 다르게 Http 메시지 바디를 통해 데이터가 직접 데이터가 넘어오는 경우는
    // @RequestParam, @ModelAttribute를 사용할 수 없다!!!!
    // (물론 HTML Form형식으로 전달되는 경우는 요청 파리미터로 인정된다.)
    // - 먼저 가장 단순한 텍스트 메시지를 HTTP 메시지 바디에 담아서 전송하고 읽어보자
    // - ✌️Http 메시지 바디의 데이터를 InputStream을 사용해서 직접 읽을 수 있다.

    @PostMapping("/request-body-string-v1")
    public void requestBodyString(HttpServletRequest request, HttpServletResponse response) throws IOException {

        //inputSream 얻기
        ServletInputStream inputStream = request.getInputStream();

        //byte코드를 인코딩해야한다.
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        //Postman에서 Body선택 - raw클릭 후 'Text' - hello 입력
        log.info("messageBody={}",messageBody);//messageBody=hello

        response.getWriter().write("ok");//회사에서도 이렇게 많이 사용하더라~(return 타입은 void로 하고)
    }

    /**
     * [스프링 MVC는 다음 파라미터를 지원한다.]
     * - InputStream(Reader) : HTTP 요청 메시지 바디의 내용을 직접 조회
     * - OutputStream(Writer) : Http 응답 메시지의 바디에 직접 결과 출력
     * */
    @PostMapping("/request-body-string-v2")
    public void requestBodyStringV2(InputStream inputStream, Writer responseWriter) throws IOException {
        //HttpServletRequest을 전체 다 사용하는게 아니지? InputStream만 사용해도 되기때문에~

        //byte코드를 인코딩해야한다.
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

//      //Postman에서 Body선택 - raw클릭 후 'Text' - hi 입력
        log.info("messageBody={}",messageBody);//messageBody=hi
        responseWriter.write("ok");
    }

    //Stream 바꾸는게 귀찮지?! 스프링 알아서 해줘!
    // ==> Http 메시지 컨버터 기능이 있다.
    // HTTP 메시지 자체를 그대로 주고 받게할 수 있다.
    @PostMapping("/request-body-string-v3")
    public  HttpEntity<String> requestBodyStringV3(HttpEntity<String> httpEntity){
        /**
         * HttpEntity : HTTP header, body 정보를 편리하게 조회
         * - '메시지 바디 정보를 직접 조회'
         * - 요청 파라미터를 조회하는 기능과 관계 없음 @RequestParam x, @ModelAttribute x
         *
         * HttpEntity는 '응답에서도' 사용가능
         * - 메시지 바디 정보 직접 반환
         * - 헤더 정보 포함 가능
         * - view 조회 x
         * */
        String messageBody = httpEntity.getBody();
        log.info("messageBody={}", messageBody);

        return new HttpEntity<>("ok");//첫번째 파리미터는 Http메시지 바디에 보낼 내용을 적는다.
    }

    //HttpEntity를 상속받은 다음 객체들도 같은 기능을 제공한다.
    @PostMapping("/request-body-string-v4")
    public  HttpEntity<String> requestBodyStringV4(RequestEntity<String> httpEntity){
        //RequestEntity
        // - HttpMethod, url 정보가 추가, 요청에서 사용
        //ResponseEntity
        // - Http상태 코드 설정 가능, 응답에서 사용
        // - return new ResponseEntity<String>("Hello World", responseHeaders, HttpStatus.CREATED)
        String messageBody = httpEntity.getBody();
        log.info("messageBody={}",messageBody);

        return new ResponseEntity<String>("ok", HttpStatus.CREATED);
    }

    //다 귀찮아!!
    //요새는 이렇게 많이 사용해
    @ResponseBody
    @PostMapping("/request-body-string-v5")
    public  String requestBodyStringV5(@RequestBody String messageBody){
        //근데! header정보가 필요하다면
        //@RequestHeader를 사용하거나 HttpEntity를 사용하면 된다.
        log.info("messageBody={}",messageBody);
        return "ok";

        //(강조!!) 이렇게 메시지 바디를 직접 조회하거나 기능은 요청 파라미터를 조회하는
        // @RequestParam, @ModelAttribute와는 전혀 관계가 없다.
    }

    //정리
    /**
     * https://blog.naver.com/nanabi08/222971170367
     * */
}
