package com.dog.shop.model;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class MemberContext extends User {

    private final com.dog.shop.domain.User member;

    /*public MemberContext(com.dog.shop.domain.User member, Collection<? extends GrantedAuthority> authorities) {
        super(Long.toString(member.getId()), member.getPassword(), authorities);
        this.member = member;
    }*/
    public MemberContext(com.dog.shop.domain.User member, Collection<? extends GrantedAuthority> authorities) {
        super(member.getEmail(), member.getPassword(), authorities);  // 여기를 수정
        this.member = member;
    }


}