package com.kasisoft.libs.common.io;

import com.kasisoft.libs.common.*;

import jakarta.validation.constraints.*;

import java.util.function.*;

import java.nio.file.*;

/**
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class DeletingFileWalker implements Runnable {

    private Path                                 source;

    private CustomFileVisitor                    fsWalker;

    private Function<Exception, FileVisitResult> errorHandler;

    private long                                 fileCount;

    private long                                 dirCount;

    private long                                 totalSize;

    public DeletingFileWalker(@NotNull Path source) {
        this(source, null);
    }

    public DeletingFileWalker(@NotNull Path source, Function<Exception, FileVisitResult> errorHandler) {

        this.source       = source;
        this.fsWalker     = new CustomFileVisitor();
        this.errorHandler = errorHandler != null ? errorHandler : this::defaultErrorHandler;

        fsWalker.setOnFile($ -> {
            totalSize += $.toFile().length();
            fileCount++;
            IoFunctions.deleteFile($);
        });

        fsWalker.setOnPostDirectory($ -> {
            IoFunctions.deleteDir($);
            dirCount++;
        });

        fsWalker.setErrorHandler(errorHandler);

    }

    private FileVisitResult defaultErrorHandler(Exception ex) {
        throw KclException.wrap(ex);
    }

    @Override
    public synchronized void run() {
        try {
            fileCount = 0L;
            dirCount  = 0L;
            totalSize = 0L;
            Files.walkFileTree(source, fsWalker);
        } catch (Exception ex) {
            errorHandler.apply(ex);
        }
    }

    public long getFileCount() {
        return fileCount;
    }

    public long getDirCount() {
        return dirCount;
    }

    public long getTotalSize() {
        return totalSize;
    }

} /* ENDCLASS */
