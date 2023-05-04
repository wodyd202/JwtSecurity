package com.ljy.jwt.application;

import com.ljy.jwt.application.exception.UserNotFoundException;
import com.ljy.jwt.application.resource.GetUserResource;
import com.ljy.jwt.common.security.LoginUser;
import com.ljy.jwt.domain.User;
import com.ljy.jwt.domain.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class GetUserService implements UserDetailsService {
    private final UserRepository userRepository;

    public GetUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public GetUserResource get(String userId) {
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new UserNotFoundException("해당 아이디의 유저 정보가 존재하지 않습니다."));
        return new GetUserResource(user.getUserId(), user.getUsername(), user.getCreatedAt());
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new UsernameNotFoundException("해당 아이디의 사용자가 존재하지 않습니다."));
        return new LoginUser(user);
    }
}
