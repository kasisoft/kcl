package com.kasisoft.libs.common.constants;

/**
 * A collection of file sizes. Calculation won't work on {@link #TerraByte} due to value limits.
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
public enum FileSize {

    Byte(1L, 1, "B", "B"),
    KiloByte(1000L, 1024, "KB", "KiB"),
    MegaByte(1000L * 1000, 1024L * 1024, "MB", "MiB"),
    GigaByte(1000L * 1000 * 1000, 1024L * 1024 * 1024, "GB", "GiB"),
    TerraByte(0L, 0L, "TB", "TiB");

    private long   humanSize;
    private long   computerSize;
    private String humanUnit;
    private String computerUnit;

    FileSize(long human, long computer, String humanU, String computerU) {
        humanSize    = human;
        computerSize = computer;
        humanUnit    = humanU;
        computerUnit = computerU;
    }

    public String getHumanUnit() {
        return humanUnit;
    }

    public String getComputerUnit() {
        return computerUnit;
    }

    public long getHumanSize() {
        return humanSize;
    }

    public long getComputerSize() {
        return computerSize;
    }

    public long humanSize(int count) {
        return count * humanSize;
    }

    public long computerSize(int count) {
        return count * computerSize;
    }

    public String humanFormat(int count) {
        return "%d %s".formatted(humanSize(count), humanUnit);
    }

    public String computerFormat(int count) {
        return "%d %s".formatted(computerSize(count), computerUnit);
    }

    public FileSize next() {
        return switch (this) {
            case Byte -> KiloByte;
            case KiloByte -> MegaByte;
            case MegaByte -> GigaByte;
            case GigaByte -> TerraByte;
            default -> null;
        };
    }

    public FileSize previous() {
        return switch (this) {
            case KiloByte -> Byte;
            case MegaByte -> KiloByte;
            case GigaByte -> MegaByte;
            case TerraByte -> GigaByte;
            default -> null;
        };
    }

} /* ENDENUM */
