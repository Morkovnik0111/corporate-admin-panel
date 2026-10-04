package com.corporate.admin.pattern.chain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccessContext {
    private final Long userId;
    private final Long roleId;
    private final Long permissionId;
    private final String permissionCode;
}