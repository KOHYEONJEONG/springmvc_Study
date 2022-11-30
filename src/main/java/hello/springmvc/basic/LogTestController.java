package hello.springmvc.basic;

import org.slf4j.Logger;//중요
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController //@Controller + @RequestMapping
public class LogTestController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @RequestMapping("/log-test")
    public  String logTesT(){
        String name = "Spring";

        System.out.println("name = "+ name);
        log.trace("trace log={}", name);
        log.debug("debug log={}", name);//현재 로그는 개발 서버에서 볼떄
        log.info("info log={}", name);//현재 정보는 비즈니스 정보거나 운영시스템에도 봐야할 정보일때.
        log.warn("warn log={}", name);//경고
        log.error("error log={}", name);//error 확인해야할 것. 별도의 파일로 관리하거나


        return "OK";//HTTP BODY에 들어감.
    }
}