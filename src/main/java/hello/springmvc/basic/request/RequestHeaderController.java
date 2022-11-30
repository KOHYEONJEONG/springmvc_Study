package hello.springmvc.basic.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

@Slf4j//로그찍기
@RestController
public class RequestHeaderController {

    //MultiValueMap : map이랑 유사, 하나의 key에 여러 값을 받을 수 있다.

    @RequestMapping("/headers")
    public String headers(HttpServletRequest request,
                          HttpServletResponse response,
                          HttpMethod httpMethod, //GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;
                          Locale locale, //언어 정보
                          @RequestHeader  MultiValueMap<String, String> headerMap, //MultiValueMap은 Map과 유사한데, 하나의 키에 여러 값을 받을 수 있다.(같은 키에 여러 값이 있는 경우에 사용한다.)
                          @RequestHeader("host") String host,
                          @CookieValue(value = "myCookie", required = false) String cookie)
    {
        log.info("request={}", request);       //request=org.apache.catalina.connector.RequestFacade@544210eb
        log.info("response={}", response);     //response=org.apache.catalina.connector.ResponseFacade@801eadf
        log.info("httpMethod={}", httpMethod); //httpMethod=GET
        log.info("locale={}", locale);         //locale=ko_KR
        log.info("headerMap={}", headerMap);   //headerMap={user-agent=[PostmanRuntime/7.29.2], accept=[*/*], postman-token=[8bb5cb84-f582-4385-846c-2ed22e15ec0a], host=[localhost:8080], accept-encoding=[gzip, deflate, br], connection=[keep-alive]}
        log.info("header host={}", host);      //header host=localhost:8080
        log.info("myCookie={}", cookie);       //myCookie=null
        return "ok";
    }


}
