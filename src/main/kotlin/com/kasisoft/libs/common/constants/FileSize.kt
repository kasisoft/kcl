package com.kasisoft.libs.common.constants

/**
 * A collection of file sizes. Calculation won't work on {@link #TerraByte} due to value limits.
 *
 * @author daniel.kasmeroglu@kasisoft.net
 */
enum class FileSize (val humanSize: Long, val computerSize: Long, val humanUnit: String, val computerUnit: String) {
    
  Byte(1, 1, "B", "B"),
  KiloByte(1000, 1024, "KB", "KiB"),
  MegaByte(1000 * 1000, 1024 * 1024, "MB", "MiB"),
  GigaByte(1000 * 1000 * 1000, 1024 * 1024 * 1024, "GB", "GiB"),
  TerraByte(0L, 0L, "TB", "TiB");

  fun humanSize(count: Int) = count * humanSize
 
  fun computerSize(count: Int) = count * computerSize
  
  fun humanFormat(count: Int) = String.format("%d %s", humanSize(count), humanUnit)

  fun computerFormat(count: Int) = String.format("%d %s", computerSize(count), computerUnit)

  fun next(): FileSize? {
    when (this) {
      Byte -> return KiloByte
      KiloByte -> return MegaByte
      MegaByte -> return GigaByte
      GigaByte -> return TerraByte
      else -> return null
    }
  }

  fun previous(): FileSize? {
    when (this) {
      KiloByte -> return Byte
      MegaByte -> return KiloByte
      GigaByte -> return MegaByte
      TerraByte -> return GigaByte
      else -> return null
    }
  }
    
} /* ENDENUM */