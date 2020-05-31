package com.kasisoft.libs.common.old.spi;

import com.kasisoft.libs.common.KclException;
import com.kasisoft.libs.common.old.wrapper.WrapperFactory;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import java.util.stream.Collectors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ServiceLoader;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SPILoader<T> {

  Predicate<T>          filter;
  Function<T, T>        postprocessor;
  Class<T>              clazz;
  Map<String, Object>   configuration;
  
  private SPILoader() {
    filter        = null;
    postprocessor = null;
    configuration = null;
    clazz         = null;
  }
  
  /**
   * Small helper which is used to load all SPI services currently available.
   * 
   * @return   A list with all SPI services currently available. Not <code>null</code>.
   * 
   * @throws KclException in case one SPI could not be configured properly.
   */
  public List<T> loadServices() {
    List<T> result = new ArrayList<>();
    ServiceLoader.load( clazz ).forEach( result::add );
    if( configuration != null ) {
      result.parallelStream()
        .filter( $ -> $ instanceof Configurable )
        .forEach( s -> ((Configurable) s).configure( configuration ) );
    }
    if( postprocessor != null ) {
      result = result.parallelStream()
        .map( postprocessor::apply )
        .collect( Collectors.toList() );
    }
    if( filter != null ) {
      result = result.parallelStream()
        .filter( filter )
        .collect( Collectors.toList() );
    }
    return result;
  }
  
  public static <R> SPILoaderBuilder<R> builder() {
    return new SPILoaderBuilder<>();
  }

  public static class SPILoaderBuilder<S> {
    
    private SPILoader   instance = new SPILoader();

    public SPILoaderBuilder<S> serviceType( Class<S> type) {
      instance.clazz = type;
      return this;
    }

    public SPILoaderBuilder<S> filter( Predicate<S> test ) {
      instance.filter = test;
      return this;
    }

    public SPILoaderBuilder<S> postProcessor( Consumer<S> consumer ) {
      instance.postprocessor = WrapperFactory.toFunction( consumer );
      return this;
    }

    public SPILoaderBuilder<S> postProcessor( Function<S, S> transformer ) {
      instance.postprocessor = transformer;
      return this;
    }

    public SPILoaderBuilder<S> configuration( Map<String, Object> config ) {
      if( config != null ) {
        instance.configuration = config;
        if( instance.configuration.isEmpty() ) {
          instance.configuration = null;
        }
      }
      return this;
    }

    public SPILoaderBuilder<S> configuration( Properties config ) {
      return configuration( WrapperFactory.toMap( config ) );
    }
    
    public SPILoader build() {
      if( instance.clazz != null ) {
        return instance;
      } else {
        return null;
      }
    }
    
  } /* ENDCLASS */
  
} /* ENDCLASS */
