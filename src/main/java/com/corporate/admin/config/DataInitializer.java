package com.corporate.admin.config;

import com.corporate.admin.domain.entity.Permission;
import com.corporate.admin.domain.entity.Role;
import com.corporate.admin.domain.entity.User;
import com.corporate.admin.domain.repository.PermissionRepository;
import com.corporate.admin.domain.repository.RoleRepository;
import com.corporate.admin.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository users;
    private final RoleRepository roles;
    private final PermissionRepository permissions;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) {
        Permission userRead = permissions.findByCode("USER_READ").orElseGet(() ->
                permissions.save(Permission.builder().code("USER_READ").description("Read users").build()));
        Permission userWrite = permissions.findByCode("USER_WRITE").orElseGet(() ->
                permissions.save(Permission.builder().code("USER_WRITE").description("Write users").build()));
        Permission roleManage = permissions.findByCode("ROLE_MANAGE").orElseGet(() ->
                permissions.save(Permission.builder().code("ROLE_MANAGE").description("Manage roles").build()));
        Permission permManage = permissions.findByCode("PERMISSION_MANAGE").orElseGet(() ->
                permissions.save(Permission.builder().code("PERMISSION_MANAGE").description("Manage permissions").build()));

        Role superAdmin = roles.findByName("SUPER_ADMIN").orElseGet(() ->
                roles.save(Role.builder()
                        .name("SUPER_ADMIN")
                        .description("Full access")
                        .permissions(Set.of(userRead, userWrite, roleManage, permManage))
                        .build()));

        if (users.findByUsername("admin").isEmpty()) {
            users.save(User.builder()
                    .username("admin")
                    .password(encoder.encode("admin123"))
                    .fullName("System Administrator")
                    .enabled(true)
                    .roles(Set.of(superAdmin))
                    .build());
        }
    }
}