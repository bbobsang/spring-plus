package org.example.expert.domain.user.service;

import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserDetailsServiceImpl implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        logger.info("사용자 이메일로 조회 중: {}", username);
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> {
                    logger.error("이메일로 사용자를 찾을 수 없습니다: {}", username);
                    return new UsernameNotFoundException("User not found with email: " + username);
                });

        logger.info("사용자 조회 성공: {}", user.getEmail());
        return user;
    }
}
