package com.test.fakeapitest.service;

import com.test.fakeapitest.domain.Member;
import com.test.fakeapitest.domain.Role;
import com.test.fakeapitest.repository.MemberRepository;
import com.test.fakeapitest.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;



    @Transactional
    public Member findByEmail(String email){

        return memberRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다"));
    }

    @Transactional
    public Member addMember(Member member){
        Optional<Role> userRole = roleRepository.findByName("ROLE_USER");
        member.addrole(userRole.get());
        Member saveMember = memberRepository.save(member);
        return saveMember;
    }

    @Transactional
    public Optional<Member> getMember(Long memberId){
        return memberRepository.findById(memberId);
    }
}
