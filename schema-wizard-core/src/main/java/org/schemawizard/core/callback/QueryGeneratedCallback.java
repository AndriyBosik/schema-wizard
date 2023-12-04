package org.schemawizard.core.callback;

public interface QueryGeneratedCallback extends Callback {
    void handle(String sql);
}
