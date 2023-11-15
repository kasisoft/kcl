package com.kasisoft.libs.common.datatypes;

import com.kasisoft.libs.common.io.*;

import jakarta.validation.constraints.*;

import java.util.*;

/**
 * Management for file type recognizers.
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class FileTypeManager {

    private List<FileType> filetypes;

    private int            maxspace;

    /**
     * Initializes this management type while looking for all SPI declarations.
     */
    public FileTypeManager() {
        filetypes = new ArrayList<FileType>();
        ServiceLoader.load(FileType.class).forEach(filetypes::add);
        if (!filetypes.isEmpty()) {
            Collections.sort(filetypes, ($a, $b) -> $a.getMinSize() - $b.getMinSize());
            maxspace = filetypes.get(0).getMinSize();
        } else {
            maxspace = 0;
        }
    }

    /**
     * Returns a list with all known FileType instances.
     *
     * @return A list with all known FileType instances.
     */
    @NotNull
    public FileType[] getFileTypes() {
        return filetypes.toArray(new FileType[filetypes.size()]);
    }

    /**
     * Identifies the FileType for the supplied resource.
     *
     * @param input
     *            The input type which may be one of the following types:
     *            <ul>
     *            <li>String</li>
     *            <li>Path</li>
     *            <li>URL</li>
     *            <li>URI</li>
     *            <li>InputStream</li>
     *            </ul>
     * @return The FileType if it could be identified.
     */
    public <T> FileType identify(@NotNull T input) {
        var data = IoFunctions.genericLoadBytes(input, maxspace);
        return data.map(this::identify).orElse(null);
    }

    /**
     * Identifies the FileType for the supplied data.
     *
     * @param data
     *            The data of the input which type shall be identified.
     * @return The FileType if it could be identified.
     */
    public FileType identify(@NotNull byte[] data) {
        for (var i = 0; i < filetypes.size(); i++) {
            if (filetypes.get(i).test(data)) {
                return filetypes.get(i);
            }
        }
        return null;
    }

} /* ENDCLASS */
