package com.kasisoft.libs.common.io;

import com.kasisoft.libs.common.*;

import jakarta.validation.constraints.*;

import java.util.function.*;

import java.nio.file.*;

/**
 * Walks the filesystem while copying the filesystem structure.
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class CopyingFileWalker implements Runnable {

    private Path                source;
    private CustomFileVisitor   fsWalker;
    private long                fileCount;
    private long                dirCount;
    private long                totalSize;

    public CopyingFileWalker(@NotNull Path source, @NotNull Path destination) {
        this(source, destination, null);
    }

    public CopyingFileWalker(@NotNull Path source, @NotNull Path destination, Function<Exception, FileVisitResult> errorHandler) {

        this.source       = source;
        this.fsWalker     = new CustomFileVisitor();

        fsWalker.setOnPreDirectory($ -> {
            var destDir = destination.resolve(source.relativize($));
            IoFunctions.mkDirs(destDir);
            dirCount++;
        });

        fsWalker.setOnFile($ -> {
            var destFile = destination.resolve(source.relativize($));
            IoFunctions.copyFile($, destFile);
            fileCount++;
            totalSize += $.toFile().length();
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
