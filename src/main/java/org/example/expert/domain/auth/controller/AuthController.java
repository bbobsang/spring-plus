package org.example.expert.domain.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.auth.dto.request.SigninRequest;
import org.example.expert.domain.auth.dto.response.SigninResponse;
import org.example.expert.domain.auth.exception.InvalidCredentialsException;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.UserRepository;
import org.example.expert.config.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;  // AuthenticationManager 추가

    // 로그인 처리
    @PostMapping("/auth/signin")
    public ResponseEntity<SigninResponse> signin(@Valid @RequestBody SigninRequest signinRequest) {
        try {
            // 이메일로 사용자 찾기
            User user = userRepository.findByEmail(signinRequest.getEmail())
                    .orElseThrow(() -> new InvalidCredentialsException("이메일이 존재하지 않습니다."));

            // 비밀번호 확인
            if (!passwordEncoder.matches(signinRequest.getPassword(), user.getPassword())) {
                throw new InvalidCredentialsException("비밀번호가 일치하지 않습니다.");
            }

            // JWT 토큰 발급
            String token = jwtUtil.createToken(user.getId().toString(), user.getEmail(), user.getUserRole().name(), user.getNickname());

            // 로그인 성공 응답
            SigninResponse signinResponse = new SigninResponse(token, user.getNickname());
            return new ResponseEntity<>(signinResponse, HttpStatus.OK);

        } catch (InvalidCredentialsException e) {
            // 로그인 실패시 401 처리
            return new ResponseEntity<>(new SigninResponse("잘못된 로그인 정보입니다."), HttpStatus.UNAUTHORIZED);
        }
    }
}
