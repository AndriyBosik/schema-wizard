package org.schemawizard.core.config;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.schemawizard.core.utils.EnvUtils;
import org.testcontainers.containers.GenericContainer;

public abstract class AbstractDbInitializer implements DbInitializer {
    protected final GenericContainer<?> container;

    public AbstractDbInitializer(GenericContainer<?> container, int containerPort) {
        this.container = container
                .withReuse(true)
                .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
                        new HostConfig()
                                .withPortBindings(
                                        new PortBinding(
                                                Ports.Binding.bindPort(EnvUtils.DB_PORT),
                                                new ExposedPort(containerPort)))));
    }

    @Override
    public void init() {
        container.start();
    }

    @Override
    public void shutdown() {
        container.stop();
    }
}
