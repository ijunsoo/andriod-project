package com.oil.station.userCode.MemberCode.service;




import com.oil.station.global.utill.exception.CustomException;
import com.oil.station.global.utill.init.ErrorCode;
import com.oil.station.userCode.Member;
import com.oil.station.userCode.MemberCode.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberQueryServiceImp implements MemberQueryService {
    private final MemberRepository memberRepository;
        @Override
        public Optional<Member> getMemberWithAuthorities(String loginId) {

            Member member = memberRepository.findByMemberId(loginId)
                    .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
            System.out.println(Optional.ofNullable(member));
            member.getMemberRoleList().size();
            return Optional.ofNullable(member);
        }

}
