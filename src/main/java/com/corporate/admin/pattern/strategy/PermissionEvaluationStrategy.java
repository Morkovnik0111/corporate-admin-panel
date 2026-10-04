package com.corporate.admin.pattern.strategy;

import com.corporate.admin.domain.entity.User;

public interface PermissionEvaluationStrategy {
    String name();
    boolean hasPermission(User user, String permissionCode);
}