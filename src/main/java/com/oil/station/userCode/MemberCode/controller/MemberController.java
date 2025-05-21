package com.oil.station.userCode.MemberCode.controller;


import com.oil.station.global.security.LoginMember;
import com.oil.station.global.utill.response.CustomApiResponse;
import com.oil.station.userCode.Member;
import com.oil.station.userCode.MemberCode.dto.SignupRequestDto;
import com.oil.station.userCode.MemberCode.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<CustomApiResponse<?>> signUp(@RequestBody SignupRequestDto dto) {
        ResponseEntity<CustomApiResponse<?>> response = memberService.signUp(dto);
        return response;
    }
    @GetMapping("/mypage")
    public ResponseEntity<CustomApiResponse<?>> getMyPage(@LoginMember Member loginMember) {
        ResponseEntity<CustomApiResponse<?>> response =memberService.getMyPage(loginMember);
        return response;
    }

}
