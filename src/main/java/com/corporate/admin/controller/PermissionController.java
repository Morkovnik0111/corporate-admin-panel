package com.corporate.admin.controller;

import com.corporate.admin.dto.request.CreatePermissionRequest;
import com.corporate.admin.dto.response.PermissionResponse;
import com.corporate.admin.mapper.PermissionMapper;
import com.corporate.admin.service.PermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService service;
    private final PermissionMapper mapper;

    @GetMapping
    @PreAuthorize("hasAuthority('PERMISSION_MANAGE')")
    public List<PermissionResponse> list() {
        return service.findAll().stream().map(mapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PERMISSION_MANAGE')")
    public PermissionResponse get(@PathVariable Long id) {
        return mapper.toResponse(service.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PERMISSION_MANAGE')")
    public PermissionResponse create(@Valid @RequestBody CreatePermissionRequest req) {
        return mapper.toResponse(service.create(req));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('PERMISSION_MANAGE')")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}