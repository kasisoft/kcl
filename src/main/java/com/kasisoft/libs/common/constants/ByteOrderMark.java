package com.kasisoft.libs.common.constants;

import com.kasisoft.libs.common.utils.*;

import jakarta.validation.constraints.*;

import java.util.*;

/**
 * Constants the different byte order marks.
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
public enum ByteOrderMark {

    UTF8(new byte[] {(byte) 0xef, (byte) 0xbb, (byte) 0xbf}),
    UTF16BE(new byte[] {(byte) 0xfe, (byte) 0xff}),
    UTF16LE(new byte[] {(byte) 0xff, (byte) 0xfe}),
    UTF32BE(new byte[] {(byte) 0x00, (byte) 0x00, (byte) 0xfe, (byte) 0xff}),
    UTF32LE(new byte[] {(byte) 0xff, (byte) 0xfe, (byte) 0x00, (byte) 0x00});

    private byte[] bom;

    ByteOrderMark(byte[] bom) {
        this.bom = bom;
    }

    public byte[] getBOM() {
        return bom;
    }

    /**
     * Returns <code>true</code> if the supplied data starts with this BOM.
     *
     * @param data
     *            The data to be tested.
     * @return <code>true</code> The supplied data starts with this BOM.
     */
    public boolean startsWith(@NotNull byte[] data) {
        return startsWith(data, 0);
    }

    /**
     * Returns <code>true</code> if the supplied data starts with this BOM.
     *
     * @param data
     *            The data to be tested.
     * @param offset
     *            An offset where the start has to begin. Must be a positive number.
     * @return <code>true</code> The supplied data starts with this BOM.
     */
    public boolean startsWith(@NotNull byte[] data, int offset) {
        return PrimitiveFunctions.compare(data, bom);
    }

    /**
     * Returns the ByteOrderMark located at the beginning of the supplied data.
     *
     * @param data
     *            The data to be tested.
     * @return The ByteOrderMark if it could be identified.
     */
    @NotNull
    public static  Optional<ByteOrderMark> identify(@NotNull byte[] data) {
        return identify(data, 0);
    }

    /**
     * Returns the ByteOrderMark located at a specific location of the supplied data.
     *
     * @param data
     *            The data to be tested.
     * @param offset
     *            The location where to start the test. Must be positive.
     * @return The ByteOrderMark if it could be identified.
     */
    @NotNull
    public static Optional<ByteOrderMark> identify(@NotNull byte[] data, int offset) {
        var marks = ByteOrderMark.values();
        for (var mark : marks) {
            if (mark.startsWith(data, offset)) {
                return Optional.of(mark);
            }
        }
        return Optional.empty();
    }

} /* ENDENUM */
