package com.example.domitory.controllers;

import com.example.domitory.entity.Roles;
import com.example.domitory.entity.Users;
import com.example.domitory.repos.RoleRepository;
import com.example.domitory.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
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
import java.util.List;
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


    // Регистрация нового пользователя
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Users user) {
        // Проверяем, существует ли пользователь с таким именем
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Имя пользователя уже существует");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Назначение роли "STUDENT" по умолчанию
        Roles userRole = roleRepository.findByName("STUDENT")
                .orElseThrow(() -> new RuntimeException("Роль STUDENT не найдена"));

        Set<Roles> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        // Сохранение пользователя в базе данных
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body("Регистрация прошла успешно!");
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

    @RequestMapping("/index")
    public String index(Model model) {
        // Получаем объект аутентификации
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Проверяем, что пользователь аутентифицирован
        if (authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            return "index"; // Перенаправляем на страницу dashboard для аутентифицированных пользователей
        }

        // Если пользователь не аутентифицирован, отображаем главную страницу
        return "login";
    }

}

