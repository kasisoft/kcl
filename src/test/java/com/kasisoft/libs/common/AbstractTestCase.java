package com.kasisoft.libs.common;

import static org.testng.Assert.fail;

import javax.validation.constraints.NotNull;

import java.util.Optional;

import java.net.URL;

import java.nio.file.Path;
import java.nio.file.Paths;

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
  
  public @NotNull Optional<Path> findResource(@NotNull String resource) {
    URL url = getClass().getClassLoader().getResource(resource);
    if (url == null) {
      return Optional.empty();
    }
    try {
      Path result = Paths.get(url.toURI());
      if (!result.toFile().isFile()) {
        return Optional.empty();
      }
      return Optional.of(result);
    } catch (Exception ex) {
      fail(ex.getLocalizedMessage());
      return null;
    }
  }
  
  private @NotNull Path getResource(@NotNull String resource) {
    return findResource(resource).orElseThrow(() -> new AssertionError(String.format("Missing resource: %s", resource)));
  }
  
} /* ENDCLASS */
