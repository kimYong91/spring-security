package com.budait.spring_security.service;

import com.budait.spring_security.model.User;
import com.budait.spring_security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 회원을 저장(가입)하는 메서드
    public User saveUser(User user) {
        // 패스워드를 암호화하여 저장
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
        // save -> 내가 만든 것이 아님, 메서드 자체가 알아서 User 필드에 작성한 username, password 를
        // ID(username)와 password(password)의 기능을 할 수 있도록 쿼리를 작성해 줌
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
