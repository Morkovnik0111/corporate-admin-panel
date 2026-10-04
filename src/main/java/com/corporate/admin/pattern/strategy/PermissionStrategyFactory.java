package com.corporate.admin.pattern.strategy;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class PermissionStrategyFactory {

    private final Map<String, PermissionEvaluationStrategy> strategies;

    public PermissionStrategyFactory(List<PermissionEvaluationStrategy> list) {
        this.strategies = list.stream()
                .collect(Collectors.toMap(PermissionEvaluationStrategy::name, Function.identity()));
    }

    public PermissionEvaluationStrategy get(String name) {
        PermissionEvaluationStrategy s = strategies.get(name);
        if (s == null) {
            throw new IllegalArgumentException("Unknown strategy: " + name);
        }
        return s;
    }

    public PermissionEvaluationStrategy defaultStrategy() {
        return get("RBAC");
    }
}