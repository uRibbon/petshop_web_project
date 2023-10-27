package com.dog.shop.service;

import com.dog.shop.domain.User;
import com.dog.shop.dto.userDto.UserReqDto;
import com.dog.shop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CheckService {

    @Autowired
    private UserRepository userRepository;

    public void agreeUser(UserReqDto userReqDto) {
        User user = new User();
        user.setAgree(userReqDto.getAgree());
        userRepository.save(user);
    }
}
