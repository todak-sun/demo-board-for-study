package io.todaksun.study.demoboard.util;

import io.todaksun.study.demoboard.domain.entities.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MemberAdapter extends User {

    private Member member;

    public MemberAdapter(Member member) {
        super(member.getUsername(), member.getPassword(), authorities(List.of("ROLE_USER")));
        this.member = member;
    }

    private static Collection<? extends GrantedAuthority> authorities(List<String> roles) {
        return roles.stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public Member getMember() {
        return this.member;
    }

}
