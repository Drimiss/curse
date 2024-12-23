package com.example.domitory.controllers;

import com.example.domitory.entity.Roles;
import com.example.domitory.entity.Users;
import com.example.domitory.repos.RoleRepository;
import com.example.domitory.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${app.secret-word}") // Секретное слово из application.properties
    private String secretWord;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Users user) {

        // Проверка секретного слова
        if (!secretWord.equals(user.getSecret())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Неверное секретное слово");
        }

        // Проверка на уникальность имени пользователя
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Имя пользователя уже существует");
        }

        // Хэширование пароля
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Установка роли
        Roles userRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new RuntimeException("Роль не найдена"));
        Set<Roles> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        // Сохранение пользователя
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Регистрация прошла успешно!");
    }


    @GetMapping("/register")
    public ResponseEntity<String> getRegPage() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/templates/register.html");
        String htmlContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("text/html;charset=UTF-8"))
                .body(htmlContent);
    }
    

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody Users user) {
        Users existingUser = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if (!passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неправильное имя пользователя или пароль");
        }
        return ResponseEntity.ok("Вход выполнен успешно!");
    }

    @GetMapping("/login")
    public ResponseEntity<String> getLoginPage() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/templates/login.html");
        String htmlContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("text/html;charset=UTF-8"))
                .body(htmlContent);
    }

}

