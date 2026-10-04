package com.corporate.admin.service;

import com.corporate.admin.domain.entity.User;
import com.corporate.admin.domain.repository.UserRepository;
import com.corporate.admin.exception.NotFoundException;
import com.corporate.admin.pattern.chain.AccessContext;
import com.corporate.admin.pattern.chain.AccessValidator;
import com.corporate.admin.pattern.chain.RoleExistsValidator;
import com.corporate.admin.pattern.chain.UserExistsValidator;
import com.corporate.admin.pattern.decorator.CachedPermissionService;
import com.corporate.admin.pattern.strategy.PermissionEvaluationStrategy;
import com.corporate.admin.pattern.strategy.PermissionStrategyFactory;
import org.springframework.stereotype.Service;

@Service
public class AccessControlFacade {

    private final PermissionEvaluationStrategy strategy;
    private final UserRepository users;
    private final AccessValidator chain;

    public AccessControlFacade(PermissionStrategyFactory factory,
                               UserRepository users,
                               UserExistsValidator userValidator,
                               RoleExistsValidator roleValidator) {
        this.strategy = new CachedPermissionService(factory.defaultStrategy());
        this.users = users;

        userValidator.setNext(roleValidator);
        this.chain = userValidator;
    }

    public boolean hasPermission(Long userId, String permissionCode) {
        User u = users.findById(userId)
                .orElseThrow(() -> new NotFoundException("User " + userId + " not found"));
        return strategy.hasPermission(u, permissionCode);
    }

    public void validate(AccessContext ctx) {
        chain.validate(ctx);
    }
}