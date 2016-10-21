package com.kasisoft.libs.common.util;

import com.kasisoft.libs.common.internal.*;

import lombok.experimental.*;

import lombok.*;

import java.util.function.*;

import java.util.*;

/**
 * Base type to simplify the handling of command lines.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class AbstractCmdLineBuilder<R extends AbstractCmdLineBuilder, V> {

  private enum ArgumentType {
    
    Flag,
    Parameter,
    ManyParameter
    
  }
  
  List<Argument>   arguments;
  List<String>     messages;
  boolean          error;
  
  public AbstractCmdLineBuilder() {
    arguments = new ArrayList<>();
    messages  = new ArrayList<>();
    error     = false;
  }
  
  /**
   * Registers a parameter which is allowed to occurs only once. Additional parameters values will be logged as warnings.
   * 
   * @param key           The value indicates enablement.
   * @param required      <code>true</code> <=> At least one value must be provided.
   * @param description   An optional description for this option.
   * @param transformer   The transformer for the textual representation.
   * @param applicator    The function to call when this option had been identified.
   * 
   * @return   this
   */
  public <T> R withParameter( @NonNull String key, boolean required, String description, @NonNull Function<String, T> transformer, @NonNull Consumer<T> applicator ) {
    arguments.add( new Argument( ArgumentType.Parameter, required, key, description, transformer, applicator ) );
    return (R) this;
  }

  /**
   * Registers a parameter which is allowed to occurs only once. Additional parameters values will be logged as warnings.
   * 
   * @param key           The value indicates enablement.
   * @param required      <code>true</code> <=> At least one value must be provided.
   * @param transformer   The transformer for the textual representation.
   * @param applicator    The function to call when this option had been identified.
   * 
   * @return   this
   */
  public <T> R withParameter( @NonNull String key, boolean required, @NonNull Function<String, T> transformer, @NonNull Consumer<T> applicator ) {
    return withParameter( key, required, null, transformer, applicator );
  }

  /**
   * Registers a parameter which is allowed to occurs only once. Additional parameters values will be logged as warnings.
   * 
   * @param key          The value indicates enablement.
   * @param required     <code>true</code> <=> At least one value must be provided.
   * @param applicator   The function to call when this option had been identified.
   * 
   * @return   this
   */
  public R withParameter( @NonNull String key, boolean required, @NonNull Consumer<String> applicator ) {
    return withParameter( key, required, null, String::valueOf, applicator );
  }

  /**
   * Registers a parameter which is allowed to occurs only once. Additional parameters values will be logged as warnings.
   * 
   * @param key          The value indicates enablement.
   * @param applicator   The function to call when this option had been identified.
   * 
   * @return   this
   */
  public R withParameter( @NonNull String key, @NonNull Consumer<String> applicator ) {
    return withParameter( key, false, null, String::valueOf, applicator );
  }

  /**
   * Registers a parameter which is allowed to occurs more than once.
   * 
   * @param key           The value indicates enablement.
   * @param required      <code>true</code> <=> At least one value must be provided.
   * @param description   An optional description for this option.
   * @param transformer   The transformer for the textual representation.
   * @param applicator    The function to call when this option had been identified.
   * 
   * @return   this
   */
  public <T> R withManyParameter( @NonNull String key, boolean required, String description, @NonNull Function<String, T> transformer, @NonNull Consumer<T> applicator ) {
    arguments.add( new Argument( ArgumentType.ManyParameter, required, key, description, transformer, applicator ) );
    return (R) this;
  }

  /**
   * Registers a parameter which is allowed to occurs more than once.
   * 
   * @param key           The value indicates enablement.
   * @param required      <code>true</code> <=> At least one value must be provided.
   * @param transformer   The transformer for the textual representation.
   * @param applicator    The function to call when this option had been identified.
   * 
   * @return   this
   */
  public <T> R withManyParameter( @NonNull String key, boolean required, @NonNull Function<String, T> transformer, @NonNull Consumer<T> applicator ) {
    return withManyParameter( key, required, null, transformer, applicator );
  }

  /**
   * Registers a parameter which is allowed to occurs more than once.
   * 
   * @param key           The value indicates enablement.
   * @param required      <code>true</code> <=> At least one value must be provided.
   * @param applicator    The function to call when this option had been identified.
   * 
   * @return   this
   */
  public R withManyParameter( @NonNull String key, boolean required, @NonNull Consumer<String> applicator ) {
    return withManyParameter( key, required, null, String::valueOf, applicator );
  }

  /**
   * Registers a parameter which is allowed to occurs more than once.
   * 
   * @param key          The value indicates enablement.
   * @param applicator   The function to call when this option had been identified.
   * 
   * @return   this
   */
  public R withManyParameter( @NonNull String key, @NonNull Consumer<String> applicator ) {
    return withManyParameter( key, false, null, String::valueOf, applicator );
  }

  /**
   * Registers an option for a flag.
   * 
   * @param key           The value indicates enablement.
   * @param description   An optional description for this option.
   * @param applicator    The function to call when this option had been identified.
   * 
   * @return   this
   */
  public R withFlag( @NonNull String key, String description, @NonNull Consumer<Boolean> applicator ) {
    arguments.add( new Argument( ArgumentType.Flag, false, key, description, key::equals, applicator ) );
    return (R) this;
  }

  /**
   * Registers an option for a flag.
   * 
   * @param key          The value indicates enablement.
   * @param applicator   The function to call when this option had been identified.
   * 
   * @return   this
   */
  public R withFlag( @NonNull String key, @NonNull Consumer<Boolean> applicator ) {
    return withFlag( key, null, applicator );
  }
  
  /**
   * Builds an actual data structure from the parsed settings.
   *  
   * @return   The build data structure. Maybe <code>null</code>.
   */
  protected abstract V buildImpl();
  
  /**
   * Like {@link #buildImpl()} but this function returns <code>null</code> if there was an error while
   * parsing command line arguments through {@link #parse(String[])}.
   * 
   * @return   The data structure.
   */
  public V build() {
    V result = null;
    if( ! error ) {
      result = buildImpl();
    }
    return result;
  }
  
  /**
   * Parses the supplied arguments.
   * 
   * @param argarray   The actual command line arguments.
   * 
   * @return   this
   */
  public R parse( @NonNull String[] argarray ) {
    List<String> args         = new ArrayList<>( Arrays.asList( argarray ) );
    int[]        applications = new int[ arguments.size() ];
    Arrays.fill( applications, 0 );
    parseFlags         ( args, applications );
    parseManyParameter ( args, applications );
    parseParameter     ( args, applications );
    if( ! args.isEmpty() ) {
      for( String arg : args ) {
        messages.add( Messages.unused_argument.format( arg ) );
      }
    }
    for( int i = 0; i < arguments.size(); i++ ) {
      if( arguments.get(i).required && (applications[i] == 0) ) {
        messages.add( Messages.error_missing_required_option.format( arguments.get(i).key ) );
        error = true;
      }
    }
    return (R) this;
  }  

  private void parseParameter( List<String> args, int[] applications ) {
    for( int i = 0; i < arguments.size(); i++ ) {
      if( arguments.get(i).type == ArgumentType.Parameter ) {
        parseParameter( args, arguments.get(i), i, applications );
      }
    }
  }
  
  private void parseParameter( List<String> args, Argument argument, int pos, int[] applications ) {
    int i = args.indexOf( argument.key );
    if( (i != -1) && (i < args.size() - 1) ) {
      applications[ pos ] = applications[ pos ] + 1;
      Object value = argument.transformer.apply( args.get( i + 1 ) );
      argument.applicator.accept( value );
      args.remove(i);
      args.remove(i);
    }
  }
  
  private void parseManyParameter( List<String> args, int[] applications ) {
    for( int i = 0; i < arguments.size(); i++ ) {
      if( arguments.get(i).type == ArgumentType.ManyParameter ) {
        parseManyParameter( args, arguments.get(i), i, applications );
      }
    }
  }
  
  private void parseManyParameter( List<String> args, Argument argument, int pos, int[] applications ) {
    int i = args.indexOf( argument.key );
    while( (i != -1) && (i < args.size() - 1) ) {
      applications[ pos ] = applications[ pos ] + 1;
      Object value = argument.transformer.apply( args.get( i + 1 ) );
      argument.applicator.accept( value );
      args.remove(i);
      args.remove(i);
      i = args.indexOf( argument.key );
    }
  }
  
  private void parseFlags( List<String> args, int[] applications ) {
    for( int i = 0; i < arguments.size(); i++ ) {
      if( arguments.get(i).type == ArgumentType.Flag ) {
        Argument argument = arguments.get(i);
        if( args.contains( argument.key ) ) {
          args.remove( argument.key );
          applications[i] = applications[i] + 1;
          argument.applicator.accept( Boolean.TRUE );
        }
      }
    }
  }
  
  /**
   * Returns the full usage for the command line.
   * 
   * @return   The full usage for the command line. Not <code>null</code>.
   */
  public String usage() {
    return usage( false );
  }
  
  /**
   * Returns the usage for the command line.
   * 
   * @param shortUsage   <code>true</code> <=> Just return the commandline.
   * 
   * @return   The usage for the command line. Not <code>null</code>.
   */
  public String usage( boolean shortUsage ) {
    List<Argument> args    = new ArrayList<>( arguments );
    Collections.sort( args );
    StringBuilder    builder = new StringBuilder();
    for( Argument arg : args ) {
      if( ! arg.required ) {
        builder.append( "[" );
      }
      builder.append( arg.key );
      if( arg.type != ArgumentType.Flag ) {
        builder.append( " " ).append( "<value>" );
      }
      if( ! arg.required ) {
        builder.append( "]" );
      }
      if( arg.type == ArgumentType.ManyParameter ) {
        builder.append( "*" );
      }
      builder.append( " " );
    }
    if( ! args.isEmpty() ) {
      builder.deleteCharAt( builder.length() - 1 );
    }
    builder.append( "\n" );
    if( ! shortUsage ) {
      for( Argument arg : args ) {
        if( arg.description != null ) {
          builder.append( "\t" ).append( arg.key ).append( " : " ).append( arg.description ).append( "\n" );
        }
      }
    }
    return builder.toString();
  }

  @AllArgsConstructor
  private static final class Argument implements Comparable<Argument> {
    
    ArgumentType   type;
    boolean        required;
    String         key;
    String         description;
    Function       transformer;
    Consumer       applicator;
    
    @Override
    public int compareTo( Argument o ) {
      return key.compareTo( o.key );
    }
    
  } /* ENDCLASS */
  
} /* ENDCLASS */
