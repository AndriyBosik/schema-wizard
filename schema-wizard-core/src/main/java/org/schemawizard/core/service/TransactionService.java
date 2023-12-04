package org.schemawizard.core.service;

import java.sql.Connection;
import java.util.function.Function;

public interface TransactionService {
    <T> T doWithinTransaction(Function<Connection, T> action);
}
