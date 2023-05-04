package com.ljy.jwt.application;

import com.ljy.jwt.application.dto.SignUpUserDto;
import com.ljy.jwt.application.exception.AlreadySignUpedUserException;
import com.ljy.jwt.application.resource.SignUpUserResource;
import com.ljy.jwt.domain.User;
import com.ljy.jwt.domain.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SignUpUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SignUpUserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public SignUpUserResource signUp(SignUpUserDto dto) {
        verifyNotAlreadySignUpedId(dto.getId());
        User user = new User(dto.getId(), passwordEncoder.encode(dto.getPassword()), dto.getUsername());
        userRepository.save(user);
        return new SignUpUserResource(user.getUserId(), user.getUsername(), user.getCreatedAt());
    }

    private void verifyNotAlreadySignUpedId(String id) {
        if(userRepository.existByUserId(id)) {
            throw new AlreadySignUpedUserException("이미 해당 아이디로 가입된 사용자가 존재합니다.");
        }
    }
}
