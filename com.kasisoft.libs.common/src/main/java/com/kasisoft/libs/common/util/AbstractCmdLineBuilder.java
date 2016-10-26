package com.kasisoft.libs.common.util;

import static com.kasisoft.libs.common.internal.Messages.*;

import com.kasisoft.libs.common.function.*;

import java.util.function.*;

import java.util.*;

import lombok.experimental.*;

import lombok.*;

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
  
  List<Argument>           arguments;
  Consumer<List<String>>   errorHandler;
  boolean                  error;
  boolean                  treatUnusedArgsAsErrors;
  
  public AbstractCmdLineBuilder() {
    arguments               = new ArrayList<>();
    errorHandler            = this::ehDefault;
    error                   = false;
    treatUnusedArgsAsErrors = false;
  }

  public R treatUnusedArgsAsErrors() {
    return treatUnusedArgsAsErrors( true );
  }
  
  public R treatUnusedArgsAsErrors( boolean errors ) {
    treatUnusedArgsAsErrors = errors;
    return (R) this;
  }
 
  public R errorHandler( @NonNull Consumer<List<String>> consumer ) {
    errorHandler = consumer;
    return (R) this;
  }
  
  /**
   * Starts the configuration of a flag.
   * 
   * @param option    The main option key.
   * @param options   Alternative option keys.
   * 
   * @return this
   */
  public R flag( @NonNull String option, String ... options ) {
    Argument argument = new Argument( ArgumentType.Flag, option, options );
    arguments.add( argument );
    return (R) this;
  }

  /**
   * Starts the configuration of single parameter.
   * 
   * @param option    The main option key.
   * @param options   Alternative option keys.
   * 
   * @return this
   */
  public R single( @NonNull String option, String ... options ) {
    Argument argument = new Argument( ArgumentType.Parameter, option, options );
    arguments.add( argument );
    return (R) this;
  }

  /**
   * Starts the configuration of multiple parameters.
   * 
   * @param option    The main option key.
   * @param options   Alternative option keys.
   * 
   * @return this
   */
  public R many( @NonNull String option, String ... options ) {
    Argument argument = new Argument( ArgumentType.ManyParameter, option, options );
    arguments.add( argument );
    return (R) this;
  }
  
  private Argument argument() {
    if( arguments.isEmpty() ) {
      throw new IllegalArgumentException( error_missing_argument );
    }
    return arguments.get( arguments.size() - 1 );
  }
  
  /**
   * Marks the current argument as required.
   * 
   * @return   this
   */
  public R required() {
    return required( true );
  }
  
  /**
   * Marks the current argument as required or not.
   * 
   * @param required   <code>true</code> <=> At least one value must be provided.
   * 
   * @return   this
   */
  public R required( boolean required ) {
    argument().required = required;
    return (R) this;
  }

  /**
   * Configures default values.
   * 
   * @param defValue   The default value (untransformed).
   * 
   * @return   this
   */
  public R defaultValue( String defValue ) {
    argument().defaultVal = defValue;
    return (R) this;
  }
  
  /**
   * Provides an informal description for this argument.
   * 
   * @param description   An optional description for this option.
   * 
   * @return   this
   */
  public R description( @NonNull String description ) {
    argument().description = description;
    return (R) this;
  }
  
  /**
   * Provides a transformer for this argument.
   * 
   * @param transformer   The transformer for the textual representation.
   * 
   * @return   this
   */
  public <T> R transformer( @NonNull Function<String, T> transformer ) {
    argument().transformer = transformer;
    return (R) this;
  }

  /**
   * Provides an applicator (setter) for this argument.
   * 
   * @param applicator   The function to call when this option had been identified.
   * 
   * @return   this
   */
  public <T> R applicator( @NonNull Consumer<T> applicator ) {
    argument().applicator = applicator;
    return (R) this;
  }

  /**
   * Provides a test for the transformed value.
   * 
   * @param test   A test for the value. If the value doesn't pass it will be ignored.
   * 
   * @return   this
   */
  public <T> R test( @NonNull Predicate<T> test ) {
    argument().test = test;
    return (R) this;
  }

  /**
   * Creates a new record instance.
   *  
   * @return   A new record instance.
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
  
  private int indexOf( List<String> args, Argument argument ) {
    return indexOf( args, argument, 0 );
  }
  
  private int indexOf( List<String> args, Argument argument, int offset ) {
    int result = -1;
    for( int i = offset; i < args.size(); i++ ) {
      if( argument.test( args.get(i) ) ) {
        result = i;
        break;
      }
    }
    return result;
  }
  
  private void validateConfiguration() {
    for( Argument arg : arguments ) {
      if( ! arg.isValid() ) {
        throw new IllegalArgumentException( arg.option );
      }
    }
  }
  
  /**
   * Parses the supplied arguments.
   * 
   * @param argarray   The actual command line arguments.
   * 
   * @return   this
   */
  public R parse( @NonNull String[] argarray ) {
    
    validateConfiguration();
    
    List<String> args         = new ArrayList<>( Arrays.asList( argarray ) );
    int[]        applications = new int[ arguments.size() ];
    Arrays.fill( applications, 0 );
    
    parseFlags          ( args, applications );
    parseManyParameter  ( args, applications );
    parseParameter      ( args, applications );
    handleDefaultValues ( applications );
    
    List<String> errors = new ArrayList<>();
    
    checkForRemainingArgs( args, errors );
    checkForMissingRequiredArgs( applications, errors );
    
    if( ! errors.isEmpty() ) {
      errorHandler.accept( errors );
      error = true;
    }
    
    return (R) this;
  }
  
  private void handleDefaultValues( int[] applications ) {
    for( int i = 0; i < arguments.size(); i++ ) {
      if( (arguments.get(i).defaultVal != null) && (applications[i] == 0) ) {
        Object value = arguments.get(i).transformer.apply( arguments.get(i).defaultVal );
        if( (arguments.get(i).test == null) || arguments.get(i).test.test( value ) ) {
          arguments.get(i).applicator.accept( value );
          applications[i] = 1;
        }
      }
    }
  }
  
  private void checkForMissingRequiredArgs( int[] applications, List<String> errors ) {
    for( int i = 0; i < arguments.size(); i++ ) {
      if( arguments.get(i).required && (applications[i] == 0) ) {
        errors.add( error_missing_required_option.format( arguments.get(i).option ) );
      }
    }
  }

  private void checkForRemainingArgs( List<String> args, List<String> errors ) {
    if( (! args.isEmpty()) && treatUnusedArgsAsErrors ) {
      for( String arg : args ) {
        errors.add( unused_argument.format( arg ) );
      }
    }
  }
  
  private void ehDefault( List<String> messages ) {
    // do nothing by default
  }
  
  private void parseParameter( List<String> args, int[] applications ) {
    for( int i = 0; i < arguments.size(); i++ ) {
      if( arguments.get(i).type == ArgumentType.Parameter ) {
        parseParameter( args, arguments.get(i), i, applications );
      }
    }
  }
  
  private void parseParameter( List<String> args, Argument argument, int pos, int[] applications ) {
    int i = indexOf( args, argument );
    if( (i != -1) && (i < args.size() - 1) ) {
      applications[ pos ] = applications[ pos ] + 1;
      Object value = argument.transformer.apply( args.get( i + 1 ) );
      if( (argument.test == null) || argument.test.test( value ) ) {
        argument.applicator.accept( value );
      }
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
    int i = indexOf( args, argument );
    while( (i != -1) && (i < args.size() - 1) ) {
      applications[ pos ] = applications[ pos ] + 1;
      Object value = argument.transformer.apply( args.get( i + 1 ) );
      if( (argument.test == null) || argument.test.test( value ) ) {
        argument.applicator.accept( value );
      }
      args.remove(i);
      args.remove(i);
      i = indexOf( args, argument );
    }
  }
  
  private void parseFlags( List<String> args, int[] applications ) {
    for( int i = 0; i < arguments.size(); i++ ) {
      if( arguments.get(i).type == ArgumentType.Flag ) {
        Argument argument = arguments.get(i);
        int      idx      = indexOf( args, argument );
        if( idx != -1 ) {
          args.remove( idx );
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
      builder.append( arg.option );
      if( ! arg.options.isEmpty() ) {
        builder.append( "(alternatives: " );
        for( String option : arg.options ) {
          builder.append( option );
          builder.append( "," );
        }
        builder.deleteCharAt( builder.length() - 1 );
        builder.append( ")" );
      }
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
          builder.append( "\t" ).append( arg.option ).append( " : " ).append( arg.description ).append( "\n" );
        }
      }
    }
    return builder.toString();
  }

  private static final class Argument implements Comparable<Argument>, Predicate<String> {
    
    ArgumentType   type;
    String         option;
    Consumer       applicator;
    boolean        required    = false;
    Set<String>    options     = Collections.emptySet();
    String         description = null;
    Function       transformer = String::valueOf;
    Predicate      test        = Predicates.acceptAll();
    String         defaultVal  = null;
    
    private Argument( ArgumentType argType, String argOption, String ... argOptions ) {
      type    = argType;
      option  = argOption;
      if( (argOptions != null) && (argOptions.length > 0) ) {
        options = new HashSet<>( Arrays.asList( argOptions ) ); 
      }
    }
    
    public boolean isValid() {
      return applicator != null;
    }
    
    @Override
    public int compareTo( Argument o ) {
      return option.compareTo( o.option );
    }

    @Override
    public boolean test( String t ) {
      boolean result = option.equals(t);
      if( ! result ) {
        result = options.contains(t);
      }
      return result;
    }
    
  } /* ENDCLASS */
  
} /* ENDCLASS */
