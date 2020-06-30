package com.kasisoft.libs.common.constants;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class FileSizeTest {

  @DataProvider(name = "data_checkFileSizes")
  public Object[][] data_checkFileSizes() {
    return new Object[][] {
      {FileSize.Byte     , 4,             4L,             4L,           "4 B",            "4 B"},
      {FileSize.KiloByte , 4,         4_000L,         4_096L,       "4000 KB",       "4096 KiB"},
      {FileSize.MegaByte , 4,     4_000_000L,     4_194_304L,    "4000000 MB",    "4194304 MiB"},
      {FileSize.GigaByte , 4, 4_000_000_000L, 4_294_967_296L, "4000000000 GB", "4294967296 GiB"},
      {FileSize.TerraByte, 4,             0L,             0L,          "0 TB",          "0 TiB"},
    };
  }
  
  @Test(dataProvider = "data_checkFileSizes", groups = "all")
  public void checkFileSizes(FileSize size, int count, long humanSize, long computerSize, String humanText, String computerText) {
    assertThat(size.humanSize     (count), is(humanSize   ));
    assertThat(size.computerSize  (count), is(computerSize));
    assertThat(size.humanFormat   (count), is(humanText   ));
    assertThat(size.computerFormat(count), is(computerText));
  }
  
  @Test(groups = "all")
  public void next() {
    assertThat(FileSize.Byte.next(), is(FileSize.KiloByte));
    assertThat(FileSize.KiloByte.next(), is(FileSize.MegaByte));
    assertThat(FileSize.MegaByte.next(), is(FileSize.GigaByte));
    assertThat(FileSize.GigaByte.next(), is(FileSize.TerraByte));
    assertNull(FileSize.TerraByte.next());
  }

  @Test(groups = "all")
  public void previous() {
    assertThat(FileSize.KiloByte.previous(), is(FileSize.Byte));
    assertThat(FileSize.MegaByte.previous(), is(FileSize.KiloByte));
    assertThat(FileSize.GigaByte.previous(), is(FileSize.MegaByte));
    assertThat(FileSize.TerraByte.previous(), is(FileSize.GigaByte));
    assertNull(FileSize.Byte.previous());
  }

  @Test(groups = "all")
  public void getHumanSize() {
    assertTrue(FileSize.KiloByte.getHumanSize() > FileSize.Byte.getHumanSize());
    assertTrue(FileSize.MegaByte.getHumanSize() > FileSize.KiloByte.getHumanSize());
    assertTrue(FileSize.GigaByte.getHumanSize() > FileSize.MegaByte.getHumanSize());
  }

  @Test(groups = "all")
  public void getComputerSize() {
    assertTrue(FileSize.KiloByte.getComputerSize() > FileSize.Byte.getComputerSize());
    assertTrue(FileSize.MegaByte.getComputerSize() > FileSize.KiloByte.getComputerSize());
    assertTrue(FileSize.GigaByte.getComputerSize() > FileSize.MegaByte.getComputerSize());
  }

} /* ENDCLASS */
