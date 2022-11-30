package hello.springmvc.basic.requestMapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

//오류의 내용도 json타입으로 보내준다.
/**
 * {
 *     "timestamp": "2022-11-25T00:33:02.440+00:00",
 *     "status": 405, <-- method: GET인데 POST로 날려서 HTTP 에러코드
 *     "error": "Method Not Allowed",
 *     "path": "/hello-basic"
 *  }
 * */
@RestController //@Controller + @ResponsBody
public class MappingController {

    private Logger log = LoggerFactory.getLogger(getClass());//slf4j로 import해야함.

    ///hello-basic/ == /hello-basic <-- 둘다 다른 URL이지만, 스프링은 다른 URL 요청들을 같은 요청으로 매핑한다.

    @RequestMapping(value = "/hello-basic") //({매핑, 매핑2}) <-- or조건으로 만들어줌, 둘다 매핑되게
    public String helloBasic(){
        log.info("helloBasic");
        return "ok"; //@RestController때문에 return에 적힌 그대로 http 메시지 바디에 보낸다.(view를 찾지 않고)
    }

    //@RequestMapping은 HTTP 메서드를 지정하지 않으면 http메서드와 무관하게 호출된다.
    //모두 허용 GET, HEAD, POST, PUT, PATCH, DELETE

    //method = RequestMethod.GET 를 지정해줘야 제약이 가능하다
    @RequestMapping(value = "/mapping-get-v1", method = RequestMethod.GET) //({매핑, 매핑2}) <-- or조건으로 만들어줌, 둘다 매핑되게
    public String mappingGetV1(){
        log.info("mappingGetV1");
        return "ok"; //@RestController때문에 return에 적힌 그대로 http 메시지 바디에 보낸다.(view를 찾지 않고)
    }
    /**
     * 편리한 축약 애노테이션 (코드보기)
     * @GetMapping == @RequestMapping(method = RequestMethod.GET)
     * @PostMapping
     * @PutMapping
     * @DeleteMapping
     * @PatchMapping
     */

    @GetMapping(value="/mapping-get-v2")
    public String mappingGetV2(){
        log.info("mapping-get-v2");
        return "ok";
    }

    /**
     * @PathVariable 사용
     * 변수명이 같으면 생략 가능 (@PathVariable 이 친구는 있어야함!!!! 생략하면 안됨.)
     *
     * URL요청 Path와 같으면
     * @PathVariable("userId") String userId -> @PathVariable String userId
     * */
    @GetMapping(value="/mapping/{userId}")
    public String mappingPath(@PathVariable String userId){
        log.info("mappingPath userId={}",userId);
        //2022-11-25 09:42:56.663  INFO 13456 --- [nio-8080-exec-1] h.s.b.requestMapping.MappingController   : mappingPath userId=userAA
        return "ok";
    }

    /**
     * @PathVariable 사용 다중
     * */
    @GetMapping("/mapping/users/{userId}/orders/{orderId}")
    public String mappingPath(@PathVariable String userId, @PathVariable Long orderId){
        log.info("mappingPath userId={}, orderId={}", userId, orderId);
        return "ok";
    }

    /**
     * 특정 파리미터가 있어야 추가 매핑
     * 특정 파라미터가 있거나 없는 조건을 추가할 수 있다. '잘 사용하지는 않는다'
     *  params="mode",
     *  params="!mode"
     *  params="mode=debug"
     *  params="mode!=debug" (! = )
     * */
    @GetMapping(value = "/mapping-param", params = "mode=debug")
    public String mappingParam() { //http://localhost:8080/mapping-param?mode=debug
        log.info("mappingParam");
        return "ok";
    }

    /**
     * 특정 헤더로 추가 매핑
     * headers="mode",
     * headers="!mode"
     * headers="mode=debug"
     * headers="mode!=debug" (! = )
     */
    @GetMapping(value = "/mapping-header", headers = "mode=debug")
    public String mappingHeader() {
        //POSTMAN
        //Headers탭 - KEY)mode / VALUE)debug
        log.info("mappingHeader");
        return "ok";
    }

    /**
     * 요청에 Content타입을 소비한다는 뜻 : consumes
     * Content-Type 헤더 기반 추가 매핑 Media Type
     * consumes="application/json"
     * consumes="!application/json"
     * consumes="application/*"
     * consumes="*\/*"
     * MediaType.APPLICATION_JSON_VALUE
     */
    @PostMapping(value = "/mapping-consume", consumes = "application/json")
    public String mappingConsumes() {
        //Postman에서 Body탭 - raw 선택 /JSON 선택
        //{"data":"value"}
        //Send 클릭
        //변경된 정보는 Headers탭에서 KEY)Content-Type/ VALUE)application/json 인거 확인!!!!
        log.info("mappingConsumes");
        return "ok";
    }

    /**
     * Accept 헤더 기반 Media Type <- 클라이언트가 ContentType이 text/html인 것을 받아 들일 수 있어.
     * produces = "text/html"
     * produces = "!text/html"
     * produces = "text/*"
     * produces = "*\/*"
     */
    @PostMapping(value = "/mapping-produce", produces = "text/html")
    public String mappingProduces() {//HTTP에서 미디어 타입이 안맞으면 406 status를 줌.
        //POSTMAN
        //Headers탭 기존 Accept에 체크 테스트를 위해 체크해제 후 아래처럼 작성 후에 SEND 클릭
        // KEY)Accept/ VALUE)text/html <-- 'VALUE)기본값 -> */*는 모든 것을 허용한다.'

        log.info("mappingProduces");
        return "ok";
    }





}
