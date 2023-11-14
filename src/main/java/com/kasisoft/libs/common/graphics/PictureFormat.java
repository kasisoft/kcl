package com.kasisoft.libs.common.graphics;

import com.kasisoft.libs.common.constants.*;

import jakarta.validation.constraints.*;

import java.util.function.*;

import java.util.*;

/**
 * Collection of constants used to identify image formats.
 *
 * @author daniel.kasmeroglu@kasisoft.net
 */
public enum PictureFormat implements Predicate<String> {

    Bmp(true, MimeType.Bitmap, "bmp", Arrays.asList("bmp")), Eps(false, MimeType.EncapsulatedPostscript, null, Arrays.asList("eps")), Gif(true, MimeType.Gif, "gif", Arrays.asList("gif")), Jpeg(true, MimeType.Jpeg, "jpg", Arrays.asList("jpg", "jpeg")), Png(true, MimeType.Png, "png", Arrays.asList("png")), Ps(false, MimeType.Postscript, null, Arrays.asList("ps")), Svg(false, MimeType.Svg, null, Arrays.asList("svg"));

    private boolean      rasterFormat;

    private MimeType     mimeType;

    private String       imageIOFormat;

    private List<String> suffices;

    PictureFormat(boolean rasterFormat, MimeType mimeType, String imageIOFormat, List<String> suffices) {
        this.rasterFormat  = rasterFormat;
        this.mimeType      = mimeType;
        this.imageIOFormat = imageIOFormat;
        this.suffices      = suffices;
    }

    public boolean isRasterFormat() {
        return rasterFormat;
    }

    public MimeType getMimeType() {
        return mimeType;
    }

    public String getImageIOFormat() {
        return imageIOFormat;
    }

    public List<String> getSuffices() {
        return suffices;
    }

    @Override
    public boolean test(String t) {
        if (t != null) {
            var lidx = t.lastIndexOf('.');
            if (lidx != -1) {
                var suffix = t.substring(lidx + 1);
                return suffices.contains(suffix);
            }
        }
        return false;
    }

    public @NotNull String getSuffix() {
        return suffices.get(0);
    }

    public static PictureFormat[] rasterFormatValues() {
        return new PictureFormat[] {Bmp, Gif, Jpeg, Png};
    }

} /* ENDENUM */
