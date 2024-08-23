package com._NT.deliveryShop.service;

import com._NT.deliveryShop.domain.dto.UserDto;
import com._NT.deliveryShop.domain.entity.User;
import com._NT.deliveryShop.domain.entity.UserRoleEnum;
import com._NT.deliveryShop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDto.Response createUser(UserDto.Create userDto) {

        log.info(userDto.toString());

        String username = userDto.getUsername();
        String password = passwordEncoder.encode(userDto.getPassword());

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        // email 중복확인
        String email = userDto.getEmail();
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 Email 입니다.");
        }

        // 사용자 등록
        String mobileNumber = userDto.getMobileNumber();
        UserRoleEnum role = UserRoleEnum.USER;

        User user = new User(username, password, email, mobileNumber, role);
        userRepository.save(user);

        return UserDto.Response.builder()
                .username(username)
                .email(email)
                .mobileNumber(mobileNumber)
                .role(role)
                .build();
    }
}
