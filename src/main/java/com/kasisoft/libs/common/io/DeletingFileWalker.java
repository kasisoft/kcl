package com.kasisoft.libs.common.io;

import com.kasisoft.libs.common.*;

import jakarta.validation.constraints.*;

import java.util.function.*;

import java.nio.file.*;

/**
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class DeletingFileWalker implements Runnable {

    private Path                source;
    private CustomFileVisitor   fsWalker;
    private long                fileCount;
    private long                dirCount;
    private long                totalSize;

    public DeletingFileWalker(@NotNull Path source) {
        this(source, null);
    }

    public DeletingFileWalker(@NotNull Path source, Function<Exception, FileVisitResult> errorHandler) {

        this.source       = source;
        this.fsWalker     = new CustomFileVisitor();

        fsWalker.setOnFile($ -> {
            totalSize += $.toFile().length();
            fileCount++;
            IoFunctions.deleteFile($);
        });

        fsWalker.setOnPostDirectory($ -> {
            IoFunctions.deleteDir($);
            dirCount++;
        });

        fsWalker.setErrorHandler(errorHandler != null ? errorHandler : ($ex -> FileVisitResult.TERMINATE));

    }

    public synchronized void reset() {
        fileCount = 0L;
        dirCount  = 0L;
        totalSize = 0L;
        fsWalker.reset();
    }

    public synchronized void stop() {
        fsWalker.stop();
    }

    @Override
    public synchronized void run() {
        try {
            reset();
            Files.walkFileTree(source, fsWalker);
        } catch (Exception ex) {
            throw KclException.wrap(ex);
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
