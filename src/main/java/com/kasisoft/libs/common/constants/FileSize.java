package com.kasisoft.libs.common.constants;

/**
 * A collection of file sizes. Calculation won't work on {@link #TerraByte} due to value limits.
 *
 * @author daniel.kasmeroglu@kasisoft.net
 */
public enum FileSize {

  Byte      (                 1 ,                  1 , "B"  ,   "B"),
  KiloByte  (              1000 ,               1024 , "KB" , "KiB"),
  MegaByte  (       1000 * 1000 ,        1024 * 1024 , "MB" , "MiB"),
  GigaByte  (1000 * 1000 * 1000 , 1024 * 1024 * 1024 , "GB" , "GiB"),
  TerraByte (                0L ,                 0L , "TB" , "TiB");

  private long     humanSize;
  private long     computerSize;
  private String   humanUnit;
  private String   computerUnit;

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
    switch (this) {
    case Byte     : return KiloByte;
    case KiloByte : return MegaByte;
    case MegaByte : return GigaByte;
    case GigaByte : return TerraByte;
    default       : return null;
    }
  }

  public FileSize previous() {
    switch (this) {
    case KiloByte  : return Byte;
    case MegaByte  : return KiloByte;
    case GigaByte  : return MegaByte;
    case TerraByte : return GigaByte;
    default        : return null;
    }
  }

} /* ENDENUM */
