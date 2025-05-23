package io.github.andriybosik.schemawizard.core.test;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import io.github.andriybosik.schemawizard.core.annotation.PreResetDb;
import io.github.andriybosik.schemawizard.core.config.impl.EnvironmentExtension;
import io.github.andriybosik.schemawizard.core.config.impl.ResetDbExtension;
import io.github.andriybosik.schemawizard.core.starter.SchemaWizard;
import io.github.andriybosik.schemawizard.core.utils.FactoryUtils;

import java.util.Arrays;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith({
        EnvironmentExtension.class,
        ResetDbExtension.class})
public class ConcurrencyTest {
    @RepeatedTest(5)
    @PreResetDb(drop = {"migration_history", "people", "posts"})
    void shouldUpgradeOnlyOnce() throws ExecutionException, InterruptedException {
        final int numberOfThreads = 2;

        CyclicBarrier startupBarrier = new CyclicBarrier(numberOfThreads);

        CompletableFuture[] futures = Stream.generate(() -> CompletableFuture.<Void>supplyAsync(() -> {
                    upgrade(startupBarrier);
                    return null;
                }))
                .limit(numberOfThreads)
                .toArray(CompletableFuture[]::new);

        CompletableFuture.allOf(futures).get();

        Arrays.stream(futures)
                .forEach(future -> assertFalse(future.isCompletedExceptionally()));

    }

    private void upgrade(CyclicBarrier startupBarrier) {
        SchemaWizard schemaWizard = FactoryUtils.newInstance("concurrency.singlestartup");
        await(startupBarrier);
        schemaWizard.up();
    }

    private void await(CyclicBarrier barrier) {
        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException exception) {
            throw new RuntimeException(exception);
        }
    }
}
