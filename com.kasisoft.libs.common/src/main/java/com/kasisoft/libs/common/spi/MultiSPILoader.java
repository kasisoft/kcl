package com.kasisoft.libs.common.spi;

import com.kasisoft.libs.common.base.*;

import com.kasisoft.libs.common.wrapper.*;

import lombok.experimental.*;

import lombok.*;

import java.util.function.*;

import java.util.stream.*;

import java.util.*;

/**
 * Like SPILoader with the difference that it supports to multiple service types.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MultiSPILoader {

  Map<Class,Predicate>   filter;
  Map<Class,Function>    postprocessor;
  Map<String,Object>     configuration;
  
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
  public <T> List<T> loadServices( Class<T> servicetype ) throws FailureException {
    List<T> result = new ArrayList<>();
    ServiceLoader.load( servicetype ).forEach( result::add );
    if( configuration != null ) {
      result.stream().filter( $ -> $ instanceof Configurable ).forEach( s -> ((Configurable) s).configure( configuration ) );
    }
    if( (postprocessor != null) && postprocessor.containsKey( servicetype ) ) {
      Function<T,T> transformer = postprocessor.get( servicetype ); 
      result = result.stream().map( transformer::apply ).collect( Collectors.toList() );
    }
    if( (filter != null) && filter.containsKey( servicetype ) ) {
      Predicate<T> test = filter.get( servicetype );
      result = result.stream().filter( test ).collect( Collectors.toList() );
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

    public <T> MultiSPILoaderBuilder postProcessor( Class<T> spiType, Function<T,T> transformer ) {
      if( transformer != null ) {
        if( instance.postprocessor == null ) {
          instance.postprocessor = new Hashtable<>();
        }
        instance.postprocessor.put( spiType, transformer );
      }
      return this;
    }

    public <T> MultiSPILoaderBuilder configuration( Map<String,Object> config ) {
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
