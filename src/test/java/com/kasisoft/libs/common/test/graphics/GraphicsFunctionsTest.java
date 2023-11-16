package com.kasisoft.libs.common.test.graphics;

import static com.kasisoft.libs.common.test.testsupport.ExtendedAsserts.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.provider.*;

import org.junit.jupiter.params.*;

import org.junit.jupiter.api.*;

import com.kasisoft.libs.common.constants.*;

import com.kasisoft.libs.common.graphics.*;

import com.kasisoft.libs.common.test.*;

import com.kasisoft.libs.common.io.*;

import com.kasisoft.libs.common.*;

import javax.swing.*;

import java.util.function.*;

import java.util.stream.*;

import java.util.*;
import java.util.List;

import java.nio.file.*;

import java.awt.*;
import java.awt.image.*;

import java.net.*;

import java.io.*;

/**
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class GraphicsFunctionsTest {

    private static final TestResources TEST_RESOURCES = TestResources.createTestResources(GraphicsFunctionsTest.class);

    private int                        idx            = 0;

    private Path getTempPath(PictureFormat fmt) {
        return TEST_RESOURCES.getTempPath("image_%d.%s".formatted(idx++, fmt.getSuffix()));
    }

    private static List<Path> getInputFiles() {
        var images = TEST_RESOURCES.getDir("images");
        return Arrays.asList(images.toFile().list()).stream().map(images::resolve).collect(Collectors.toList());
    }

    private static <R> Stream<Arguments> data_readImage__writeImages(Function<Path, R> toResource) {
        var formats    = PictureFormat.values();
        var result     = new ArrayList<Arguments>();
        var inputfiles = getInputFiles();
        for (var inputfile : inputfiles) {
            for (var format : formats) {
                if (format.isRasterFormat()) {
                    result.add(Arguments.of(toResource.apply(inputfile), format));
                }
            }
        }
        return result.stream();
    }

    private <R> void readImage_writeImage(R source, PictureFormat outformat, Function<R, BufferedImage> loader, Function<Path, R> toDest, SaveImage<R> saver) {
        var image = loader.apply(source);
        assertNotNull(image);
        var tempPath = getTempPath(outformat);
        saver.accept(toDest.apply(tempPath), image, outformat);
        assertTrue(Files.isRegularFile(tempPath));
    }

    public static Stream<Arguments> data_readImage__writeImage__Path() {
        return data_readImage__writeImages(Function.identity());
    }

    @ParameterizedTest
    @MethodSource("data_readImage__writeImage__Path")
    public void readImage__writeImagePath(Path imageFile, PictureFormat outformat) {
        readImage_writeImage(imageFile, outformat, GraphicsFunctions::readImage, Function.identity(), GraphicsFunctions::writeImage);
    }

    public static Stream<Arguments> data_readImage__writeImage__File() {
        return data_readImage__writeImages($ -> $.toFile());
    }

    @ParameterizedTest
    @MethodSource("data_readImage__writeImage__File")
    public void readImage__writeImageFile(File imageFile, PictureFormat outformat) {
        readImage_writeImage(imageFile, outformat, GraphicsFunctions::readImage, $ -> $.toFile(), GraphicsFunctions::writeImage);
    }

    public static Stream<Arguments> data_readImage__writeImage__URI() {
        return data_readImage__writeImages($ -> $.toFile().toURI());
    }

    @ParameterizedTest
    @MethodSource("data_readImage__writeImage__URI")
    public void readImage__writeImageURI(URI imageFile, PictureFormat outformat) {
        readImage_writeImage(imageFile, outformat, GraphicsFunctions::readImage, $ -> $.toFile().toURI(), GraphicsFunctions::writeImage);
    }

    private JComponent createJComponent() {
        var frame = new JFrame();
        var panel = new JPanel();
        frame.setLayout(null);
        frame.getContentPane().add(panel);
        frame.setVisible(true);
        panel.setSize(200, 200);
        panel.getGraphics().drawOval(100, 100, 30, 30);
        frame.setVisible(false);
        return panel;
    }

    private <R> void writeImage__JComponent(PictureFormat outformat, Function<Path, R> toDest, SaveImageByComponent<R> saver) {
        var tempPath = getTempPath(outformat);
        saver.accept(toDest.apply(tempPath), createJComponent(), outformat);
        assertTrue(Files.isRegularFile(tempPath));
    }

    @Test
    public void writeImage__JComponent__Path() {
        for (var fmt : PictureFormat.rasterFormatValues()) {
            writeImage__JComponent(fmt, Function.identity(), GraphicsFunctions::writeImage);
        }
    }

    @Test
    public void writeImage__JComponent__File() {
        for (var fmt : PictureFormat.rasterFormatValues()) {
            writeImage__JComponent(fmt, $ -> $.toFile(), GraphicsFunctions::writeImage);
        }
    }

    @Test
    public void writeImage__JComponent__URI() {
        for (var fmt : PictureFormat.rasterFormatValues()) {
            writeImage__JComponent(fmt, $ -> $.toFile().toURI(), GraphicsFunctions::writeImage);
        }
    }

    @Test
    public void writeImage__JComponent__UnsupportedFormat() {
        assertThrows(KclException.class, () -> {
            writeImage__JComponent(PictureFormat.Svg, Function.identity(), GraphicsFunctions::writeImage);
        });
    }

    @Test
    public void writeImage__JComponent__OutputStream() {
        for (var fmt : PictureFormat.rasterFormatValues()) {
            var tempPath = getTempPath(fmt);
            IoSupportFunctions.forOutputStreamDo(tempPath, $ -> GraphicsFunctions.writeImage($, createJComponent(), fmt));
            assertTrue(Files.isRegularFile(tempPath));
        }
    }

    @Test
    public void writeImage__JComponent__OutputStream__UnsupportedFormat() {
        assertThrows(KclException.class, () -> {
            var tempPath = TEST_RESOURCES.getTempPath("image_%d.%s".formatted(idx++, PictureFormat.Svg.getSuffix()));
            IoSupportFunctions.forOutputStreamDo(tempPath, $ -> GraphicsFunctions.writeImage($, createJComponent(), PictureFormat.Svg));
            assertTrue(Files.isRegularFile(tempPath));
        });
    }

    @Test
    public void applyWatermark() {

        var img1 = GraphicsFunctions.readImage(TEST_RESOURCES.getResource("images/sample.bmp"));
        var mark = GraphicsFunctions.readImage(TEST_RESOURCES.getResource("mark.png"));
        GraphicsFunctions.applyWatermark(img1, mark, 0.4f);

        var expected = GraphicsFunctions.readImage(TEST_RESOURCES.getResource("watermarked-1.png"));
        assertImages(img1, expected);

    }

    @Test
    public void applyWatermark__Padding() {

        var img1 = GraphicsFunctions.readImage(TEST_RESOURCES.getResource("images/sample.bmp"));
        var mark = GraphicsFunctions.readImage(TEST_RESOURCES.getResource("mark.png"));
        GraphicsFunctions.applyWatermark(img1, mark, 0.4f, 20);

        var expected = GraphicsFunctions.readImage(TEST_RESOURCES.getResource("watermarked-5.png"));
        assertImages(img1, expected);

    }

    @Test
    public void applyWatermark__Positioning() {

        var img1 = GraphicsFunctions.readImage(TEST_RESOURCES.getResource("images/sample.bmp"));
        var mark = GraphicsFunctions.readImage(TEST_RESOURCES.getResource("mark.png"));
        GraphicsFunctions.applyWatermark(img1, mark, 0.4f, Alignment.Right, Alignment.Bottom);

        var expected = GraphicsFunctions.readImage(TEST_RESOURCES.getResource("watermarked-2.png"));
        assertImages(img1, expected);

    }

    @Test
    public void applyWatermark__PositioningCentered() {

        var img1 = GraphicsFunctions.readImage(TEST_RESOURCES.getResource("images/sample.bmp"));
        var mark = GraphicsFunctions.readImage(TEST_RESOURCES.getResource("mark.png"));
        GraphicsFunctions.applyWatermark(img1, mark, 0.4f, Alignment.Center, Alignment.Middle);

        var expected = GraphicsFunctions.readImage(TEST_RESOURCES.getResource("watermarked-4.png"));
        assertImages(img1, expected);

    }

    @Test
    public void applyWatermark__SimpleAlignment1() {

        var img1 = GraphicsFunctions.readImage(TEST_RESOURCES.getResource("images/sample.bmp"));
        var mark = GraphicsFunctions.readImage(TEST_RESOURCES.getResource("mark.png"));
        GraphicsFunctions.applyWatermark(img1, mark, 0.4f, Alignment.Center, 0);

        var expected = GraphicsFunctions.readImage(TEST_RESOURCES.getResource("watermarked-4.png"));
        assertImages(img1, expected);

    }

    @Test
    public void applyWatermark__SimpleAlignment2() {

        var img1 = GraphicsFunctions.readImage(TEST_RESOURCES.getResource("images/sample.bmp"));
        var mark = GraphicsFunctions.readImage(TEST_RESOURCES.getResource("mark.png"));
        GraphicsFunctions.applyWatermark(img1, mark, 0.4f, Alignment.Center);

        var expected = GraphicsFunctions.readImage(TEST_RESOURCES.getResource("watermarked-4.png"));
        assertImages(img1, expected);

    }

    @Test
    public void applyWatermark__LargerThanOriginWillBeScaledDown() {

        var img1 = GraphicsFunctions.readImage(TEST_RESOURCES.getResource("mark.png"));
        var mark = GraphicsFunctions.readImage(TEST_RESOURCES.getResource("images/sample.bmp"));
        GraphicsFunctions.applyWatermark(img1, mark, 0.4f, Alignment.Right, Alignment.Bottom);

        var expected = GraphicsFunctions.readImage(TEST_RESOURCES.getResource("watermarked-3.png"));
        assertImages(img1, expected);

    }

    @Test
    public void scaleImage() {
        var img1     = GraphicsFunctions.readImage(TEST_RESOURCES.getResource("images/sample.bmp"));
        var scaled   = GraphicsFunctions.scaleImage(img1, img1.getWidth() * 2, img1.getHeight() * 2);
        var expected = GraphicsFunctions.readImage(TEST_RESOURCES.getResource("scaled-1.png"));
        assertImages(scaled, expected);
    }

    @Test
    public void scaleImage__Image() {
        var img1     = GraphicsFunctions.readImage(TEST_RESOURCES.getResource("images/sample.bmp"));
        var scaled   = GraphicsFunctions.scaleImage((Image) img1, img1.getWidth() * 2, img1.getHeight() * 2);
        var expected = GraphicsFunctions.readImage(TEST_RESOURCES.getResource("scaled-1.png"));
        assertImages(scaled, expected);
    }

    @Test
    public void readImage__InputStream() {
        var inputfiles = getInputFiles();
        for (var file : inputfiles) {
            var image = IoSupportFunctions.forInputStream(file, GraphicsFunctions::readImage);
            assertNotNull(image);
        }
    }

    @SuppressWarnings("deprecation")
    @Test
    public void readImage__URL() throws Exception {
        var inputfiles = getInputFiles();
        for (var file : inputfiles) {
            var image = GraphicsFunctions.readImage(file.toFile().toURL());
            assertNotNull(image);
        }
    }

    @FunctionalInterface
    private static interface SaveImage<T> {

        void accept(T source, BufferedImage image, PictureFormat format);

    } /* ENDINTERFACE */

    @FunctionalInterface
    private static interface SaveImageByComponent<T> {

        void accept(T source, JComponent component, PictureFormat format);

    } /* ENDINTERFACE */

} /* ENDCLASS */
