package com.dog.shop.service;

import com.dog.shop.domain.User;
import com.dog.shop.dto.UserReqDto;
import com.dog.shop.dto.UserResDto;
import com.dog.shop.myenum.Role;
import com.dog.shop.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public boolean userCheck(String email) {
        boolean isEmail = userRepository.existsUserByEmail(email);
        System.out.println("이메일 : " + email);
        System.out.println(isEmail);
        return isEmail;
    }

    public boolean signUser(UserReqDto userReqDto) {
        userReqDto.setRole(Role.valueOf("USER"));
        User user = modelMapper.map(userReqDto, User.class);
        User savedUser = userRepository.save(user);
        System.out.println("서비스 " + user.getName());
        System.out.println("서비스 " + user.getEmail());
        return true;
    }

    public List<UserResDto> getUser() {
        List<User> userList = userRepository.findAll();
        List<UserResDto> userResDtoList = userList.stream()
                .map(user -> modelMapper.map(user, UserResDto.class))
                .collect(Collectors.toList());
        return userResDtoList;
    }
}
