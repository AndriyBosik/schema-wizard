package org.schemawizard.core.dao;

import java.sql.Connection;
import java.util.function.Function;

public interface TransactionService {
    <T> T doWithinTransaction(Function<Connection, T> action);

    void doWithinTransaction(Runnable action);
}
