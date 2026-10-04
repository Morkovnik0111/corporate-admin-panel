package com.corporate.admin.mapper;

import com.corporate.admin.domain.entity.Permission;
import com.corporate.admin.dto.response.PermissionResponse;
import org.springframework.stereotype.Component;

@Component
public class PermissionMapper {

    public PermissionResponse toResponse(Permission p) {
        return new PermissionResponse(p.getId(), p.getCode(), p.getDescription());
    }
}