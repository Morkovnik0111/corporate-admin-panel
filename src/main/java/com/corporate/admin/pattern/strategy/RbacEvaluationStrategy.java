package com.corporate.admin.pattern.strategy;

import com.corporate.admin.domain.entity.User;
import org.springframework.stereotype.Component;

@Component
public class RbacEvaluationStrategy implements PermissionEvaluationStrategy {

    @Override
    public String name() {
        return "RBAC";
    }

    @Override
    public boolean hasPermission(User user, String permissionCode) {
        if (!user.isEnabled()) return false;
        return user.getRoles().stream()
                .flatMap(r -> r.getPermissions().stream())
                .anyMatch(p -> p.getCode().equals(permissionCode));
    }
}