package com.anpetna.member.controller;

import com.anpetna.ApiResult;
import com.anpetna.member.dto.readMemberAll.ReadMemberAllReq;
import com.anpetna.member.dto.readMemberAll.ReadMemberAllRes;
import com.anpetna.member.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
public class MemberController {
    //조회
    @GetMapping("/readAll")
    @ResponseBody //클래스에 @RestController를 쓰면 기본값이라 생략 가능 대신 @Controller일땐 필요

    public ApiResult<ReadMemberAllRes> memberReadAll()//메서드 시그니처
    //공동 응답 래퍼(성공/실패, 메시지, 코드, 데이터 등 통일된 포맷
    //@RequestParam(name = "memberId")//URL에 쿼리스트링으로 표시
    // required = false - 없어도 허용하고 없으면 null이 들어옴
    //Integer page를 쓴 이유는 null을 표현하기 위해 int는 불가

    { if (page == null) page = 1; //기본 값이 1로 설정 대신 @RequestRaram(defaultValue="1")로 간결하게 처리도 가능

        var reqBody = ReadMemberAllReq.builder()
                .memberId(MemberId)
                .memberName(MemberName)
                .memberBirthY(MemberBirthY)
                .memberBirthM(MemberBirthM)
                .memberBirthD(MemberBirthD)
                .memberBirthGM(MemberBirthGM)
                .memberGender(MemberGender)
                .memberPhone(MemberPhone)
                .memberRole(MemberRole)
                .build();
        //요청 DTO(MemberListAllRes)를 빌더 패턴으로 생성
        //var는 로컬 변수 타입 오른쪽 타입이 컴파일러

        var result = MemberService.memberReadAll(reqBody);
        //서비스 레이어 호출 컨트롤러는 파라미터만 다듬고 실제 비지니스 로직
        (목록 조회, 페이징, 정렬, 필터링)은 서비스가 담당

        return new ApiResult<>(result);
        //비지니스 결과를 공통 응답 포맷으로 감싸서 클라이언트에 반환
        //success,code,message,data 같은 속성이 있어 일관된 응답 스키마를 제공
    }

    //상세 조회
    @GetMapping("/readOne")
    @ResponseBody
    @Transactional
    //장점: increaseViewCount(쓰기)와 getBoardDetail(읽기)가 같은 트랜잭션에서 수행
    //→ 중간에 예외가 나면 둘 다 롤백.
    //단점/주의: 보통 트랜잭션은 서비스 레이어에 두는 것을 권장
    //(컨트롤러에 두면 계층 분리가 약해지고, OSIV 설정과 얽혀 JPA 세션 범위가 퍼질 수 있음).
    //서비스 메서드에도 @Transactional이 있다면 전파 규칙(propagation)에 따라 합쳐지거나
    //(기본 REQUIRED) 분리될 수 있습니다. 내부가 REQUIRES_NEW면 분리 커밋되어 의도와 달라질 수 있으므로 주의.
    // 트랜잭션은 기본적으로 RuntimeException과 Error에 대해 롤백된다
    //(checked exception은 롤백되지 않음 — 필요하면 rollbackFor 설정).

