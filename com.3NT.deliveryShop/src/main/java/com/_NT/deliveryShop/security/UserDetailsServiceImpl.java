package com._NT.deliveryShop.security;

import com._NT.deliveryShop.domain.entity.User;
import com._NT.deliveryShop.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.LinkedHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    // Object일 경우 LinkedHashMap으로 변환됨.
    // User로 값을 받아오면 type casting안해도 됨. 뭐가 더 좋은방법인지는 모르겠음.
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper ObjectMapper;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Object cachedUser = redisTemplate.opsForValue().get("user:" + userId);
        User user;

        if (cachedUser instanceof User) {
            user = (User) cachedUser;
            log.info("Cache Hit (User)");
        } else if (cachedUser instanceof LinkedHashMap) {
            // LinkedHashMap을 User 객체로 변환
            user = convertMapToUser((LinkedHashMap<?, ?>) cachedUser);
            log.info("Cache Hit (LinkedHashMap)");
        } else {
            // 데이터베이스에서 조회
            user = userRepository.findById(Long.parseLong(userId))
                    .orElseThrow(() -> new UsernameNotFoundException("Not Found " + userId));
            redisTemplate.opsForValue().set("user:" + userId, user, Duration.ofHours(1));
        }

        if(user.getIsDeleted()){
            throw new UsernameNotFoundException("Not Found " + userId);
        }

        return new UserDetailsImpl(user);
    }

    private User convertMapToUser(LinkedHashMap<?, ?> map) {
        return ObjectMapper.convertValue(map, User.class);
    }

//    @Override
//    @Cacheable(value = "user", key = "#userId")
//    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
//        User user = userRepository.findById(Long.parseLong(userId))
//                .orElseThrow(() -> new UsernameNotFoundException("Not Found " + userId));
//
//        if(user.getIsDeleted()){
//            throw new UsernameNotFoundException("Not Found " + userId);
//        }
//
//        return new UserDetailsImpl(user);
//    }
}
