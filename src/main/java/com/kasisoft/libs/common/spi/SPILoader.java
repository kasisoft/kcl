package com.kasisoft.libs.common.spi;

import com.kasisoft.libs.common.KclException;
import com.kasisoft.libs.common.functional.Functions;
import com.kasisoft.libs.common.utils.WrapperFunctions;

import javax.validation.constraints.NotNull;

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
  @SuppressWarnings("cast")
  public List<T> loadServices() {
    
    var result = (List<T>) new ArrayList<T>();
    ServiceLoader.load(clazz).forEach(result::add);
    
    if (configuration != null) {
      result.parallelStream()
        .filter($ -> $ instanceof Configurable)
        .map($ -> (Configurable) $)
        .forEach($ -> $.configure(configuration))
        ;
    }
    
    if (postprocessor != null) {
      result = result.parallelStream()
        .map(postprocessor::apply)
        .collect(Collectors.toList())
        ;
    }
    
    if (filter != null) {
      result = result.parallelStream()
        .filter(filter)
        .collect(Collectors.toList())
        ;
    }
    
    return result;
    
  }
  
  public static <R> SPILoaderBuilder<R> builder() {
    return new SPILoaderBuilder<>();
  }

  public static class SPILoaderBuilder<S> {
    
    private SPILoader   instance = new SPILoader();

    public SPILoaderBuilder<S> serviceType(@NotNull Class<S> type) {
      instance.clazz = type;
      return this;
    }

    public SPILoaderBuilder<S> filter(@NotNull Predicate<S> test) {
      instance.filter = test;
      return this;
    }

    public SPILoaderBuilder<S> postProcessor(@NotNull Consumer<S> consumer) {
      instance.postprocessor = Functions.adapt(consumer);
      return this;
    }

    public SPILoaderBuilder<S> postProcessor(@NotNull Function<S, S> transformer) {
      instance.postprocessor = transformer;
      return this;
    }

    public SPILoaderBuilder<S> configuration(@NotNull Map<String, Object> config) {
      if (config != null) {
        instance.configuration = config;
        if (instance.configuration.isEmpty()) {
          instance.configuration = null;
        }
      }
      return this;
    }

    public SPILoaderBuilder<S> configuration(@NotNull Properties config) {
      return configuration(WrapperFunctions.toMap(config));
    }
    
    public SPILoader build() {
      if (instance.clazz != null) {
        return instance;
      } else {
        return null;
      }
    }
    
  } /* ENDCLASS */
  
} /* ENDCLASS */
