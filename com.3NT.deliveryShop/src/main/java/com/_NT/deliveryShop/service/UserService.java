package com._NT.deliveryShop.service;

import com._NT.deliveryShop.domain.dto.UserDto;
import com._NT.deliveryShop.domain.entity.User;
import com._NT.deliveryShop.domain.entity.UserRoleEnum;
import com._NT.deliveryShop.repository.UserRepository;
import com._NT.deliveryShop.repository.helper.RepositoryHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RepositoryHelper repoHelper;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, Object> redisTemplate;


    @Transactional
    public UserDto.Result createUser(UserDto.Create userDto) {

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
        UserRoleEnum role = userDto.getRole();

        User user = new User(username, password, email, mobileNumber, role);
        return UserDto.Result.of(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public UserDto.Result getUser(Long userId, User user) {

        // 관리자가 아닐 경우 타인의 정보 조회 시 403 에러 반환
        if(user.getRole() != UserRoleEnum.ADMIN && !Objects.equals(user.getUserId(), userId)) {
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }

        User targetUser = repoHelper.findUserOrThrow404(userId);

        // 삭제된 사용자에 대한 접근을 방지
        if (targetUser.getIsDeleted()) {
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }

        return UserDto.Result.of(targetUser);

    }

    @Transactional(readOnly = true)
    public Collection<UserDto.GetAllUsersResponse> getAllUsers(int page, int size, String sortBy) {

        Sort.Direction direction = Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<User> userList = userRepository.findAllByIsDeletedFalse(pageable);

        return userList.map(UserDto.GetAllUsersResponse::new).stream().toList(); // 반환 형 변환

    }

    @Transactional
    public UserDto.ModifyUserResult modifyUser(Long userId, UserDto.Modify userDto, User user) {

        // 관리자가 아닐 경우 타인의 정보 수정 시도 시 403 에러 반환
        if(user.getRole() != UserRoleEnum.ADMIN && !Objects.equals(user.getUserId(), userId)) {
            log.error("관리자가 아닐 경우 타인의 정보 수정 시도 시 403 에러 반환");
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }

        // 관리자가 아닌 사용자(USER 또는 OWNER)가 Role 을 다른 권한으로 수정 시도 시 403 에러 반환
        if(user.getRole() != UserRoleEnum.ADMIN && !user.getRole().equals(userDto.getRole())) {
            log.error("관리자가 아닌 사용자(USER 또는 OWNER)가 Role 을 다른 권한으로 수정 시도 시 403 에러 반환");
            throw new AccessDeniedException("수정 권한이 없습니다.");
        }

        User savedUser = userRepository.findById(userId).orElse(null);

        // 삭제된 사용자에 대한 접근 방지
        if (savedUser != null && savedUser.getIsDeleted()) {
            throw new AccessDeniedException("수정 권한이 없습니다.");
        }

        Objects.requireNonNull(savedUser).modifyUser(
                userDto.getUsername(),
                passwordEncoder.encode(userDto.getPassword()),
                userDto.getEmail(),
                userDto.getMobileNumber(),
                userDto.getRole()
        );

        redisTemplate.delete("user:" + userId);

        return UserDto.ModifyUserResult.of(userRepository.save(savedUser));

    }

    @Transactional
    public UserDto.DeleteUserResult deleteUser(Long userId, User user) {

        // 관리자가 아닐 경우 타인의 정보 삭제 시도 시 403 에러 반환
        if(user.getRole() != UserRoleEnum.ADMIN && !Objects.equals(user.getUserId(), userId)) {
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }

        if(!userRepository.existsById(userId)){
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }

        userRepository.deleteById(userId, LocalDateTime.now(), user.getUserId());

        return UserDto.DeleteUserResult.builder()
                .userId(userId)
                .build();
    }
}
