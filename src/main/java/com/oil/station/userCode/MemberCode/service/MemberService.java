package com.oil.station.userCode.MemberCode.service;


import com.oil.station.global.utill.response.CustomApiResponse;
import com.oil.station.userCode.Member;
import com.oil.station.userCode.MemberCode.dto.SignupRequestDto;
import org.springframework.http.ResponseEntity;

public interface MemberService {
    ResponseEntity<CustomApiResponse<?>> signUp(SignupRequestDto dto);

    void updateRefreshToken(Member member, String reIssuedRefreshToken);
    ResponseEntity<CustomApiResponse<?>> getMyPage(Member member);

}
