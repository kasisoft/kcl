package com.kasisoft.libs.common.old.spi;

import com.kasisoft.libs.common.old.wrapper.WrapperFactory;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import java.util.stream.Collectors;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ServiceLoader;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * Like SPILoader with the difference that it supports multiple service types.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MultiSPILoader {

  Map<Class, Predicate>   filter;
  Map<Class, Function>    postprocessor;
  Map<String, Object>     configuration;
  
  private MultiSPILoader() {
    filter        = null;
    postprocessor = null;
    configuration = null;
  }
  
  /**
   * Small helper which is used to load all SPI services currently available.
   * 
   * @param servicetype   The desired service type. Not <code>null</code>
   * 
   * @return   A list with all SPI services currently available. Not <code>null</code>.
   * 
   * @throws FailureException in case one SPI could not be configured properly.
   */
  public <T> List<T> loadServices( Class<T> servicetype ) {
    List<T> result = new ArrayList<>();
    ServiceLoader.load( servicetype ).forEach( result::add );
    if( configuration != null ) {
      result.parallelStream()
        .filter( $ -> $ instanceof Configurable )
        .forEach( s -> ((Configurable) s).configure( configuration ) );
    }
    if( (postprocessor != null) && postprocessor.containsKey( servicetype ) ) {
      Function<T,T> transformer = postprocessor.get( servicetype ); 
      result = result.parallelStream()
        .map( transformer::apply )
        .collect( Collectors.toList() );
    }
    if( (filter != null) && filter.containsKey( servicetype ) ) {
      Predicate<T> test = filter.get( servicetype );
      result = result.parallelStream()
        .filter( test )
        .collect( Collectors.toList() );
    }
    return result;
  }
  
  public static MultiSPILoaderBuilder builder() {
    return new MultiSPILoaderBuilder();
  }

  public static class MultiSPILoaderBuilder {
    
    private MultiSPILoader   instance = new MultiSPILoader();
    
    public <T> MultiSPILoaderBuilder filter( Class<T> spiType, Predicate<T> test ) {
      if( test != null ) {
        if( instance.filter == null ) {
          instance.filter = new Hashtable<>();
        }
        instance.filter.put( spiType, test );
      }
      return this;
    }

    public <T> MultiSPILoaderBuilder postProcessor( Class<T> spiType, Consumer<T> consumer ) {
      if( consumer != null ) {
        if( instance.postprocessor == null ) {
          instance.postprocessor = new Hashtable<>();
        }
        instance.postprocessor.put( spiType, WrapperFactory.toFunction( consumer ) );
      }
      return this;
    }

    public <T> MultiSPILoaderBuilder postProcessor( Class<T> spiType, Function<T, T> transformer ) {
      if( transformer != null ) {
        if( instance.postprocessor == null ) {
          instance.postprocessor = new Hashtable<>();
        }
        instance.postprocessor.put( spiType, transformer );
      }
      return this;
    }

    public <T> MultiSPILoaderBuilder configuration( Map<String, Object> config ) {
      if( config != null ) {
        instance.configuration = config;
        if( instance.configuration.isEmpty() ) {
          instance.configuration = null;
        }
      }
      return this;
    }

    public <T> MultiSPILoaderBuilder configuration( Properties config ) {
      return configuration( WrapperFactory.toMap( config ) );
    }
    
    public MultiSPILoader build() {
      return instance;
    }
    
  } /* ENDCLASS */
  
} /* ENDCLASS */