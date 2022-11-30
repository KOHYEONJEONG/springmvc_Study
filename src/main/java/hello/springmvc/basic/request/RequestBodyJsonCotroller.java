package hello.springmvc.basic.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.springmvc.basic.HelloData;
import lombok.extern.slf4j.Slf4j;
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
 * {"username":"hello","age":20}
 * content-type : application/json
 * */

@Slf4j
@Controller
public class RequestBodyJsonCotroller {//HTTP 요청 메시지 - JSON 테스트

    //json이니까 ObjectMapper필요
   private ObjectMapper objectMapper = new ObjectMapper();

   @PostMapping("/request-body-json-v1")
    public void requestBodyJsonV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
       //HttpServletRequest를 사용해서 직접 HTTP 메시지 바디에서 데이터를 읽어와서, 문자로 변환한다.
       ServletInputStream inputStream = request.getInputStream();
       String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);//stream은 byte코드로 읽어오기에, 인코딩을 해줘야한다.

       log.info("messageBody={}", messageBody);
       HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
       //문자로 된 JSON 데이터를 Jackson 라이브러리인 objectMapper 를 사용해서 자바 객체로 변환한다.
       log.info("username={}, age={}",helloData.getUsername(), helloData.getAge());

       response.getWriter().write("ok");
   }

   /*위와 결과는 같지만, v1보다 훨씬 간결해*/
    @ResponseBody
    @PostMapping("/request-body-json-v2")
    public String requestBodyJsonV2(@RequestBody String messageBody) throws IOException {

        log.info("messageBody={}", messageBody);
        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
        log.info("username={}, age={}",helloData.getUsername(), helloData.getAge());

        return "ok";
    }

    /*더 간결해짐*/
    @ResponseBody
    @PostMapping("/request-body-json-v3")
    public String requestBodyJsonV3(@RequestBody HelloData helloData) throws IOException {
        //파라미터 자체에 @RequestBody에 HelloData 바로 넣어버리기
        log.info("username={}, age={}",helloData.getUsername(), helloData.getAge());
        //.RequestBodyJsonCotroller   : username=hello, age=20
        return "ok";
    }


}
