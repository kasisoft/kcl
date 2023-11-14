package com.kasisoft.libs.common.io;

import com.kasisoft.libs.common.*;

import jakarta.validation.constraints.*;

import java.util.function.*;

import java.util.stream.*;

import java.util.*;

import java.nio.file.*;

/**
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class ListingFileWalker implements Supplier<List<String>> {

    private Path                                 source;

    private CustomFileVisitor                    fsWalker;

    private Function<Exception, FileVisitResult> errorHandler;

    private List<String>                         pathes;

    public ListingFileWalker(@NotNull Path source, boolean includeDirs) {
        this(source, includeDirs, null);
    }

    public ListingFileWalker(@NotNull Path source, boolean includeDirs, Function<Exception, FileVisitResult> errorHandler) {

        this.source       = source;
        this.fsWalker     = new CustomFileVisitor();
        this.errorHandler = errorHandler != null ? errorHandler : this::defaultErrorHandler;
        pathes            = new ArrayList<String>(50);

        fsWalker.setOnPreDirectory($ -> {
            if (includeDirs) {
                addRelativePath($, true);
            }
        });

        fsWalker.setOnFile($ -> {
            addRelativePath($, false);
        });

        fsWalker.setErrorHandler(errorHandler);

    }

    private FileVisitResult defaultErrorHandler(Exception ex) {
        throw KclException.wrap(ex);
    }

    private void addRelativePath(Path current, boolean dir) {
        String str = source.relativize(current).toString().replace('\\', '/');
        if (str.isBlank()) {
            return;
        }
        if (dir && (!str.endsWith("/"))) {
            str = str + '/';
        }
        pathes.add(str);
    }

    @Override
    public synchronized List<String> get() {
        try {
            pathes.clear();
            Files.walkFileTree(source, fsWalker);
            Collections.sort(pathes);
            return Collections.unmodifiableList(pathes);
        } catch (Exception ex) {
            errorHandler.apply(ex);
            throw KclException.wrap(ex);
        }
    }

    public synchronized List<Path> getResolved() {
        return getResolved(source);
    }

    public synchronized List<Path> getResolved(@NotNull Path dir) {
        return get().stream().map(dir::resolve).collect(Collectors.toList());
    }

} /* ENDCLASS */
