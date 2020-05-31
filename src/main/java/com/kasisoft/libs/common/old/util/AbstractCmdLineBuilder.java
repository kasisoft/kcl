package com.kasisoft.libs.common.old.util;

import static com.kasisoft.libs.common.old.internal.Messages.error_missing_argument;
import static com.kasisoft.libs.common.old.internal.Messages.error_missing_required_argument;
import static com.kasisoft.libs.common.old.internal.Messages.error_missing_required_option;
import static com.kasisoft.libs.common.old.internal.Messages.unused_argument;

import com.kasisoft.libs.common.old.function.Predicates;
import com.kasisoft.libs.common.old.text.StringFunctions;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import java.util.stream.Collectors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;
import lombok.NonNull;

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
    ManyParameter,
    Indexed
    
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

  public R errorHandler( @NonNull String applicationName ) {
    return errorHandler( applicationName, null );
  }
  
  public R errorHandler( @NonNull String applicationName, Runnable followup ) {
    errorHandler = $ -> {
      System.err.println( applicationName + " " + usage( false ) );
      for( int i = 0; i < $.size(); i++ ) {
        System.err.println( "=> " + $.get(i) );
      }
      System.err.println();
      if( followup != null ) {
        followup.run();
      }
    };
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
   * Starts the configuration for a single parameter without an option. Creating these arguments
   * must comply with the ordering on the command line.
   * 
   * @return this
   */
  public R indexed() {
    return indexed( null );
  }

  /**
   * Starts the configuration for a single parameter without an option. Creating these arguments
   * must comply with the ordering on the command line.
   * 
   * @param name   A name that will be used to identify this indexed parameter.
   * 
   * @return this
   */
  public R indexed( String name ) {
    Argument argument = new Argument( ArgumentType.Indexed, name );
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
   * @param   remaining <=> List of remaining arguments (order is being preserved)
   * 
   * @return   A new record instance.
   */
  protected abstract V buildImpl( @NonNull List<String> remaining );
  
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
  public V parse( @NonNull String[] argarray ) {
    
    validateConfiguration();
    
    List<String> args         = new ArrayList<>( Arrays.asList( argarray ) );
    int[]        applications = new int[ arguments.size() ];
    Arrays.fill( applications, 0 );
    
    parseFlags            ( args, applications );
    parseManyParameter    ( args, applications );
    parseParameter        ( args, applications );
    parseIndexedParameter ( args, applications );
    handleDefaultValues   ( applications );
    
    List<String> errors = new ArrayList<>();
    
    checkForRemainingArgs( args, errors );
    checkForMissingRequiredArgs( applications, errors );
    
    if( ! errors.isEmpty() ) {
      errorHandler.accept( errors );
      error = true;
    }
    
    V result = null;
    if( ! error ) {
      result = buildImpl( args );
    }
    return result;

  }
  
  private void handleDefaultValues( int[] applications ) {
    for( int i = 0; i < arguments.size(); i++ ) {
      if( (arguments.get(i).defaultVal != null) && (applications[i] == 0) ) {
        Object value = arguments.get(i).transformer.apply( arguments.get(i).defaultVal );
        if( arguments.get(i).test.test( value ) ) {
          arguments.get(i).applicator.accept( value );
          applications[i] = 1;
        }
      }
    }
  }
  
  private void checkForMissingRequiredArgs( int[] applications, List<String> errors ) {
    int idxCount = 1;
    for( int i = 0; i < arguments.size(); i++ ) {
      if( arguments.get(i).required && (applications[i] == 0) ) {
        String option = arguments.get(i).option;
        if( option == null ) {
          option = String.format( "arg%d", idxCount );
        }
        if( arguments.get(i).type == ArgumentType.Indexed ) {
          errors.add( error_missing_required_argument.format( idxCount, option ) );
          idxCount++;
        } else {
          errors.add( error_missing_required_option.format( option ) );
        }
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

  private void parseIndexedParameter( List<String> args, int[] applications ) {
    for( int i = 0; (i < arguments.size()) && (!args.isEmpty()); i++ ) {
      if( arguments.get(i).type == ArgumentType.Indexed ) {
        parseIndexed( args, arguments.get(i), i, applications );
      }
    }
  }

  private void parseIndexed( List<String> args, Argument argument, int pos, int[] applications ) {
    Object value = argument.transformer.apply( args.get(0) );
    applications[ pos ] = applications[ pos ] + 1;
    if( argument.test.test( value ) ) {
      argument.applicator.accept( value );
    }
    args.remove(0);
  }

  private void parseParameter( List<String> args, Argument argument, int pos, int[] applications ) {
    int i = indexOf( args, argument );
    if( (i != -1) && (i < args.size() - 1) ) {
      applications[ pos ] = applications[ pos ] + 1;
      Object value = argument.transformer.apply( args.get( i + 1 ) );
      if( argument.test.test( value ) ) {
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
      if( argument.test.test( value ) ) {
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
   * @param shortUsage    <code>true</code> <=> Just return the commandline.
   * 
   * @return   The usage for the command line. Not <code>null</code>.
   */
  public String usage( boolean shortUsage ) {
    List<Argument> args     = arguments.stream().filter( $ -> $.type != ArgumentType.Indexed ).collect( Collectors.toList() );
    Collections.sort( args );
    arguments.stream().filter( $ -> $.type == ArgumentType.Indexed ).forEach( args::add );
    int            idxCount = 1;
    StringBuilder  builder  = new StringBuilder();
    for( Argument arg : args ) {
      if( ! arg.required ) {
        builder.append( "[" );
      }
      if( arg.option != null ) {
        builder.append( arg.option );
      } else if( arg.type == ArgumentType.Indexed ) {
        builder.append( String.format( "arg%d", idxCount++ ) );
      }
      if( ! arg.options.isEmpty() ) {
        builder.append( "(alternatives: " );
        for( String option : arg.options ) {
          builder.append( option );
          builder.append( "," );
        }
        builder.deleteCharAt( builder.length() - 1 );
        builder.append( ")" );
      }
      if( (arg.type != ArgumentType.Flag) && (arg.type != ArgumentType.Indexed) ) {
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
      builder.append( "\n" );
      idxCount = 1;
      List<String> names = new ArrayList<>();
      for( Argument arg : args ) {
        if( arg.description != null ) {
          String option = arg.option;
          if( (option == null) && (arg.type == ArgumentType.Indexed) ) {
            option = String.format( "arg%d", idxCount++ );
          }
          names.add( option );
        }
      }
      int maxLength = names.parallelStream().map( String::length ).reduce( 0, Math::max ).intValue() + 1;
      for( Argument arg : args ) {
        if( arg.description != null ) {
          String name       = names.remove(0);
          String padding    = StringFunctions.fillString( maxLength - name.length(), ' ' );
          builder.append( "\t" ).append( name ).append( padding ).append( " : " ).append( arg.description ).append( "\n" );
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
