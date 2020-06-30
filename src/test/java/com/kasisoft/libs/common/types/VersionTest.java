package com.kasisoft.libs.common.types;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.kasisoft.libs.common.KclException;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class VersionTest {

  @DataProvider(name = "data_failingVersions")
  public Object[][] data_failingVersions() {
    return new Object[][] {
      {null              , Boolean.TRUE },
      {"1"               , Boolean.TRUE },
      {"1.1"             , Boolean.TRUE },
      {"1.1.1"           , Boolean.TRUE },
      {"1.1.qualifier"   , Boolean.TRUE },
      {"1q.1.1.qualifier", Boolean.TRUE },
      {"1.1q.1.qualifier", Boolean.TRUE },
      {"1.1.1q.qualifier", Boolean.TRUE },
      {"1.1.qualifier"   , Boolean.FALSE},
    };
  }
  
  @Test(dataProvider = "data_failingVersions", expectedExceptions = KclException.class, groups = "all")
  public void failingVersions(String version, boolean hasqualifier) throws Exception {
    new Version(version, true, hasqualifier);
  }

  @DataProvider(name = "data_versions")
  public Object[][] data_versions() {
    return new Object[][] {
      {"1.1"            , Boolean.FALSE , Boolean.FALSE , new Version(1, 1)},
      {"1.1.1"          , Boolean.TRUE  , Boolean.FALSE , new Version(1, 1, 1)},
      {"1.1.qualifier"  , Boolean.FALSE , Boolean.TRUE  , new Version(1, 1, "qualifier")},
      {"1.1.1.qualifier", Boolean.TRUE  , Boolean.TRUE  , new Version(1, 1, 1, "qualifier")},
      {"1.1.1_qualifier", Boolean.TRUE  , Boolean.TRUE  , new Version(1, 1, 1, "qualifier")},
    };
  }

  @Test(dataProvider = "data_versions", groups = "all")
  public void versions(String version, boolean hasmicro, boolean hasqualifier, Version expected) throws Exception {
    assertThat(new Version(version, hasmicro, hasqualifier), is(expected));
  }

  @DataProvider(name = "data_versionsAll")
  public Object[][] data_versionsAll() {
    return new Object[][] {
      {"1.1"            , new Version(1, 1)},
      {"1.1.1"          , new Version(1, 1, 1)},
      {"1.1.qualifier"  , new Version(1, 1, "qualifier")},
      {"1.1.1.qualifier", new Version(1, 1, 1, "qualifier")},
      {"1.1.1_qualifier", new Version(1, 1, 1, "qualifier")},
    };
  }

  @Test(dataProvider = "data_versionsAll", groups = "all")
  public void versionsAll(String version, Version expected) throws Exception {
    assertThat(new Version(version), is(expected));
  }

  @DataProvider(name = "data_sort")
  public Object[][] data_sort() throws Exception {
      
    Version[] versions  = {new Version("2.1", false, false), new Version("1.1", false, false)};
    Version[] versions1 = {new Version("1.1", false, false), new Version("2.1", false, false)};
    Version[] versions2 = {new Version("1.2", false, false), new Version("1.1", false, false)};
    Version[] versions3 = {new Version("1.1", false, false), new Version("1.2", false, false) };
    Version[] versions4 = {new Version("1.1.2", true, false), new Version("1.1.1", true, false)};
    Version[] versions5 = {new Version("1.1.1", true, false), new Version("1.1.2", true, false)};
    Version[] versions6 = {new Version("1.1.1.zz", true, true), new Version("1.1.1.aa", true, true)};
    Version[] versions7 = { new Version("1.1.1.aa", true, true), new Version("1.1.1.zz", true, true)};
    Version[] versions8 = {new Version("1.1.1_zz", true, true), new Version("1.1.1.aa", true, true)};
    Version[] versions9 = {new Version("1.1.1_aa", true, true), new Version("1.1.1.zz", true, true)};
    return new Object[][] {
      { 
        Arrays.asList(versions), 
        Arrays.asList(versions1) 
      },
      { 
        Arrays.asList(versions2), 
        Arrays.asList(versions3) 
      },
      { 
        Arrays.asList(versions4), 
        Arrays.asList(versions5) 
      },
      { 
        Arrays.asList(versions6), 
        Arrays.asList(versions7) 
      },
      { 
        Arrays.asList(versions8), 
        Arrays.asList(versions9) 
      },
    };
      
  }
  
  @Test(dataProvider = "data_sort", groups = "all")
  public void sort(List<Version> versions, List<Version> expected) {
    Collections.sort(versions);
    assertThat(versions, is( expected));
  }
  
} /* ENDCLASS */
