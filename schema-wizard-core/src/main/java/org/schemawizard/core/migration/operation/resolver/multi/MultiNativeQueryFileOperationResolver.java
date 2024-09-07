package org.schemawizard.core.migration.operation.resolver.multi;

import org.schemawizard.core.exception.InvalidMigrationDefinitionException;
import org.schemawizard.core.metadata.DatabaseProvider;
import org.schemawizard.core.metadata.ErrorMessage;
import org.schemawizard.core.migration.annotation.Provider;
import org.schemawizard.core.migration.model.MigrationInfo;
import org.schemawizard.core.migration.operation.NativeQueryFileOperation;
import org.schemawizard.core.migration.operation.resolver.OperationResolver;
import org.schemawizard.core.utils.IOUtils;

import java.io.InputStream;
import java.net.URL;

@Provider(DatabaseProvider.MULTI)
public class MultiNativeQueryFileOperationResolver implements OperationResolver<NativeQueryFileOperation> {
    private final ClassLoader classLoader;

    public MultiNativeQueryFileOperationResolver(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public MigrationInfo resolve(NativeQueryFileOperation operation) {
        String filePath = operation.getFilePath();
        URL resource = classLoader.getResource(filePath);
        if (resource == null) {
            throw new InvalidMigrationDefinitionException(String.format(ErrorMessage.NOT_EXISTENT_FILE_FORMAT, filePath));
        }
        InputStream inputStream = IOUtils.getInputStream(resource.getFile());
        return new MigrationInfo(
                new String(
                        IOUtils.readAllBytes(inputStream)));
    }
}
