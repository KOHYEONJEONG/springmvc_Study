package hello.springmvc.basic.response;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ResponseViewController { //HTTP 응답

    @RequestMapping("/response-view-v1")
    public ModelAndView responseViewV1(){
        ModelAndView mav = new ModelAndView("response/hello");
        mav.addObject("data","hello!");
        return mav;
    }

    /*위와 동일*/
    @RequestMapping("/response-view-v2")
    public String responseViewV2(Model model){
        model.addAttribute("data","hello!");
        return "response/hello";//반환값이 String이면 반환값이 view에 이름이 된다.
    }

    /* @ResponseBody를 붙였으니 문자가 HTTP 메시지 BODY에 값을 보냄.
    * String을 반환하는 경우 - View or HTTP 메시지
      - @ResponseBody 가 없으면 response/hello 로 뷰 리졸버가 실행되어서 뷰를 찾고, 렌더링 한다.
      - @ResponseBody 가 있으면 뷰 리졸버를 실행하지 않고, HTTP 메시지 바디에 직접 response/hello 라는
        문자가 입력된다.
     */
    @ResponseBody
    @RequestMapping("/response-view-v2_1")
    public String responseViewV2_1(Model model){
        model.addAttribute("data","hello!");
        return "response/hello";//@ResponseBody를 붙였으니 문자가 HTTP 메시지 BODY에 값이 보내지는거야(VIEW 안찾고)
    }

    @RequestMapping("/response/hello")
    public void responseViewV3(Model model){
        //Controller에 이름과 view에 논리적인 이름이 같다면 return이 없어도 되지만,,, 비추천한다.(명확성이 떨어짐)
        model.addAttribute("data","hello!");
    }

}
