package com.corporate.admin.service.impl;

import com.corporate.admin.domain.entity.Permission;
import com.corporate.admin.domain.repository.PermissionRepository;
import com.corporate.admin.dto.request.CreatePermissionRequest;
import com.corporate.admin.exception.ConflictException;
import com.corporate.admin.exception.NotFoundException;
import com.corporate.admin.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissions;

    @Override
    public Permission create(CreatePermissionRequest req) {
        if (permissions.findByCode(req.code()).isPresent()) {
            throw new ConflictException("Permission already exists: " + req.code());
        }
        return permissions.save(Permission.builder()
                .code(req.code())
                .description(req.description())
                .build());
    }

    @Override
    @Transactional(readOnly = true)
    public Permission findById(Long id) {
        return permissions.findById(id)
                .orElseThrow(() -> new NotFoundException("Permission " + id + " not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Permission> findAll() {
        return permissions.findAll();
    }

    @Override
    public void delete(Long id) {
        Permission p = findById(id);
        permissions.delete(p);
    }
}