package com.corporate.admin.pattern.chain;

import com.corporate.admin.domain.repository.UserRepository;
import com.corporate.admin.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserExistsValidator extends AbstractAccessValidator {

    private final UserRepository users;

    @Override
    public void validate(AccessContext ctx) {
        if (ctx.getUserId() != null && !users.existsById(ctx.getUserId())) {
            throw new NotFoundException("User " + ctx.getUserId() + " not found");
        }
        proceed(ctx);
    }
}