package io.github.andriybosik.schemawizard.core.dao;

import java.sql.Connection;
import java.util.function.Consumer;
import java.util.function.Function;

public interface TransactionService {
    <T> T doWithinTransaction(Function<Connection, T> action);

    void doWithinTransaction(Consumer<Connection> action);

    void doWithinTransaction(Runnable action);
}
