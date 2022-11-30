package hello.springmvc.basic.requestMapping;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mapping/users") //중복되는 URL은 이렇게 빼놓으면 훨씬 간결해진다.
public class MappingClassController {
    //이번시간 에는 api를 만들때 자주 사용되는 매핑 스타일
    //회원 관리를 HTTP API로 만든다 생각하고 매핑을 어떻게 하는지 알아보자

    //테스트는 POSTMAN 프로그램 사용( 사용전에 해당 프로젝트 실행 꼭 하기)

    /**
     * 회원 목록 조회: GET /users
     * 회원 등록:     POST /users
     * 회원 조회:     GET /users/{userId}
     * 회원 수정:     PATCH /users/{userId}
     * 회원 삭제:     DELETE /users/{userId
     * */

    //http://localhost:8080/

    @GetMapping
    public String user(){
        return "get Users";
    }

    @PostMapping
    public String addUser(){
        return "post user";
    }

    @GetMapping("/{userId}")
    public String findUser(@PathVariable String userId){
        return "get userId = "+ userId;
    }

    @PatchMapping("/{userId}")
    public String updateUser(@PathVariable String userId){
        return "patch userId = "+ userId;
    }

    @DeleteMapping ("/{userId}")
    public String deleteUser(@PathVariable String userId){
        return "delete userId = "+ userId;
    }

}
