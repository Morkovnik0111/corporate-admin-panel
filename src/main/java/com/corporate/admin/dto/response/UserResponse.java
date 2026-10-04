package com.corporate.admin.dto.response;

import java.util.List;

public record UserResponse(
        Long id,
        String username,
        String fullName,
        String email,
        boolean enabled,
        List<String> roles) {
}