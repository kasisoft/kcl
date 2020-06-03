package com.kasisoft.libs.common;

import static org.testng.Assert.fail;

import javax.validation.constraints.NotNull;

import java.util.Optional;

import java.net.URL;

import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.File;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AbstractTestCase {

  String rootFolder;
  
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
  }
  
  public @NotNull Optional<Path> findRootFolder() {
    URL url = getClass().getClassLoader().getResource(rootFolder);
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
