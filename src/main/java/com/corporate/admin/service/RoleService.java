package com.corporate.admin.service;

import com.corporate.admin.domain.entity.Role;
import com.corporate.admin.dto.request.CreateRoleRequest;

import java.util.List;

public interface RoleService {
    Role create(CreateRoleRequest req);
    Role findById(Long id);
    List<Role> findAll();
    void delete(Long id);
    void addPermission(Long roleId, Long permissionId);
    void removePermission(Long roleId, Long permissionId);
}