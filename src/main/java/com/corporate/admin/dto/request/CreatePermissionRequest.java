package com.corporate.admin.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreatePermissionRequest(
        @NotBlank String code,
        @NotBlank String description) {
}