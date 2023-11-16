package com.kasisoft.libs.common.test;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.constraints.*;

import java.util.concurrent.*;

import java.util.*;

import java.nio.file.*;

import java.io.*;

/**
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class TestResources {

    private static Path                         tempDir;

    private static Map<Class<?>, TestResources> testResources = new ConcurrentHashMap<>();

    static {
        try {
            tempDir = Files.createTempDirectory("test");
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    public static TestResources createTestResources(Class<?> cls) {
        return testResources.computeIfAbsent(cls, TestResources::new);
    }

    private String rootFolder;

    private Path   tempFolder;

    private TestResources(Class<?> cls) {
        rootFolder = cls.getSimpleName();
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
        return findRootFolder().orElseThrow(() -> new AssertionError("Missing resource: %s".formatted(rootFolder)));
    }

    public @NotNull Optional<Path> findResource(@NotNull String resource) {
        return findRootFolder().map($ -> $.resolve(resource));
    }

    public @NotNull Path getResource(@NotNull String resource) {
        return findResource(resource).orElseThrow(() -> new AssertionError("Missing resource: %s".formatted(resource)));
    }

    public @NotNull Path getDir(@NotNull String resource) {
        var result = getResource(resource);
        assertTrue(Files.isDirectory(result));
        return result;
    }

    public @NotNull Path getFile(@NotNull String resource) {
        var result = getResource(resource);
        assertTrue(Files.isRegularFile(result));
        return result;
    }

    public @NotNull Optional<File> findResourceAsFile(@NotNull String resource) {
        return findResource(resource).map(Path::toFile);
    }

    public @NotNull File getResourceAsFile(@NotNull String resource) {
        return findResourceAsFile(resource).orElseThrow(() -> new AssertionError("Missing resource: %s".formatted(resource)));
    }

} /* ENDCLASS */