    public ApiResult<MemberReadOneRes> memberReadOne(
            //공통 응답 래퍼
            //실제 응답 데이터

            @PathVariable("boardIndex") Long boardIndex) {
        //URL 경로변수                  Long타입으로 변환해 바인딩
        //유효성 보강은 @Positive 같은 제약으로 할 수 있음

        var request = MemberReadOneReq.builder()
                .memberIndex(memberIndex)
                .build();

        memberService.increaseViewCount(memberIndex); //조회수 증가 비지니스 로직 호출(쓰기)
        //같은 트랜잭션 안이라면, 이후 예외 발생 시 이 증가도 롤백됩니다.
        //동시성/성능 포인트: 단순 select→set→save 방식이면 경쟁 시 덮어쓰기로 카운트 유실 가능
         → DB 단일 UPDATE(e.g., update board set view_cnt = view_cnt + 1 where id = ?)로 처리하는 것이 안전.
        //빈번한 증가라면 캐시/Redis INCR 후 주기적 적재도 고려.
        //증가 후 곧바로 상세를 다시 읽을 때, 1개의 트랜잭션 안이면 flush 타이밍에 따라 읽은 값이 증가
        //반영 안 될 수도 있습니다. JPA에서는 @Modifying(clearAutomatically = true) 같은 설정으로
        //2차 캐시/영속성 컨텍스트를 정리해주는 게 안전.

        var result = boardService.getBoardDetail(request); //상세조회 비지니스 로직 호출(읽기)

        return new ApiResult<>(result); //비즈니스 결과를 공통 응답 포맷으로 감싸 반환.
    }

//수정
    @PostMapping("/modify")
    //@PutMapping("/boards")
    이 메서드는 HTTP PUT 요청이 /boards 경로로 왔을 때 실행된다.
    PUT의 의미: 리소스의 전체 교체(replace)를 기대하는 HTTP 메서드이며 멱등성(idempotent) 이다 — 같은 요청을 여러 번 보내도 결과가 같아야 함(주로 업데이트/교체 용도).
    실무에서는 수정할 리소스의 식별자(ID)를 URL에 포함하는 경우가 더 명확함: /boards/{id}. 현재 코드는 ID를 request 바디에 담아 전달하는 방식으로 보인다(가능하지만 REST 관점에서 덜 직관적).
    @ResponseBody
    @Transactional
    public ApiResult<MemberModifyRes> membermodify(
            @RequestBody ModifyBoardReq request) {
        //클라이언트가 보낸 JSON본문을 ModifyBoardReq 객체로 역직렬화(매핑)
        //JSON 예: { "boardIndex": 123, "title": "수정", "content": "..." } 등 DTO 필드에 맞춰 전송해야 함.
        //변환 실패(잘못된 JSON, 타입 불일치) 시 400 Bad Request가 발생한다.
        //실무에서는 @Valid 붙여 DTO 필드 유효성 검사(@NotNull, @Size 등)를 적용하는 게 일반적.

        var result = boardService.modifyBoard(request); // 실제 비지니스 로직은 서비스 레이어에서 수행
        //기대 동작: (1) 존재 확인, (2) 권한 검증(작성자인지), (3) 변경 적용, (4) 저장, (5) 결과 DTO 반환.
        //예외/에러는 서비스에서 던지고, 컨트롤러는 @ControllerAdvice 등으로 예외를 공통 처리하는 게 깔끔함.

        return new ApiResult<>(result);
    }

//    흔한 예외/HTTP 응답 매핑 (실무에서 처리할 것)
//400 Bad Request — 바디 구조/타입 불일치, 유효성 실패(@Valid)
//401 Unauthorized — 인증 필요(로그인 안됨)
//403 Forbidden — 권한 없음(작성자가 아닌 사람이 수정 시)
//404 Not Found — 해당 게시글이 없음
//409 Conflict — 동시성 충돌(optimistic lock 실패)
//500 Internal Server Error — 기타 서버 오류
//→ @ControllerAdvice로 예외를 잡아 일관된 ApiResult 에러 포맷으로 변환하자.

//삭제
    @DeleteMapping("/boards/{boardIndex}")
//HTTP DELETE 요청으로 경로 /boards/{boardIndex}에 매핑됩니다. 예: DELETE /boards/123.
//REST 원칙에서 DELETE는 리소스 삭제를 표현하며 보통 멱등(idempotent) 해야 합니다(같은 요청을 여러 번 //보내도 결과는 같아야 함).
//경로에 식별자(boardIndex)가 포함되어 리소스를 명확하게 지정합니다(REST에 적합한 패턴).
    @ResponseBody@Transactionalpublic ApiResult<MemberDeleteRes> memberDelete(@PathVariable("memberIndex")Long memberIndex) {var request = DeleteBoardReq.builder().boardIndex(boardIndex).build();var result = boardService.deleteBoard(request);return new ApiResult<>(result);}

//등록
    @PostMapping("/boards")
    //REST 관점에서 POST /boards는 새로운 리소스(게시글)를 생성하는 데 사용됩니다.
    POST는 일반적으로 비멱등(non-idempotent) 이므로 중복 요청을 방지할 방안(예: 클라이언트 쪽 idempotency-key)을 고려해야 합니다.
    public ApiResult<PostBoardRes> postBoard(
            @RequestBody PostBoardReq reqBody) {
        //클라이언트가 보낸 JSON 바디(Content-Type: application/json)를 PostBoardReq 객체로 역직렬화합니다.
        JSON 구조가 DTO와 맞지 않거나 타입이 틀리면 400 Bad Request(HttpMessageNotReadableException 등)가 발생합니다.
        유효성 검증(@Valid) 을 추가하면 필드별 검증(제약 위반 => 400)을 할 수 있습니다.

        var result = boardService.postBoard(reqBody);
        return new ApiResult<>(result);
    }

}
