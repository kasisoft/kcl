package com.kasisoft.libs.common.io;

import com.kasisoft.libs.common.*;

import jakarta.validation.constraints.*;

import java.util.function.*;

import java.nio.file.*;

/**
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class MovingFileWalker implements Runnable {

    private Path                                 source;

    private CustomFileVisitor                    fsWalker;

    private Function<Exception, FileVisitResult> errorHandler;

    private long                                 fileCount;

    private long                                 dirCount;

    private long                                 totalSize;

    public MovingFileWalker(@NotNull Path source, @NotNull Path destination) {
        this(source, destination, null);
    }

    public MovingFileWalker(@NotNull Path source, @NotNull Path destination, Function<Exception, FileVisitResult> errorHandler) {

        this.source       = source;
        this.fsWalker     = new CustomFileVisitor();
        this.errorHandler = errorHandler != null ? errorHandler : this::defaultErrorHandler;

        fsWalker.setOnPreDirectory($ -> {
            var destDir = destination.resolve(source.relativize($));
            IoFunctions.mkDirs(destDir);
            dirCount++;
        });

        fsWalker.setOnFile($ -> {
            var destFile = destination.resolve(source.relativize($));
            IoFunctions.moveFile($, destFile);
            fileCount++;
            totalSize += destFile.toFile().length();
        });

        fsWalker.setOnPostDirectory(IoFunctions::delete);

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
