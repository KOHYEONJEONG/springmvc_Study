package hello.springmvc.basic.request;

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

    //json이니까 ObjectMapper필요, JSON 데이터를 자바 객체로 변환한다.
   private ObjectMapper objectMapper = new ObjectMapper();

   @PostMapping("/request-body-json-v1")
    public void requestBodyJsonV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
       //HttpServletRequest를 사용해서 직접 HTTP 메시지 바디에서 데이터를 읽어와서, 문자로 변환한다.
       ServletInputStream inputStream = request.getInputStream();
       String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);//stream은 byte코드로 읽어오기에, 인코딩을 해줘야한다.

       log.info("messageBody={}", messageBody);

       HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);//HelloData에 데이터를 담아준다.
       //문자로 된 JSON 데이터를 Jackson 라이브러리인 objectMapper 를 사용해서 자바 객체로 변환한다.

       log.info("username={}, age={}",helloData.getUsername(), helloData.getAge());

       response.getWriter().write("ok");
   }

   /*위와 결과는 같지만, v1보다 훨씬 간결해*/
    @ResponseBody
    @PostMapping("/request-body-json-v2")
    public String requestBodyJsonV2(@RequestBody String messageBody) throws IOException {

        log.info("messageBody={}", messageBody);
        //2022-12-01 09:22:43.684  INFO 9676 --- [nio-8080-exec-4] h.s.b.request.RequestBodyJsonCotroller   : messageBody={"username":"hello","age":20}

        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
        log.info("username={}, age={}",helloData.getUsername(), helloData.getAge());
        //2022-12-01 09:22:43.708  INFO 9676 --- [nio-8080-exec-4] h.s.b.request.RequestBodyJsonCotroller   : username=hello, age=20

        return "ok";
    }

    /**
     * ♦️더 간결해짐!!
     * ObjectMapper도 안 사용해도
     * @ModelAttribte 기능처럼 바로 @RequestBody HelloData 를 선언하면 된다.
     * (=> @RequestBody에 직접 만든 객체를 지정할 수 있다!!!!)
     * */
    @ResponseBody
    @PostMapping("/request-body-json-v3")
    public String requestBodyJsonV3(@RequestBody HelloData data){

        log.info("username={}, age={}",data.getUsername(), data.getAge());
        //2022-12-01 09:25:27.138  INFO 9676 --- [nio-8080-exec-3] h.s.b.request.RequestBodyJsonCotroller   : username=hello, age=20
        return "ok";
    }

    /*위와 같은 결과, HttpEntity<제너릭>를 사용해도 되는데 V3가 훨씬 간결 하긴 하지용*/
    @ResponseBody
    @PostMapping("/request-body-json-v4")
    public String requestBodyJsonV4(HttpEntity<HelloData> httpEntity){
        HelloData data = httpEntity.getBody();//http에서 body를 꺼내면 HelloData를 제너릭에 넣어줬으니 HelloData 타입으로 반환
        log.info("username={}, age={}",data.getUsername(), data.getAge());
        return "ok";
    }

    /**
     * [여기까지 요약]
     * Http 메시지 컨버터는
     * 1)문자            <-Content-Type : text/plan
     * 2)JSON 객체로 변환 <-Content-Type : application/json
     * */

@ResponseBody//객체-> http 메시지 컨버터 - JSON 응답 해줌.
@PostMapping("/request-body-json-v5")
public HelloData requestBodyJsonV5(@RequestBody HelloData data){//@RequestBody : JSON 요청 - http 메시지 컨버터(그중에서 JSON을 처리하는 컨버터가 실행되징.) - 객체로 바꿔서 넘어옴.
    log.info("username={}, age={}",data.getUsername(), data.getAge());//2022-12-01 09:56:07.493  INFO 1136 --- [nio-8080-exec-1] h.s.b.request.RequestBodyJsonCotroller   : username=hello, age=20
    return data;//JSON형태로 반환된다.(@ResponseBody애 의해서)
    /**
     * return 값
     * {
     *     "username": "hello",
     *     "age": 20
     * }
     * */
}


}
