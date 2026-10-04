package com.corporate.admin.pattern.observer;

import com.corporate.admin.domain.entity.AuditLog;
import com.corporate.admin.domain.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuditLogListener {

    private final AuditLogRepository repo;

    @Async
    @EventListener
    public void on(AuditEvent event) {
        repo.save(AuditLog.builder()
                .actor(event.actor())
                .action(event.action())
                .target(event.target())
                .details(event.details())
                .timestamp(event.at())
                .build());
        log.info("AUDIT {} {} {}", event.actor(), event.action(), event.target());
    }
}