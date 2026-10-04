package com.corporate.admin.controller;

import com.corporate.admin.dto.request.CreateUserRequest;
import com.corporate.admin.dto.response.UserResponse;
import com.corporate.admin.mapper.UserMapper;
import com.corporate.admin.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;
    private final UserMapper mapper;

    @GetMapping
    @PreAuthorize("hasAuthority('USER_READ')")
    public List<UserResponse> list() {
        return service.findAll().stream().map(mapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_READ')")
    public UserResponse get(@PathVariable Long id) {
        return mapper.toResponse(service.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER_WRITE')")
    public UserResponse create(@Valid @RequestBody CreateUserRequest req) {
        return mapper.toResponse(service.create(req));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_WRITE')")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @PostMapping("/{userId}/roles/{roleId}")
    @PreAuthorize("hasAuthority('ROLE_MANAGE')")
    public void assignRole(@PathVariable Long userId, @PathVariable Long roleId) {
        service.assignRole(userId, roleId);
    }

    @DeleteMapping("/{userId}/roles/{roleId}")
    @PreAuthorize("hasAuthority('ROLE_MANAGE')")
    public void revokeRole(@PathVariable Long userId, @PathVariable Long roleId) {
        service.revokeRole(userId, roleId);
    }
}