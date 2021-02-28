package io.todaksun.study.demoboard.service;

import io.todaksun.study.demoboard.domain.entities.Member;
import io.todaksun.study.demoboard.domain.repositories.MemberRepository;
import io.todaksun.study.demoboard.exeption.DuplicateException;
import io.todaksun.study.demoboard.exeption.NotFoundException;
import io.todaksun.study.demoboard.util.MemberAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Transactional
    public Member signIn(Member member) {

        if (memberRepository.findByUsername(member.getUsername()).isPresent()) {
            throw new DuplicateException(member.getUsername());
        }

        member.encodePassword(passwordEncoder);
        return memberRepository.save(member);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return memberRepository.findByUsername(username)
                .map(MemberAdapter::new)
                .orElseThrow(() -> new NotFoundException(0L));
    }
}
