package com.udacity.jwdnd.course1.cloudstorage.services;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.udacity.jwdnd.course1.cloudstorage.Mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.Model.User;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class UserService {
    private final UserMapper userMapper;
    private final HashService hashService;

    public UserService(UserMapper userMapper, HashService hashService) {
        this.userMapper = userMapper;
        this.hashService = hashService;
    }

    public boolean isUsernameAvailable(String username) {
        return userMapper.findUserIdByUsername(username) == null;
    }

    public int createUser(User user) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String hashedPassword = hashService.getHashedValue(user.getPassword(), encodedSalt);
        return userMapper.insert(new User(
                null,
                user.getUsername(),
                encodedSalt,
                hashedPassword,
                user.getFirstName(),
                user.getLastName()
        ));
    }

    public User findByUsername(String username) {
        return userMapper.findUserIdByUsername(username);
    }

    public String getUserIdByAuthentication(Authentication authentication) {
        String username = authentication.getName();
        User user = userMapper.findUserIdByUsername(username);
        return user != null ? String.valueOf(user.getUserId()) : "-1";
    }
    
    public Integer getUserIdByAuthenticationSub(Authentication authentication) {
        String username = authentication.getName();
        User user = userMapper.findUserIdByUsername(username);
        return user != null ? user.getUserId() : -1;
    }
    

    public String getUsername(Authentication authentication) {
        return authentication.getName();
    }
}
