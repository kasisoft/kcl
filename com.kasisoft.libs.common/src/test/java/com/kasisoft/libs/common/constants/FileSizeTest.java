package com.kasisoft.libs.common.constants;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.testng.annotations.*;

/**
 * Tests for the class 'FileSize'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class FileSizeTest {

  @DataProvider(name="createData")
  public Object[][] createData() {
    return new Object[][] {
      { FileSize.Byte      , 4,             4L,             4L ,           "4 B",            "4 B" },
      { FileSize.KiloByte  , 4,         4_000L,         4_096L ,       "4000 KB",       "4096 KiB" },
      { FileSize.MegaByte  , 4,     4_000_000L,     4_194_304L ,    "4000000 MB",    "4194304 MiB" },
      { FileSize.GigaByte  , 4, 4_000_000_000L, 4_294_967_296L , "4000000000 GB", "4294967296 GiB" },
      { FileSize.TerraByte , 4,             0L,             0L ,          "0 TB",          "0 TiB" },
    };
  }
  
  @Test(dataProvider="createData", groups="all")
  public void checkFileSizes( FileSize size, int count, long humanSize, long computerSize, String humanText, String computerText ) {
    assertThat( size . humanSize      ( count ), is ( humanSize    ) );
    assertThat( size . computerSize   ( count ), is ( computerSize ) );
    assertThat( size . humanFormat    ( count ), is ( humanText    ) );
    assertThat( size . computerFormat ( count ), is ( computerText ) );
  }

} /* ENDCLASS */
