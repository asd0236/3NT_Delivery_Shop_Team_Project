package com._NT.deliveryShop.security;

import com._NT.deliveryShop.domain.entity.User;
import com._NT.deliveryShop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new UsernameNotFoundException("Not Found " + userId));

        if(user.getIsDeleted()){
            throw new UsernameNotFoundException("Not Found " + userId);
        }

        return new UserDetailsImpl(user);
    }
}