package hello.springmvc.basic.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
@RestController
public class RequestParamController {

    /**
     * @ReqeustParam을 사용하면
     * 요청 파리미터를 굉장히 쉽게 사용가능.
     * */

    @RequestMapping("/request-param2-v1")
    public void requestParamV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));
        response.getWriter().write("ok");
    }

    @RequestMapping("/request-param2-v2")
    public String requestParamV2(@RequestParam("username")String memberName,
                                 @RequestParam("age") int memberAge){

        log.info("username={}, age={}",memberName,memberAge);
        return "ok";
    }

    @RequestMapping("/request-param2-v3")
    public String requestParamV3(@RequestParam String username,
                                 @RequestParam int age){

        log.info("v3) username={}, age={}",username,age);
        return "ok";
    }

    //인간에 욕심은 끝이 없지!
    // String, int, Integer 등의 단순한 타입이면 @RequestPram도 생략가능하다.
    @RequestMapping("/request-param2-v4")
    public String requestParamV4(String username, int age){
        log.info("v4) username={}, age={}",username, age);
        return "ok";
    }

    //필수값! 설정
    //필수값이 없으면 400에러 반환
    @RequestMapping("/request-param2-required")
    public String requestParamRequired(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Integer age //null을 받을 수 있게 int -> Integer로 바꿨음.
    ){
        /**
         * required
         * - 파라미터 필수 여부
         * - 기본값이 파라미터 필수('true')이다.
         *
         * /request-param요청
         * - username이 없으면 400 예외가 발생한다.
         *
         * 주의!- 파라미터 이름만 사용(파라미터 이름만 있을경우)
         * - 빈문자로 통과!
         *
         * 주의! 기본형(primitive)에 null입력
         * - /request-param 요청
         * - @RequestPram(required = false) int age
         * -  * null을 int에 입력하는 것은 불가능(500 예외 발생생         * @
         * */
        log.info("required) username={}, age={}",username, age);
        ///request-param-required?username=고현정
        //2022-12-30 17:12:07.571  INFO 14256 --- [nio-8080-exec-1] h.s.b.request.RequestParamController     : username=고현정, age=null
        return "ok";
    }

    //기본값!
    /**
     * 파라미터에 값이 없을 경우 defaultValue를 사용하면 기본 값을 적용할 수 있다.
     * 또, 이미 기본 값이 있기 때문에 required는 의미가 없다.
     *
     * 파라미터만 있고 값은 없는 경우(빈문자의 경우에도 설정한 기본 값이 적용된다.
     */
    @RequestMapping("/request-param2-default")
    public String requestParamDefault(
            @RequestParam(defaultValue = "guest") String username,
            @RequestParam(defaultValue = "-1") int age){
        log.info("(default) username={}, age={}",username, age);
        return "ok";
    }

    @RequestMapping("/request-param2-map")
    public String requestParam(@RequestParam Map<String, Object> paramMap){

        log.info("username={}, age={}", paramMap.get("username"), paramMap.get("age"));
        return "ok";
    }
}
