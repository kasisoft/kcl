package com.kasisoft.libs.common.io;

import java.util.function.*;

import java.nio.file.*;
import java.nio.file.attribute.*;

import java.io.*;

/**
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class CustomFileVisitor extends SimpleFileVisitor<Path> {

  private Function<Exception, FileVisitResult>   errorHandler;
  private Consumer<Path>                         onPostDirectory;
  private Consumer<Path>                         onPreDirectory;
  private Consumer<Path>                         onFile;
  private Consumer<Path>                         onFileFailed;
  
  public CustomFileVisitor() {
    this.errorHandler       = $ -> FileVisitResult.TERMINATE;
    this.onPostDirectory    = $ -> {};
    this.onPreDirectory     = $ -> {};
    this.onFile             = $ -> {};
    this.onFileFailed       = $ -> {};
  }
  
  public Consumer<Path> getOnPostDirectory() {
    return onPostDirectory;
  }

  public void setOnPostDirectory(Consumer<Path> onPostDirectory) {
    this.onPostDirectory = onPostDirectory;
  }

  public Consumer<Path> getOnPreDirectory() {
    return onPreDirectory;
  }

  public void setOnPreDirectory(Consumer<Path> onPreDirectory) {
    this.onPreDirectory = onPreDirectory;
  }

  public Consumer<Path> getOnFile() {
    return onFile;
  }

  public void setOnFile(Consumer<Path> onFile) {
    this.onFile = onFile;
  }

  public Consumer<Path> getOnFileFailed() {
    return onFileFailed;
  }

  public void setOnFileFailed(Consumer<Path> onFileFailed) {
    this.onFileFailed = onFileFailed;
  }

  public void setErrorHandler(Function<Exception, FileVisitResult> errorHandler) {
    this.errorHandler = errorHandler;
  }
  
  public Function<Exception, FileVisitResult> getErrorHandler() {
    return errorHandler;
  }
  
  @Override
  public FileVisitResult postVisitDirectory(Path dir, IOException io) throws IOException {
    try {
      onPostDirectory.accept(dir);
      return FileVisitResult.CONTINUE;
    } catch (Exception ex) {
      return errorHandler.apply(ex);
    }
  }

  @Override
  public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
    try {
      onPreDirectory.accept(dir);
      return FileVisitResult.CONTINUE;
    } catch (Exception ex) {
      return errorHandler.apply(ex);
    }
  }

  @Override
  public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
    try {
      onFile.accept(file);
      return FileVisitResult.CONTINUE;
    } catch (Exception ex) {
      return errorHandler.apply(ex);
    }
  }

  @Override
  public FileVisitResult visitFileFailed(Path file, IOException ioex) throws IOException {
    try {
      onFileFailed.accept(file);
      return FileVisitResult.CONTINUE;
    } catch (Exception ex) {
      return errorHandler.apply(ex);
    }
  }

} /* ENDCLASS */
