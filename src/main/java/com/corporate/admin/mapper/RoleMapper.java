package com.corporate.admin.mapper;

import com.corporate.admin.domain.entity.Role;
import com.corporate.admin.dto.response.RoleResponse;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public RoleResponse toResponse(Role r) {
        return new RoleResponse(
                r.getId(),
                r.getName(),
                r.getDescription(),
                r.getPermissions().stream().map(p -> p.getCode()).toList());
    }
}