package org.example.expert.domain.user.entity;

import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.expert.domain.user.enums.UserRole;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.Collections;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    private String nickname;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); // 비밀번호 암호화

    // 일반 생성자
    public User(String email, String password, UserRole userRole, String nickname) {
        this.email = email;
        this.password = passwordEncoder.encode(password);
        this.userRole = userRole;
        this.nickname = nickname;
    }

    public static User fromAuthUser(AuthUser authUser) {
        // 역할 처리
        UserRole userRole = null;
        if (authUser.getUserRole() != null) {
            try {
                userRole = UserRole.valueOf(authUser.getUserRole().name());
            } catch (IllegalArgumentException e) {
                throw new InvalidRequestException("Invalid UserRole: " + authUser.getUserRole());
            }
        }

        // 비밀번호 암호화
        return new User(
                authUser.getEmail(),
                "",
                authUser.getUserRole(),
                authUser.getNickname()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(userRole.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // 비밀번호 변경시 암호화된 비밀번호로 변경
    public void changePassword(String newPassword) {
        this.password = passwordEncoder.encode(newPassword);
    }

    public void updateRole(UserRole newRole) {
        this.userRole = newRole;
    }
}
