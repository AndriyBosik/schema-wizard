package com.example.migration.operation.resolver.multi;

import com.example.exception.InvalidMigrationDefinitionException;
import com.example.metadata.DatabaseProvider;
import com.example.metadata.ErrorMessage;
import com.example.migration.annotation.Provider;
import com.example.migration.model.MigrationInfo;
import com.example.migration.operation.NativeQueryFileOperation;
import com.example.migration.operation.resolver.OperationResolver;
import com.example.utils.IOUtils;

import java.io.InputStream;
import java.net.URL;

@Provider(DatabaseProvider.MULTI)
public class MultiNativeQueryFileOperationResolver implements OperationResolver<NativeQueryFileOperation> {
    @Override
    public MigrationInfo resolve(NativeQueryFileOperation operation) {
        String filePath = operation.getFilePath();
        URL resource = getClass().getClassLoader().getResource(filePath);
        if (resource == null) {
            throw new InvalidMigrationDefinitionException(String.format(ErrorMessage.NOT_EXISTENT_FILE_FORMAT, filePath));
        }
        InputStream inputStream = IOUtils.getInputStream(resource.getFile());
        return new MigrationInfo(
                new String(
                        IOUtils.readAllBytes(inputStream)));
    }
}
