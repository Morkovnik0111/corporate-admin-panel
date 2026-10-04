package com.corporate.admin.pattern.observer;

import java.time.Instant;

public record AuditEvent(
        String actor,
        String action,
        String target,
        String details,
        Instant at) {
}