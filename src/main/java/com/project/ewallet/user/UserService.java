package com.project.ewallet.user;

import com.project.ewallet.user.dto.JwtClaimsDto;
import com.project.ewallet.user.dto.LoginRequestDto;
import com.project.ewallet.user.dto.LoginResponseDto;
import com.project.ewallet.user.dto.SignupRequestDto;
import com.project.ewallet.user.entity.UserDetail;
import com.project.ewallet.user.repository.UserDetailRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserDetailRepository userDetailRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Value("${jwt.access-token-expiration:5000}")
    private long accessTokenExpiration;

    public UserService(UserDetailRepository userDetailRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userDetailRepository = userDetailRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public void signup(SignupRequestDto request) {

        UserDetail userDetail = new UserDetail();
        userDetail.setName(request.getName());
        userDetail.setEmail(request.getEmail());
        userDetail.setMobileNumber(request.getMobileNumber());
        userDetail.setUsername(request.getUsername());
        userDetail.setPassword(passwordEncoder.encode(request.getPassword()));
        userDetailRepository.save(userDetail);
    }

    public LoginResponseDto login(LoginRequestDto request) {
        UserDetail userDetail = userDetailRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), userDetail.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        JwtClaimsDto claims = new JwtClaimsDto(userDetail.getId(), userDetail.getUsername(), userDetail.getEmail());
        String accessToken = jwtUtil.generateAccessToken(claims);
        String refreshToken = jwtUtil.generateRefreshToken(claims);

        userDetail.setRefreshToken(refreshToken);
        userDetailRepository.save(userDetail);

        return new LoginResponseDto(accessToken, refreshToken, accessTokenExpiration);
    }
}

