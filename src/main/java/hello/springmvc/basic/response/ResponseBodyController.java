package hello.springmvc.basic.response;

import hello.springmvc.basic.HelloData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
//@Controller
//@ResponseBody를 넣으면 view를 찾지 않고 HTTP 메시지 바디에 값을 뿌려준다.
@RestController//@Controller 대신에 @RestController 애노테이션을 사용하면 컨트롤러에 '모두 @ResponseBody가 적용'되는 효과가 있다.
                //따서 뷰 템블릿을 사용하는 것이 아니라, HTTP 메시지 바디에 직접 데이터를 입력한다. 이름 그대로 RestAPI(HTTP API)를 만들 때 사용하는 컨트롤러이다.
public class ResponseBodyController {//HTTP 응답 - HTTP API, 'HTTP 메시지 바디'에 직접 입력(HTML에 담아 보내는 게 아님)


    //가장 단순한 방법
   @GetMapping("/response-body-string-v1")
    public void responseBodyV1(HttpServletResponse response) throws IOException{
      //HttpServletResponse 객체를 통해서 HTTP 메시지 바디에 직접 ok 응답 메시지를 전달한다
        response.getWriter().write("ok");
    }

    @GetMapping("/response-body-string-v2")
    public ResponseEntity<String> responseBodyV1(){
       return new ResponseEntity<String>("ok",HttpStatus.OK);
    }

    //@ResponseBody
    @GetMapping("/response-body-string-v3")
    public String responseBodyV3(){
       return "ok";
    }

    //json으로 응답
   // @ResponseBody
    @GetMapping("/response-body-json-v1")
    public ResponseEntity<HelloData> responseBodyJsonV1(){
       HelloData helloData = new HelloData();
       helloData.setUsername("userA");
       helloData.setAge(20);
       return new ResponseEntity<>(helloData, HttpStatus.CREATED); //두번째 파라미터는 상태코드이다.(변경도 가능하다)
    }

    //좀더 간결하게 적을 수도 있다!!! 단 상태코드를 사용해야한다면 아래 어노테이션을 사용하자.
    //@ResponseStatus(HttpStatus.OK) <-- 단 동적으로 바꿔야 한다면 new ResponseEntity<>를 사용하자.
    @ResponseStatus(HttpStatus.OK) //이 어노테이션을 사용하면 상태값도 넘겨줄 수 있어
  //  @ResponseBody
    @GetMapping("/response-body-json-v2")
    public HelloData responseBodyJsonV2(){
        HelloData helloData = new HelloData();
        helloData.setUsername("userA");
        helloData.setAge(20);
       return helloData;
    }

}
