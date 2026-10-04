package com.corporate.admin.mapper;

import com.corporate.admin.domain.entity.User;
import com.corporate.admin.dto.response.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User u) {
        return new UserResponse(
                u.getId(),
                u.getUsername(),
                u.getFullName(),
                u.getEmail(),
                u.isEnabled(),
                u.getRoles().stream().map(r -> r.getName()).toList());
    }
}