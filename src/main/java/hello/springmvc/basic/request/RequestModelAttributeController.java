package hello.springmvc.basic.request;

import hello.springmvc.basic.HelloData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class RequestModelAttributeController {

    @RequestMapping("/model-attribute-v1")
    public  String modelAttributeV1(@RequestParam String username, @RequestParam int age){
        HelloData helloData = new HelloData();
        helloData.setUsername(username);
        helloData.setAge(age);

        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());
        log.info("helloData={}", helloData);//@Data를 사용하면 tiString()도 만들어준다.
        return "ok";
    }

    /**
     * 마치 마법처럼 HelloData 객체가 생성되며, 요청 파라미터 값도 모두 들어가 있다.
     * 스프링 MVC는 @ModelAttribute가 있으면 다음을 실행한다.
     * - HelloData 객체를 생성한다.
     * - 요청 파라미터의 이름으로 HelloData 객체의 프로퍼티를 찾느다.
     * 그리고 해당 프로퍼티의 setter 호출해서 파라미터의 값을 입력(바인딩)한다.
     * */
    @RequestMapping("/model-attribute-v2")
    public  String modelAttributeV2(@ModelAttribute HelloData helloData){
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());
        return "ok";
    }

    //@ModelAttribute도 생략가능하지만
    //어떻게 생략을 해도 되냐면- 기본적으로 내가만든 클래스는 @ModelAttribute가 붙기 때문이다.
    //또 @RequestParam도 생략이가능한데 String, int,Integer 같은 단순 타입을 인식하기에 이것또한 생략하능하다.
    //그래도 혼란을 야기할 수 있으니 넣어주자.
    @RequestMapping("/model-attribute-v3")
    public  String modelAttributeV3(HelloData helloData){
        log.info("없음) username={}, age={}", helloData.getUsername(), helloData.getAge());
        return "ok";
    }

}
