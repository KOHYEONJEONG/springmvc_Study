package hello.springmvc.basic.request;

import hello.springmvc.basic.HelloData;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Controller
public class RequestParamController { //HTTP 요청 파라미터 - 쿼리 파라미터, HTML Form 강의

    //POSTMAN으로 테스트 실행하면 된다.

    @RequestMapping("/request-param-v1")
    public void requestParamV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //http:127.0.0.1:8080/request-param-v1?username=고현정&age=26 실행

        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));
        log.info("username={}, age={}", username, age);
        //2022-11-30 11:42:52.303  INFO 10232 --- [nio-8080-exec-2] h.s.b.r.request.RequestParamController   : username=고현정, age=26

        response.getWriter().write("ok");//http 메시지 Body에 뿌려줌.
    }

    /*@RequestParam 사용방법*/

    @ResponseBody //http 메시지 바디에 바로 뿌려줌(view를 찾지 않고)
    @RequestMapping("/request-param-v2") //method가 지정되지 않아서 모두 들어올 수 있음.
    public  String requestParamaV2(@RequestParam("username") String memberName,
                                   @RequestParam("age") int memberAge){

        //http://localhost:8080/request-param-v2?username=김&age=10

        log.info("username={}, age={}", memberName, memberAge);
        //2022-11-25 13:41:02.024  INFO 10784 --- [nio-8080-exec-2] h.s.b.r.request.RequestParamController   : username=김, age=10
        return "ok";
    }

    @ResponseBody //http 메시지 바디에 바로 뿌려줌(view를 찾지 않고)
    @RequestMapping("/request-param-v3") //method가 지정되지 않아서 모두 들어올 수 있음.
    public  String requestParamaV3(@RequestParam String username, //url 파라미터와 명이 같으면 ("") 필요없음
                                   @RequestParam int age){
        
        //http://localhost:8080/request-param-v3?username=김&age=10

        log.info("username={}, age={}", username, age);
        return "ok";
    }

    /**
     * 너무 애노테이션을 없애도 직관적으로 알기가 어려울 수도...(합의가 되면 회사에 맞춰서 사용하자)
     * */
    @ResponseBody //http 메시지 바디에 바로 뿌려줌(view를 찾지 않고)
    @RequestMapping("/request-param-v4")
    public  String requestParamaV4(String username, //인간에 욕심은 끝이 없음, 제외 해도 받아와 짐.
                                   int age){

        //http://localhost:8080/request-param-v4?username=김&age=10

        log.info("username={}, age={}", username, age);
        return "ok";
    }

    /**
     * @RequestParam.required
     * /request-param-required -> username이 없으므로 예외
     *
     * 주의!
     * /request-param-required?username= -> 빈문자로 통과
     *
     * 주의!
     * /request-param-required
     * int age -> null을 int에 입력하는 것은 불가능, 따라서 Integer 변경해야 함(또는 다음에 나오는
    defaultValue 사용)
     */
    @ResponseBody
    @RequestMapping("/request-param-required")
    public String requestParamRequired(
            @RequestParam(required = true) String username,
            @RequestParam(required = false) Integer age) {
        //(required = true)는 필수로 들어와야하는 값
        //(required = false)는 없어도 오류 안남.(빈값 허용)


        //http://localhost:8080/request-param-required?username=hello&age=10
        //http://localhost:8080/request-param-required?username=hello
        //http://localhost:8080/request-param-required?username= <-- 파라미터 이름만 있고 값이 없는 경우 빈문자로 통과
        //http://localhost:8080/request-param-required?age=10 <-- 오류(400, Bad Request)
        //http://localhost:8080/request-param-required?username=hello&age=null <-- 오류(400, Bad Request) , int는 null을 받을 수 없음.
        log.info("username={}, age={}", username, age);
        return "ok";
    }


    /**
     * 기본값 적용
     *
     * @RequestParam
     * - defaultValue 사용
     *
     * 참고: defaultValue는 빈 문자의 경우에도 적용(기본적으로 파라미터만 있어도 빈값으로 적용된다.)
     * /request-param-default?username=
     */
    @ResponseBody
    @RequestMapping("/request-param-default")
    public String requestParamDefault(
            @RequestParam(required = true, defaultValue = "guest") String username,
            @RequestParam(required = false, defaultValue = "-1") int age) {
        //(required = true)는 필수로 들어와야하는 값
        //(required = false)는 없어도 오류 안남.(빈값 허용)

        //http://localhost:8080/request-param-default <-- 파라미터가 없어도 defaultValue가 기본값을 지정해줌.

        log.info("username={}, age={}", username, age);
        //2022-11-25 13:53:58.486  INFO 7084 --- [nio-8080-exec-2] h.s.b.r.request.RequestParamController   : username=guest, age=-1

        return "ok";
    }

    /*학원에서는 아래 방법을 사용해서 클라이언트가 요청한 값을 받아서 해결했지만, 결과적으로 아래 방법은 회사에서 사용안해 ㅠㅠㅠ */
    @ResponseBody
    @RequestMapping("/request-param-map")
    public  String requestParamMap(@RequestParam Map<String, Object> paramMap){
        log.info("username={}, age={}", paramMap.get("username"), paramMap.get("age"));
        return "ok"; //http 메시지 바디에 바로 뿌려줌.
    }

    /**
     * @ModelAttribute 애노테이션 사용
     *
     * @ModelAttribute 는 생략할 수 있다.
     * 그런데 @RequestParam 도 생략할 수 있으니 혼란이 발생할 수 있다.
     *
     * 스프링은 해당 생략시 다음과 같은 규칙을 적용한다.
     * String , int , Integer 같은 단순 타입 = @RequestParam
     * 나머지 = @ModelAttribute (argument resolver 로 지정해둔 타입 외)
     *
     */
    /*@ResponseBody
    @RequestMapping("/model-attribute")
    public String modelAttrivuteV1(@RequestParam String username, @RequestParam int age){

        HelloData helloData = new HelloData();
        helloData.setUsername(username);
        helloData.setAge(age);

        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());
        log.info("helloData={}", helloData);//ToString()이 실행 됌.
        *//*
          2022-11-30 13:22:45.751  INFO 15568 --- [nio-8080-exec-5] h.s.b.request.RequestParamController     : username=고현정, age=26
          2022-11-30 13:22:45.753  INFO 15568 --- [nio-8080-exec-5] h.s.b.request.RequestParamController     : helloData=HelloData(username=고현정, age=26)
        * *//*
        return "ok";
    }*/
    @ResponseBody
    @RequestMapping("/model-attribute")
    public String modelAttrivuteV1(@ModelAttribute HelloData helloData){//("") <-- 이 친구는 view에 보낼때 거기서 사용할 명칭인데, 현재 테스트에서는 필요 없어서
        //@ModelAttribute는 HelloData 객체가 생성되고, 요청 파라미터의 값도 모두 들어가 있다.

        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());
        log.info("helloData={}", helloData);//ToString()이 실행 됌.
        /*
         2022-11-30 13:24:40.826  INFO 15684 --- [nio-8080-exec-2] h.s.b.request.RequestParamController     : username=고현정, age=26
         2022-11-30 13:24:40.827  INFO 15684 --- [nio-8080-exec-2] h.s.b.request.RequestParamController     : helloData=HelloData(username=고현정, age=26)
        * */
        return "ok";
    }

}
