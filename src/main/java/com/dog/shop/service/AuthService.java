package com.dog.shop.service;

import com.dog.shop.domain.User;
import com.dog.shop.dto.userDto.UserReqDto;
import com.dog.shop.dto.userDto.UserResDto;
import com.dog.shop.exception.MemberNotFoundException;
import com.dog.shop.myenum.Role;
import com.dog.shop.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    // 이메일 중복 체크
    public boolean userCheck(String email) {
        boolean isEmail = userRepository.existsUserByEmail(email);
        return isEmail;
    }

    // 비밀번호 정규식 체크
    public boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return password.matches(passwordRegex);
    }

    // 회원가입
    public void signUser(UserReqDto userReqDto) {
        if (!isValidPassword(userReqDto.getPassword())) {
            // 유)효하지 않은 비밀번호 처리 (예외 던지기 또는 오류 응답 보내기 등
            throw new MemberNotFoundException("Invalid password");
        }

        String encodedPassword = passwordEncoder.encode(userReqDto.getPassword());
        userReqDto.setPassword(encodedPassword);
        // userReqDto.setRole(Role.USER); // "USER" 문자열을 사용할 필요 없이 Role 열거형 값으로 설정
        User user = modelMapper.map(userReqDto, User.class);
        user.setRole(Role.USER);
        User savedUser = userRepository.save(user);
        // 적절한 응답값을 리턴 또는 예외 던지기 (등록 성공 또는 실패에 따라)
    }

    // 회원 리스트 불러오기
    public List<UserResDto> getUser() {
        List<User> userList = userRepository.findAll();
        List<UserResDto> userResDtoList = userList.stream()
                .map(user -> modelMapper.map(user, UserResDto.class))
                .collect(Collectors.toList());
        return userResDtoList;
    }

    // 회원 정보 명시적 삭제
    public boolean userDelete(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("user not found"));
        if (user != null) {
            user.setAddress(null);
            user.setBirthDate(null);
            user.setPhoneNumber(null);
            user.setPassword(null);
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }

    // 회원정보 업데이트
    public boolean updateUserInfo(String email, UserReqDto userReqDto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("user not found"));
        user.setName(userReqDto.getName());
        user.setPassword(userReqDto.getPassword());
        user.setAddress(userReqDto.getAddress());
        user.setPhoneNumber(userReqDto.getPhoneNumber());
        user.setBirthDate(userReqDto.getBirthDate());
        userRepository.save(user);
        return true;
    }

    // 비밀번호 재설정
    public boolean resetPwd(String email, UserReqDto userReqDto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("user not found"));
        String encodedPassword = passwordEncoder.encode(userReqDto.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
        return true;
    }
}