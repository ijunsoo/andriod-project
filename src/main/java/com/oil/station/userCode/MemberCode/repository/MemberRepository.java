package com.oil.station.userCode.MemberCode.repository;


import com.oil.station.userCode.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository  extends JpaRepository<Member, Long> {
    Optional<Member> findByMemberId(String memberId);
    boolean existsByMemberId(String memberId);


    Optional<Member> findByRefreshToken(String refreshToken);


}
