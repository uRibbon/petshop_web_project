package com.dog.shop.custom;

import com.dog.shop.domain.User;
import com.dog.shop.exception.MemberNotFoundException;
import com.dog.shop.model.MemberContext;
import com.dog.shop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws MemberNotFoundException {

        User member = userRepository.findByEmail(email).orElseThrow(() -> new MemberNotFoundException("해당 멤버가 존재하지 않습니다."));
        List<GrantedAuthority> roles = new ArrayList<>();
        String role = String.valueOf(member.getRole());
        roles.add(new SimpleGrantedAuthority(role));

        MemberContext memberContext = new MemberContext(member, roles);

        return memberContext;

    }
}
