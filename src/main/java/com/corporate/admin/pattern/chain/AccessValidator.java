package com.corporate.admin.pattern.chain;

public interface AccessValidator {
    void validate(AccessContext ctx);
    void setNext(AccessValidator next);
}