package com.oil.station.userCode.MemberCode.service;



import com.oil.station.global.security.JwtService;
import com.oil.station.global.utill.response.CustomApiResponse;
import com.oil.station.userCode.Member;
import com.oil.station.userCode.MemberCode.dto.SigninResponseDto;
import com.oil.station.userCode.MemberCode.dto.SignupRequestDto;
import com.oil.station.userCode.MemberCode.repository.MemberRepository;
import com.oil.station.userCode.MemberCode.repository.RoleRepository;
import com.oil.station.userCode.MemberRole;
import com.oil.station.userCode.Role;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.oil.station.userCode.MemberCode.enums.Authority.ROLE_USER;


@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImp implements MemberService {
    private final MemberRepository memberRepository;
    private final JwtService jwtService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<CustomApiResponse<?>> signUp(SignupRequestDto dto) {
        if (memberRepository.existsByMemberId(dto.getMemberId())) {
            CustomApiResponse<?> response = CustomApiResponse.createFailWithout(HttpStatus.BAD_REQUEST.value(), "중복되는 아이디가 존재합니다");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        Member member = Member.toMember(dto, encodedPassword, jwtService.createRefreshToken());
        // 역할 확인
        Optional<Role> optionalRole = roleRepository.findById(ROLE_USER.getId());
        if (!optionalRole.isPresent()) {
            // 역할이 존재하지 않는 경우 처리
            CustomApiResponse<?> response = CustomApiResponse.createFailWithout(HttpStatus.BAD_REQUEST.value(), "존재하지 않는 역할입니다");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        Role role = optionalRole.get();

        MemberRole memberRole = MemberRole.builder()
                .role(role) // 역할을 설정
                .member(member) // 회원 설정
                .build();

        member.addMemberRole(memberRole);

        // 저장
        memberRepository.save(member);

        CustomApiResponse<?> response = CustomApiResponse.createSuccessWithoutData(HttpStatus.OK.value(), "회원가입에 성공하였습니다");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public void updateRefreshToken(Member member, String reIssuedRefreshToken) {
        member.changeRefreshToken(reIssuedRefreshToken);
        memberRepository.saveAndFlush(member);
    }


    private static boolean isWithinFiveMinute(LocalDateTime timeToCheck) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(timeToCheck, now);
        return Math.abs(duration.toMinutes()) <= 5;
    }

    @Override
    public ResponseEntity<CustomApiResponse<?>> getMyPage(Member member) {
        SigninResponseDto signinResponseDto =new SigninResponseDto(member.getMemberId(), member.getNickname(), member.getPassword());
        CustomApiResponse<?> response = CustomApiResponse.createSuccess(HttpStatus.OK.value(), signinResponseDto,"회원 조회에 성공하였습니다.");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    }

