package com.kasisoft.libs.common.spi;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.testng.annotations.Test;

import java.util.function.Function;

import java.util.HashMap;
import java.util.Properties;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class SPIServiceTest {

  @Test
  public void defaultSettings() {
    
    var loader      = SPILoader.builder().build();
    
    var services    = loader.loadServices(SPIService.class);
    
    assertThat(services.get(0).getString(), is("service-1"));
    assertThat(services.get(1).getString(), is("service-2"));
    assertThat(services.get(2).getString(), is("service-3"));
    
  }

  @Test
  public void configured__WithProperties() {
    
    var properties  = new Properties();
    properties.setProperty(SPIService2.KEY_NAME, "bobo");
    
    var loader      = SPILoader.builder()
      .configuration(properties)
      .build();
    
    var services    = loader.loadServices(SPIService.class);
    
    assertThat(services.get(0).getString(), is("service-1"));
    assertThat(services.get(1).getString(), is("bobo"));
    assertThat(services.get(2).getString(), is("service-3"));
    
  }

  @Test
  public void configured__WithMap() {
    
    var map  = new HashMap<String, String>();
    map.put(SPIService2.KEY_NAME, "bobo");
    
    var loader      = SPILoader.builder()
      .configuration(map)
      .build();
    
    var services    = loader.loadServices(SPIService.class);
    
    assertThat(services.get(0).getString(), is("service-1"));
    assertThat(services.get(1).getString(), is("bobo"));
    assertThat(services.get(2).getString(), is("service-3"));
    
  }

  @Test
  public void configured__WithFunctionPostProcessor() {
    
    Function<SPIService, SPIService> converter = $spi -> {
      if ($spi instanceof SPIService3) {
        return new SPIService4();
      } else {
        return $spi;
      }
    };
    
    var loader      = SPILoader.builder()
      .postProcessor(SPIService.class, converter)
      .build();
    
    var services    = loader.loadServices(SPIService.class);
    
    assertThat(services.get(0).getString(), is("service-1"));
    assertThat(services.get(1).getString(), is("service-2"));
    assertThat(services.get(2).getString(), is("service-4"));
    
  }

} /* ENDCLASS */
