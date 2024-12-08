package com.example.domitory.conf;

import com.example.domitory.entity.Roles;
import com.example.domitory.entity.Users;
import com.example.domitory.repos.RoleRepository;
import com.example.domitory.repos.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class RoleSetterConf implements CommandLineRunner {

    private final RoleRepository repoRole;
    private final UserRepository repoUser;
    private final PasswordEncoder passEnc;

    public RoleSetterConf(RoleRepository repoRole, UserRepository repoUser, PasswordEncoder passEnc) {
        this.repoRole = repoRole;
        this.repoUser = repoUser;
        this.passEnc = passEnc;
    }

    @Override
    public void run(String... args) throws Exception {
        // Проверка или создание роли ADMIN
        Roles adminRole = repoRole.findByName("ADMIN").orElseGet(() -> {
            Roles role = new Roles("ADMIN");
            return repoRole.save(role);
        });

        // Проверка существования пользователя с именем "admin_s" и его создание, если не существует
        if (!repoUser.findByUsername("admin_s").isPresent()) {
            Users admin = new Users();
            admin.setUsername("admin_s");
            admin.setPassword(passEnc.encode("1234"));

            // Назначение роли ADMIN пользователю
            Set<Roles> roles = new HashSet<>();
            roles.add(adminRole);
            admin.setRoles(roles);

            repoUser.save(admin);
        }
    }

}
