package com.corporate.admin.pattern.chain;

public abstract class AbstractAccessValidator implements AccessValidator {

    private AccessValidator next;

    @Override
    public void setNext(AccessValidator next) {
        this.next = next;
    }

    protected void proceed(AccessContext ctx) {
        if (next != null) {
            next.validate(ctx);
        }
    }
}