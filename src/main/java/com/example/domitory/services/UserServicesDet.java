package com.example.domitory.services;

import com.example.domitory.entity.Users;
import com.example.domitory.repos.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServicesDet implements UserDetailsService {
    private final UserRepository userRepository;

    public UserServicesDet(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + username));
    }
    public void saveUser(Users user) {
        userRepository.save(user);
    }

    // Метод для проверки, существует ли пользователь с таким именем
    public boolean userExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

}
