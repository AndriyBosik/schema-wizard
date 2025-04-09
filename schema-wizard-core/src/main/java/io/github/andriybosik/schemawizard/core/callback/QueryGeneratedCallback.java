package io.github.andriybosik.schemawizard.core.callback;

public interface QueryGeneratedCallback extends Callback {
    void handle(String sql);
}
