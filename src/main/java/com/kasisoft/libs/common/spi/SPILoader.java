package com.kasisoft.libs.common.spi;

import com.kasisoft.libs.common.utils.MiscFunctions;

import javax.validation.constraints.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import java.util.stream.Collectors;

import java.util.ArrayList;
import java.util.HashMap;
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
public class SPILoader {

  Map<Class, Predicate>   filter;
  Map<Class, Function>    postprocessor;
  Map<String, String>     configuration;
  
  private SPILoader() {
    filter        = null;
    postprocessor = null;
    configuration = null;
  }
  
  /**
   * Small helper which is used to load all SPI services currently available.
   * 
   * @param servicetype   The desired service type.
   * 
   * @return   A list with all SPI services currently available.
   */
  @SuppressWarnings("cast")
  public <T> List<T> loadServices(@NotNull Class<T> servicetype) {
    
    var result = (List<T>) new ArrayList<T>();
    ServiceLoader.load(servicetype).forEach(result::add);
    
    if (configuration != null) {
      result.parallelStream()
        .filter($ -> $ instanceof Configurable)
        .map($ -> (Configurable) $)
        .forEach($ -> $.configure(configuration))
        ;
    }
    
    if ((postprocessor != null) && postprocessor.containsKey(servicetype)) {
      Function<T, T> processor = postprocessor.get(servicetype); 
      result = result.parallelStream()
        .map(processor::apply)
        .collect(Collectors.toList())
        ;
    }
    
    if ((filter != null) && filter.containsKey(servicetype)) {
      Predicate<T> test = filter.get(servicetype);
      result = result.parallelStream()
        .filter(test)
        .collect(Collectors.toList())
        ;
    }
    
    return result;
    
  }
  
  public static SPILoaderBuilder builder() {
    return new SPILoaderBuilder();
  }

  public static class SPILoaderBuilder {
    
    private SPILoader   instance = new SPILoader();
    
    public <T> SPILoaderBuilder filter(@NotNull Class<T> spiType, @NotNull Predicate<T> test) {
      if (instance.filter == null) {
        instance.filter = new HashMap<>();
      }
      instance.filter.put(spiType, test);
      return this;
    }

    private <T> T adaptFunction(T arg, Consumer<T> consumer) {
      consumer.accept(arg);
      return arg;
    }

    public <T> SPILoaderBuilder postProcessor(@NotNull Class<T> spiType, @NotNull Consumer<T> consumer) {
      return postProcessor(spiType, (Function<T, T>) $ -> adaptFunction($, consumer));
    }
    
    public <T> SPILoaderBuilder postProcessor(@NotNull Class<T> spiType, @NotNull Function<T, T> transformer) {
      if (transformer != null) {
        if (instance.postprocessor == null) {
          instance.postprocessor = new HashMap<>();
        }
        instance.postprocessor.put(spiType, transformer);
      }
      return this;
    }

    public <T> SPILoaderBuilder configuration(@NotNull Map<String, String> config) {
      instance.configuration = new HashMap<>(config);
      if (instance.configuration.isEmpty()) {
        instance.configuration = null;
      }
      return this;
    }

    public <T> SPILoaderBuilder configuration(@NotNull Properties config) {
      return configuration(MiscFunctions.propertiesToMap(config));
    }
    
    public SPILoader build() {
      return instance;
    }
    
  } /* ENDCLASS */
  
} /* ENDCLASS */
