package com.kasisoft.libs.common.constants;

import com.kasisoft.libs.common.annotation.*;

import jakarta.validation.constraints.*;

import java.util.*;

import java.nio.charset.*;

import java.nio.*;

/**
 * Collection of supported encodings.
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
@Specification(value = "https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/nio/charset/Charset.html", date = "14-NOV-2023")
public final class Encoding {

    public static final Encoding               ASCII;
    public static final Encoding               UTF8;
    public static final Encoding               UTF16;
    public static final Encoding               UTF16BE;
    public static final Encoding               UTF16LE;
    public static final Encoding               ISO88591;
    public static final Encoding               IBM437;

    private static final Map<String, Encoding> ENCODINGS;

    static {
        ENCODINGS = new HashMap<>();
        ASCII     = new Encoding("US-ASCII", false, null);
        UTF8      = new Encoding("UTF-8", false, ByteOrderMark.UTF8);
        UTF16     = new Encoding("UTF-16", true, null);
        UTF16BE   = new Encoding("UTF-16BE", false, ByteOrderMark.UTF16BE);
        UTF16LE   = new Encoding("UTF-16LE", false, ByteOrderMark.UTF16LE);
        ISO88591  = new Encoding("ISO-8859-1", false, null);
        IBM437    = new Encoding("IBM437", false, null);
    }

    private String        encoding;
    private boolean       bomRequired;
    private ByteOrderMark byteOrderMark;
    private Charset       charset;

    /**
     * Initializes this Encoding instance for a specific character set.
     *
     * @param key
     *            The name of the character set.
     * @param requiresbom
     *            <code>true</code> <=> Identifying this encoding requires a {@link ByteOrderMark}.
     * @param mark
     *            A {@link ByteOrderMark} which allows to identify the encoding.
     */
    public Encoding(@NotBlank String key, boolean requiresbom, ByteOrderMark mark) {
        encoding      = key;
        bomRequired   = requiresbom;
        byteOrderMark = mark;
        charset       = Charset.forName(key);
        synchronized (ENCODINGS) {
            ENCODINGS.put(key, this);
        }
    }

    public String getEncoding() {
        return encoding;
    }

    public boolean isBomRequired() {
        return bomRequired;
    }

    public ByteOrderMark getByteOrderMark() {
        return byteOrderMark;
    }

    public Charset getCharset() {
        return charset;
    }

    /**
     * Encodes the supplied text.
     *
     * @param text
     *            The text that has to be encoded.
     * @return The data which has to be encoded.
     */
    @NotNull
    public byte[] encode(@NotNull String text) {
        var buffer = charset.encode(CharBuffer.wrap(text));
        var result = new byte[buffer.limit()];
        buffer.get(result);
        return result;
    }

    /**
     * Decodes the supplied data using this encoding.
     *
     * @param data
     *            The data providing the content.
     * @return The decoded String.
     */
    @NotNull
    public String decode(@NotNull byte[] data) {
        return charset.decode(ByteBuffer.wrap(data)).toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Encoding enc) {
            return encoding.equals(enc.encoding);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return encoding.hashCode();
    }

    @Override
    public String toString() {
        return encoding;
    }

    public static Encoding[] values() {
        synchronized (ENCODINGS) {
            return ENCODINGS.values().toArray(new Encoding[ENCODINGS.size()]);
        }
    }

    /**
     * This helper function identifies the encoding value which corresponds to the supplied name. Be
     * aware that this enumeration only supports the <b>required</b> encodings.
     *
     * @param name
     *            The name of the encoding which has to be identified. Case sensitivity doesn't matter
     *            here.
     * @return The encoding value if available.
     */
    @NotNull
    public static Optional<Encoding> findByName(@NotNull String name) {
        synchronized (ENCODINGS) {
            return Optional.ofNullable(ENCODINGS.get(name));
        }
    }

    @NotNull
    public static Encoding getEncoding(Encoding encoding) {
        if (encoding == null) {
            return Encoding.UTF8;
        }
        return encoding;
    }

} /* ENDCLASS */
