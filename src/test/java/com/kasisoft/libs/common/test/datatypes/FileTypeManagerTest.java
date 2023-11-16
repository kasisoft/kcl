package com.kasisoft.libs.common.test.datatypes;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.provider.*;

import org.junit.jupiter.params.*;

import com.kasisoft.libs.common.test.*;

import com.kasisoft.libs.common.datatypes.*;

import java.util.stream.*;

import java.nio.file.*;

/**
 * Tests for the class 'FileTypeManager'.
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class FileTypeManagerTest {

    private static final TestResources TEST_RESOURCES = TestResources.createTestResources(FileTypeManagerTest.class);

    private static FileTypeManager     MANAGER        = new FileTypeManager();

    public static Stream<Arguments> identifyData() {
        return Stream.of(Arguments.of("file0", "application/gzip"), Arguments.of("file1", "application/pdf"), Arguments.of("file2", "image/png"), Arguments.of("file3", "image/bmp"), Arguments.of("file4", "image/gif"), Arguments.of("file5", "image/jpeg"), Arguments.of("file6", "application/x-bzip"), Arguments.of("file7", "application/zip"), Arguments.of("file8", "application/x-7z-compressed"), Arguments.of("file9", "application/java-vm"));
    }

    @ParameterizedTest
    @MethodSource("identifyData")
    public void identify(String resource, String mime) throws Exception {

        var file    = TEST_RESOURCES.getResource(resource);
        var content = Files.readAllBytes(file);
        assertNotNull(content);

        var filetype = MANAGER.identify(content);
        if (mime == null) {
            assertNull(filetype);
        } else {
            assertNotNull(filetype);
            assertThat(filetype.getContentType().getMimeType(), is(mime));
        }

    }

} /* ENDCLASS */
