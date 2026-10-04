package com.corporate.admin.service.impl;

import com.corporate.admin.domain.entity.Permission;
import com.corporate.admin.domain.entity.Role;
import com.corporate.admin.domain.repository.PermissionRepository;
import com.corporate.admin.domain.repository.RoleRepository;
import com.corporate.admin.dto.request.CreateRoleRequest;
import com.corporate.admin.exception.ConflictException;
import com.corporate.admin.exception.NotFoundException;
import com.corporate.admin.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roles;
    private final PermissionRepository permissions;

    @Override
    public Role create(CreateRoleRequest req) {
        if (roles.findByName(req.name()).isPresent()) {
            throw new ConflictException("Role already exists: " + req.name());
        }
        return roles.save(Role.builder()
                .name(req.name())
                .description(req.description())
                .build());
    }

    @Override
    @Transactional(readOnly = true)
    public Role findById(Long id) {
        return roles.findById(id)
                .orElseThrow(() -> new NotFoundException("Role " + id + " not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> findAll() {
        return roles.findAll();
    }

    @Override
    public void delete(Long id) {
        Role r = findById(id);
        roles.delete(r);
    }

    @Override
    public void addPermission(Long roleId, Long permissionId) {
        Role r = findById(roleId);
        Permission p = permissions.findById(permissionId)
                .orElseThrow(() -> new NotFoundException("Permission " + permissionId + " not found"));
        r.getPermissions().add(p);
    }

    @Override
    public void removePermission(Long roleId, Long permissionId) {
        Role r = findById(roleId);
        r.getPermissions().removeIf(p -> p.getId().equals(permissionId));
    }
}