package com.corporate.admin.pattern.observer;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class AuditEventPublisher {

    private final ApplicationEventPublisher publisher;

    public AuditEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void publish(String actor, String action, String target, String details) {
        publisher.publishEvent(new AuditEvent(actor, action, target, details, Instant.now()));
    }
}