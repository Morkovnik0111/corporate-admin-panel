package com.corporate.admin.pattern.decorator;

import com.corporate.admin.domain.entity.User;
import com.corporate.admin.pattern.strategy.PermissionEvaluationStrategy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CachedPermissionService extends PermissionServiceDecorator {

    private final Map<String, Boolean> cache = new ConcurrentHashMap<>();

    public CachedPermissionService(PermissionEvaluationStrategy delegate) {
        super(delegate);
    }

    @Override
    public boolean hasPermission(User user, String permissionCode) {
        String key = user.getId() + ":" + permissionCode;
        return cache.computeIfAbsent(key, k -> delegate.hasPermission(user, permissionCode));
    }

    public void invalidate(Long userId) {
        cache.keySet().removeIf(k -> k.startsWith(userId + ":"));
    }

    public void invalidateAll() {
        cache.clear();
    }
}