package com.corporate.admin.service;

import com.corporate.admin.domain.entity.User;
import com.corporate.admin.dto.request.CreateUserRequest;

import java.util.List;

public interface UserService {
    User create(CreateUserRequest req);
    User findById(Long id);
    List<User> findAll();
    void delete(Long id);
    void assignRole(Long userId, Long roleId);
    void revokeRole(Long userId, Long roleId);
}