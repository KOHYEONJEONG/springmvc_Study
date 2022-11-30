package hello.springmvc.basic.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

@Slf4j
@Controller
public class RequestBodyStringController {//PostMan테스트 시 Body탭-raw클릭-메시지 아무거나 적으면 됨.
    /**
     * HTTP message Body에 데이터를 직접 담아서 요청
     *HTTP API에서 주로 사용, JSON, XML, TEXT
     * 데이터 형식은 주로 JSON 사용
     * POST, PUT, PATCH
     *
     * 요청 파라미터와 다르게, HTTP 메시지 바디를 통해 데이터가 직접 넘어오는 경우.
     * @RequestParam, @ModelAttribute를 사용할 수 없다,.
     * (!!!물론 HTML Form 형식으로 전달되는 경우는 요청 파리미터로 인정됨.!!!)
     */

    @PostMapping("/request-body-string-v1")
    public  void requestBodyString(HttpServletRequest request, HttpServletResponse response) throws IOException{

        //PostMan
        //http://127.0.0.1:8080/request-body-string-v1
        //Body탭 - raw 클릭 - Text선택 - {"data":"value"}
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);//Stream은 byte코드이기 때문에. 인코딩을 지정해서 문자로 바꿔야한다.

        log.info("messageBody={}", messageBody);
        //messageBody={"data":"value"}

        response.getWriter().write("ok");
    }

    /**
     * 파라미터에 InputStream을 적어서 받을 수도 있다.
     * ServletInputStream inputStream = request.getInputStream(); <-- 안적어도 됌.
     */
    @PostMapping("/request-body-string-v2")
    public void requestBodyStringV2(InputStream inputStream, Writer responseWriter) throws IOException {
        /**
         * 스프링 MVC는 다음 파라미터를 지원한다.
         * InputStream(Reader): HTTP 요청 메시지 바디의 내용을 직접 조회
         * OutputStream(Writer): HTTP 응답 메시지의 바디에 직접 결과 출력
         * */
        String messageBody = StreamUtils.copyToString(inputStream,StandardCharsets.UTF_8);
        log.info("messageBody={}", messageBody);
        responseWriter.write("ok");
    }

    /**
     * HttpEntity : Http header, body 정보를 편리하게 조회 가능하게 해준다(스프링)
     * - 메시지 바디 정보를 직접 조회(@RequestParam x, @ModelAttribute x <-- 요청 파라미터(GTE-쿼리스트링, POST는 <form></form>으로 보낼때만 허용) 조회시만 사용)
     * -♦ HttpMessageConverter 사용 -> StringHttpMessageConvert 적용
     *
     * 응답에서 HttpEntity 사용 가능
     * - 메시지 바디 정보 직접 반환(view 조회X)
     * - HttpMessageConvertter 사용 -> StringHttpMessageConverter적용
     *
     * */

    @PostMapping("/request-body-string-v3")
    public HttpEntity<String> requestBodyStringV3(HttpEntity<String> httpEntity) throws IOException {

        HttpHeaders messageHeader = httpEntity.getHeaders();//헤더 정보를 얻어올 수 있음.
        log.info("messageHeader={}",messageHeader);

        String messageBody = httpEntity.getBody();//메시지 바디 정보를 얻어올 수 있다.
        log.info("messageBody={}",messageBody);

        return new HttpEntity<>("ok");
    }

    /*위와 결과는 같음. 아래는 HttpEntity를 상속하는 RequestEntity, ReponseEntity를 보여주기 위해서 */
    @PostMapping("/request-body-string-v3_1")
    public HttpEntity<String> requestBodyStringV3_1(RequestEntity<String> httpEntity) throws IOException {
        
        //PostMan
        //http:127.0.0.1:8080/request-body-string-v3_1
        //Body탭 - raw클릭 - hello입력 후 - Send 클릭

        HttpHeaders messageHeader = httpEntity.getHeaders();//헤더 정보를 얻어올 수 있음.
        log.info("messageHeader={}",messageHeader);

        String messageBody = httpEntity.getBody();//메시지 바디 정보를 얻어올 수 있다.
        log.info("messageBody={}",messageBody);//messageBody=hello


        return new ResponseEntity<String>("ok",HttpStatus.CREATED);//메시지, 상태코드
    }







}
