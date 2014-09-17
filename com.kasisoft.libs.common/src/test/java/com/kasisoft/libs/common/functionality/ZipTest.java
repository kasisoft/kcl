package com.kasisoft.libs.common.functionality;

import org.testng.annotations.*;

import org.testng.*;

import java.util.*;
import java.util.List;

import java.awt.*;

/**
 * A test for the interface 'Zip'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class ZipTest {

  private static final String KEY_RED     = "red";
  private static final String KEY_GREEN   = "green";
  private static final String KEY_BLUE    = "blue";
  private static final String KEY_NAME    = "name";
  private static final String KEY_SIZE    = "size";
  private static final String KEY_STYLE   = "style";
  
  private Zip<Color,Font,Map<String,Object>>   zipfunction;
  
  @BeforeTest
  public void setup() {
    zipfunction = new StyleMap();
  }
  
  @DataProvider(name="createData")
  public Object[][] createData() {
    
    List<Color> colors = new ArrayList<Color>();
    List<Font>  fonts  = new ArrayList<Font>();
    
    colors.add( Color.red  );
    colors.add( Color.blue );
    
    fonts.add( Font.decode( String.format( "%s-%s", Font.SANS_SERIF, "italic-13" ) ) );
    fonts.add( Font.decode( String.format( "%s-%s", Font.MONOSPACED, "italic-13" ) ) );
    
    return new Object[][] {
      { colors, fonts },
    };
  }
  
  @DataProvider(name="invalidUses")
  public Object[][] invalidUses() {
    Object[] firstrecord = createData()[0];
    return new Object[][] {
      { null        , firstrecord[0]  , firstrecord[1]  },
      { zipfunction , null            , firstrecord[1]  },
      { zipfunction , firstrecord[0]  , null            },
    };
  }
  
  @Test(dataProvider="invalidUses",expectedExceptions={NullPointerException.class}, groups="all")
  public void invalidUse( Zip<Color,Font,Map<String,Object>> zipfunction, List<Color> colors, List<Font> fonts ) {
    FuFunctions.zip( zipfunction, colors, fonts );
  }
  
  @Test(dataProvider="createData", groups="all")
  public void verifyStyles( List<Color> colors, List<Font> fonts ) {
    List<Map<String, Object>> stylemaps = FuFunctions.zip( zipfunction, colors, fonts );
    Assert.assertNotNull( stylemaps );
    Assert.assertEquals( stylemaps.size(), colors.size() );
    for( int i = 0; i < colors.size(); i++ ) {
      Map<String,Object> map    = stylemaps.get(i);
      Color              color  = toColor ( map );
      Font               font   = toFont  ( map );
      Assert.assertEquals( color , colors . get(i) );
      Assert.assertEquals( font  , fonts  . get(i) );
    }
  }
  
  private Color toColor( Map<String,Object> map ) {
    Integer red     = (Integer) map.get( KEY_RED    );
    Integer green   = (Integer) map.get( KEY_GREEN  );
    Integer blue    = (Integer) map.get( KEY_BLUE   );
    return new Color( red.intValue(), green.intValue(), blue.intValue() );
  }
  
  private Font toFont( Map<String,Object> map ) {
    String  name  = (String)  map.get( KEY_NAME );
    String  style = (String)  map.get( KEY_STYLE );
    Integer size  = (Integer) map.get( KEY_SIZE );
    return Font.decode( String.format( "%s-%s-%d", name, style, size ) );
  }
  
  /**
   * Combines a font and a color to a single map.
   */
  private static final class StyleMap implements Zip<Color,Font,Map<String,Object>> {

    @Override
    public Map<String, Object> zip( Color color, Font font ) {

      StringBuilder style = new StringBuilder();
      if( (font.getStyle() & Font.ITALIC) != 0 ) {
        style.append( "italic" );
      }
      if( (font.getStyle() & Font.BOLD) != 0 ) {
        if( style.length() > 0 ) {
          style.append( "-" );
        }
        style.append( "bold" );
      }
      if( style.length() == 0 ) {
        style.append( "plain" );
      }

      Map result = new Hashtable<String,Object>();
      result.put( KEY_RED       , Integer.valueOf( color.getRed   () ) );
      result.put( KEY_GREEN     , Integer.valueOf( color.getGreen () ) );
      result.put( KEY_BLUE      , Integer.valueOf( color.getBlue  () ) );
      result.put( KEY_NAME      , font.getName()                       );
      result.put( KEY_STYLE     , style.toString()                     );
      result.put( KEY_SIZE      , Integer.valueOf( font.getSize()    ) );
      return result;
      
    }
    
  } /* ENDCLASS */
  
} /* ENDCLASS */
