package com.corporate.admin.service;

import com.corporate.admin.domain.entity.Permission;
import com.corporate.admin.dto.request.CreatePermissionRequest;

import java.util.List;

public interface PermissionService {
    Permission create(CreatePermissionRequest req);
    Permission findById(Long id);
    List<Permission> findAll();
    void delete(Long id);
}