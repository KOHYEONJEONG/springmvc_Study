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
public class ResponseBodyController {//HTTP 응답 - HTTP API, 메시지 바디에 직접 입력

    //단순 Text타입으로 응답
    @GetMapping("/response-body-string-v1")
    public void responseBodyV1(HttpServletResponse response) throws IOException{
        response.getWriter().write("ok");
    }

    /*V1 응답을 좀 더 단순하게 */
    @GetMapping("/response-body-string-v2")
    public HttpEntity<String> responseBodyV2(HttpServletResponse response) throws IOException{
       return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    /*더 더 간결하게*/
    //@ResponseBody
    @GetMapping("/response-body-string-v3")
    public String responseBodyV3(){ //문자 처리
        return "ok";
    }

    /**
     * JSON타입으로 보낼 때
     *
     * 동적으로 HTTP응답코드를 변경할 때는 -> ResponseEntity<>를 추천한다.
     * 아무래도 @ResponseStatus(HttpStatus.OK)는 어노테이션이다 보니 동적으로 변경하기 싶지 않다.
     *
     * */
    @GetMapping("/response-body-json-v1")
    public ResponseEntity<HelloData> responseBodyV4() { //문자 처리
        HelloData helloData = new HelloData();
        helloData.setUsername("userA");
        helloData.setAge(20);
        return new ResponseEntity<>(helloData, HttpStatus.OK);
        /**
         * {
         *     "username": "userA",
         *     "age": 20
         * }
         * */
    }

    @ResponseStatus(HttpStatus.OK)
    //@ResponseBody
    @GetMapping("/response-body-json-v2")
    public HelloData responseBodyV2() { //문자 처리
        HelloData helloData = new HelloData();
        helloData.setUsername("userA");
        helloData.setAge(20);
        return helloData;
        /**
         * {
         *     "username": "userA",
         *     "age": 20
         * }
        * */
    }

}
