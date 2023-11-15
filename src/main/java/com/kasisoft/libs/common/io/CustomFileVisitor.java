package com.kasisoft.libs.common.io;

import java.util.function.*;

import java.nio.file.attribute.*;

import java.nio.file.*;

import java.io.*;

/**
 * A basic implementation of a FileVisitor which allows to use consumers as hooks and can be stopped.
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class CustomFileVisitor extends SimpleFileVisitor<Path> {

    private Function<Exception, FileVisitResult> errorHandler;
    private Consumer<Path>                       onPostDirectory;
    private Consumer<Path>                       onPreDirectory;
    private Consumer<Path>                       onFile;
    private Consumer<Path>                       onFileFailed;
    private boolean                              stop;

    public CustomFileVisitor() {
        this.errorHandler    = $ -> FileVisitResult.TERMINATE;
        this.onPostDirectory = $ -> {};
        this.onPreDirectory  = $ -> {};
        this.onFile          = $ -> {};
        this.onFileFailed    = $ -> {};
        this.stop            = false;
    }

    public void reset() {
      stop = false;
    }

    public void stop() {
      stop = true;
    }

    public void setOnPostDirectory(Consumer<Path> onPostDirectory) {
        this.onPostDirectory = onPostDirectory;
    }

    public void setOnPreDirectory(Consumer<Path> onPreDirectory) {
        this.onPreDirectory = onPreDirectory;
    }

    public void setOnFile(Consumer<Path> onFile) {
        this.onFile = onFile;
    }

    public void setOnFileFailed(Consumer<Path> onFileFailed) {
        this.onFileFailed = onFileFailed;
    }

    public void setErrorHandler(Function<Exception, FileVisitResult> errorHandler) {
        this.errorHandler = errorHandler;
    }

    private FileVisitResult fileVisitResult() {
        return stop ? FileVisitResult.TERMINATE : FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException io) throws IOException {
        try {
            onPostDirectory.accept(dir);
            return fileVisitResult();
        } catch (Exception ex) {
            return errorHandler.apply(ex);
        }
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        try {
            onPreDirectory.accept(dir);
            return fileVisitResult();
        } catch (Exception ex) {
            return errorHandler.apply(ex);
        }
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        try {
            onFile.accept(file);
            return fileVisitResult();
        } catch (Exception ex) {
            return errorHandler.apply(ex);
        }
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException ioex) throws IOException {
        try {
            onFileFailed.accept(file);
            return fileVisitResult();
        } catch (Exception ex) {
            return errorHandler.apply(ex);
        }
    }

} /* ENDCLASS */
