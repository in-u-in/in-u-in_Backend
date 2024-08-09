package com.in_u_in.in_u_in.user.repository;

import com.in_u_in.in_u_in.user.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);
}
