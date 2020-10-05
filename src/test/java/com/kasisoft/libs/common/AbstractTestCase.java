package com.kasisoft.libs.common;

import static org.testng.Assert.*;

import javax.validation.constraints.*;

import java.util.*;

import java.nio.file.*;

import java.io.*;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AbstractTestCase {

  private static Path tempDir;
  
  static {
    try {
      tempDir = Files.createTempDirectory("test");
    } catch (IOException ex) {
      throw new IllegalStateException(ex);
    }
  }
  
  String rootFolder;
  Path   tempFolder;
  
  public AbstractTestCase() {
    this(null);
  }
  
  public AbstractTestCase(String root) {
    rootFolder = root;
    if (rootFolder == null) {
      rootFolder = getClass().getSimpleName();
    }
    if (!rootFolder.endsWith("/")) {
      rootFolder += "/";
    }
    if (rootFolder.startsWith("/")) {
      rootFolder = rootFolder.substring(1);
    }
    tempFolder = tempDir.resolve(getClass().getSimpleName());
    try {
      Files.createDirectories(tempFolder);
    } catch (Exception ex) {
      throw new IllegalStateException(ex);
    }
  }
  
  public Path getTempPath(String name) {
    return tempFolder.resolve(name);
  }
  
  public @NotNull Optional<Path> findRootFolder() {
    var url = getClass().getClassLoader().getResource(rootFolder);
    if (url == null) {
      return Optional.empty();
    }
    try {
      return Optional.of(Paths.get(url.toURI()));
    } catch (Exception ex) {
      fail(ex.getLocalizedMessage());
      return null;
    }
  }
  
  public @NotNull Path getRootFolder() {
    return findRootFolder().orElseThrow(() -> new AssertionError(String.format("Missing resource: %s", rootFolder)));
  }

  public @NotNull Optional<Path> findResource(@NotNull String resource) {
    return findRootFolder().map($ -> $.resolve(resource));
  }
  
  public @NotNull Path getResource(@NotNull String resource) {
    return findResource(resource).orElseThrow(() -> new AssertionError(String.format("Missing resource: %s", resource)));
  }

  public @NotNull Optional<File> findResourceAsFile(@NotNull String resource) {
    return findResource(resource).map(Path::toFile);
  }

  public @NotNull File getResourceAsFile(@NotNull String resource) {
    return findResourceAsFile(resource).orElseThrow(() -> new AssertionError(String.format("Missing resource: %s", resource)));
  }

} /* ENDCLASS */
