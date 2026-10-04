package com.corporate.admin.pattern.decorator;

import com.corporate.admin.domain.entity.User;
import com.corporate.admin.pattern.strategy.PermissionEvaluationStrategy;

public abstract class PermissionServiceDecorator implements PermissionEvaluationStrategy {

    protected final PermissionEvaluationStrategy delegate;

    protected PermissionServiceDecorator(PermissionEvaluationStrategy delegate) {
        this.delegate = delegate;
    }

    @Override
    public String name() {
        return delegate.name();
    }

    @Override
    public boolean hasPermission(User user, String permissionCode) {
        return delegate.hasPermission(user, permissionCode);
    }
}