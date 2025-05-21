package com.oil.station.userCode.MemberCode.repository;


import com.oil.station.userCode.MemberCode.enums.Authority;
import com.oil.station.userCode.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByAuthority(Authority authority);
}
