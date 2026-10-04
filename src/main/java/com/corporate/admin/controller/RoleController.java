package com.corporate.admin.controller;

import com.corporate.admin.dto.request.CreateRoleRequest;
import com.corporate.admin.dto.response.RoleResponse;
import com.corporate.admin.mapper.RoleMapper;
import com.corporate.admin.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService service;
    private final RoleMapper mapper;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_MANAGE')")
    public List<RoleResponse> list() {
        return service.findAll().stream().map(mapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGE')")
    public RoleResponse get(@PathVariable Long id) {
        return mapper.toResponse(service.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_MANAGE')")
    public RoleResponse create(@Valid @RequestBody CreateRoleRequest req) {
        return mapper.toResponse(service.create(req));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGE')")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @PostMapping("/{roleId}/permissions/{permissionId}")
    @PreAuthorize("hasAuthority('PERMISSION_MANAGE')")
    public void addPermission(@PathVariable Long roleId, @PathVariable Long permissionId) {
        service.addPermission(roleId, permissionId);
    }

    @DeleteMapping("/{roleId}/permissions/{permissionId}")
    @PreAuthorize("hasAuthority('PERMISSION_MANAGE')")
    public void removePermission(@PathVariable Long roleId, @PathVariable Long permissionId) {
        service.removePermission(roleId, permissionId);
    }
}