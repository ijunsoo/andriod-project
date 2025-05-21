package com.oil.station.userCode.MemberCode.service;



import com.oil.station.userCode.Member;

import java.util.Optional;

public interface MemberQueryService {
    Optional<Member> getMemberWithAuthorities(String loginId);
}
