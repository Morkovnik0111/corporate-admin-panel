package com.corporate.admin.pattern.chain;

import com.corporate.admin.domain.repository.RoleRepository;
import com.corporate.admin.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleExistsValidator extends AbstractAccessValidator {

    private final RoleRepository roles;

    @Override
    public void validate(AccessContext ctx) {
        if (ctx.getRoleId() != null && !roles.existsById(ctx.getRoleId())) {
            throw new NotFoundException("Role " + ctx.getRoleId() + " not found");
        }
        proceed(ctx);
    }
}