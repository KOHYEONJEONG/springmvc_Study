package hello.springmvc.basic.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hello.springmvc.basic.HelloData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

/**
 * Postman 실행 - Body탭 - raw클릭 - JSON선택 - 아래 입력 후 - Send 클릭
 * {"username":"hello","age":20}
 * content-type : application/json <-- json타입으로 잘 선택됐는지 확인만~
 *
 * HTTP 메시지 컨버터가 content-type을 보고 어? json이네!!! 객체에 맞게 반환해준다.
 * (HttpMessageConverter 사용 -> MappingJackson2HttpMessageConverter가 동작한다. (content-type: application/json))
 *
 * */

@Slf4j
@Controller
public class RequestBodyJsonCotroller {//HTTP 요청 메시지 - JSON 테스트

    //json이니까 ObjectMapper를 생성해야겠지? 왜?
    private ObjectMapper objectMapper = new ObjectMapper();//문자로 된 JSON 데이터를 Jackson 라이브러리인 objectMapper 를 사용해서 자바 객체로 변환한다.

    @PostMapping("/request-body-json-v1")
    public void requestBodyJsonV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody={}",messageBody);
        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
        log.info("username={}, age={}",helloData.getUsername(), helloData.getAge());

        response.getWriter().write("ok");
    }

    /**
     * @RequestBody
     * HttpMessageConverter 사용 -> StringHttpMessageConverter 적용
     *
     * @ResponseBody
     * - 모든 메서드에 @ResponseBody 적용
     * - 메시지 바디 정보 직접 반환(✌️view 조회X)
     * - HttpMessageConverter 사용 -> StringHttpMessageConverter 적용
     */
    @ResponseBody
    @PostMapping("/request-body-json-v2")
    public String requestBodyJsonV2(@RequestBody String messageBody) throws JsonProcessingException {
        //raw-json선택
        // 입력 -> {"username":"고현정","age":12}
        //Headers탭에서 Content-Type : application/json <--으로 바뀌는 거 확인가능.
        HelloData data = objectMapper.readValue(messageBody, HelloData.class);
        log.info("username={}, age={}", data.getUsername(),data.getAge());
        return "ok";
    }

    /**
     * ✌️
     * 문자로 변환하고 다시 json으로 변환하는 과정이 불편하다.
     * @ModelAttribute처럼 한번에 객체로
     * 변환할 수는 없을까? 아래 예제를 보자.
     *
     *
     * ❤️생략불가! =>  HelloData에 @RequestBody 를 생략하면 @ModelAttribute 가 적용되어버린다.
     *              => 생략하면 HTTP 메시지 바디가 아니라 요청 파라미터를 처리하게 된다
     * @RequestBody 객체 파라미터
     * @RequestBody HelloData data
     * @RequestBody 에 직접 만든 객체를 지정할 수 있다
     *
     *
     * */
    @ResponseBody
    @PostMapping("/request-body-json-v3")
    public String requestBodyJsonV3(@RequestBody HelloData helloData){
        //HttpEntity , @RequestBody 를 사용하면 'HTTP 메시지 컨버터'가
        // HTTP 메시지 바디의 내용을 우리가 => {"username":"고현정","age":12}
        // 원하는 문자뿐만 아니라 객체 등으로 변환해준다. => @RequestBody HelloData helloData
        // HTTP 메시지 컨버터는 문자 뿐만 아니라 JSON도 객체로 변환해주는데, 우리가 방금 V2에서 했던 작업을
        // 대신 처리해준다

        //한마디로 HelloData data = objectMapper.readValue(messageBody, HelloData.class);가 필요없다. 대신 넣어주기 때문에
       log.info("username={}, age={}", helloData.getUsername(),helloData.getAge());
        return "ok";
    }

    @ResponseBody
    @PostMapping("/request-body-json-v4")
    public HelloData requestBodyJsonV4(HttpEntity<HelloData> httpEntity) {
        /*
        @RequestBody 요청
        -> JSON 요청 HTTP 메시지 컨버터 객체

        @ResponseBody 응답
        -> 객체 HTTP 메시지 컨버터 JSON 응답*/

        HelloData data = httpEntity.getBody();
        log.info("username={}, age={}", data.getUsername(), data.getAge());
        return data;//data객체가 json바뀐 문자가 HTTP 메시지 바디에 응답으로 뿌려준다.

        /*
        * {
         *     "username": "고현정",
         *     "age": 12
         * }
         * */
    }
}