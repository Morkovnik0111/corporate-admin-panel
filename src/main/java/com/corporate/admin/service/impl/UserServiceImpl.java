package com.corporate.admin.service.impl;

import com.corporate.admin.domain.entity.Role;
import com.corporate.admin.domain.entity.User;
import com.corporate.admin.domain.repository.RoleRepository;
import com.corporate.admin.domain.repository.UserRepository;
import com.corporate.admin.dto.request.CreateUserRequest;
import com.corporate.admin.exception.ConflictException;
import com.corporate.admin.exception.NotFoundException;
import com.corporate.admin.pattern.observer.AuditEventPublisher;
import com.corporate.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository users;
    private final RoleRepository roles;
    private final PasswordEncoder encoder;
    private final AuditEventPublisher audit;

    @Override
    public User create(CreateUserRequest req) {
        if (users.existsByUsername(req.username())) {
            throw new ConflictException("Username already exists: " + req.username());
        }
        User u = User.builder()
                .username(req.username())
                .password(encoder.encode(req.password()))
                .fullName(req.fullName())
                .email(req.email())
                .enabled(true)
                .build();
        User saved = users.save(u);
        audit.publish(currentActor(), "CREATE_USER", "User#" + saved.getId(),
                "username=" + saved.getUsername());
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(Long id) {
        return users.findById(id)
                .orElseThrow(() -> new NotFoundException("User " + id + " not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return users.findAll();
    }

    @Override
    public void delete(Long id) {
        User u = findById(id);
        users.delete(u);
        audit.publish(currentActor(), "DELETE_USER", "User#" + id, "");
    }

    @Override
    public void assignRole(Long userId, Long roleId) {
        User u = findById(userId);
        Role r = roles.findById(roleId)
                .orElseThrow(() -> new NotFoundException("Role " + roleId + " not found"));
        u.getRoles().add(r);
        audit.publish(currentActor(), "ASSIGN_ROLE", "User#" + userId, "role=" + r.getName());
    }

    @Override
    public void revokeRole(Long userId, Long roleId) {
        User u = findById(userId);
        u.getRoles().removeIf(r -> r.getId().equals(roleId));
        audit.publish(currentActor(), "REVOKE_ROLE", "User#" + userId, "roleId=" + roleId);
    }

    private String currentActor() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : "system";
    }
}