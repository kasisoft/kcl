/**
 * Name........: process (implementation)
 * Description.: Implementation of a sample process used for testing purposes.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */

#include <stdlib.h>
#include <stdio.h>

int main( int argc, char** argv ) {
  if( argc > 1 ) {
    if( strcmp( "-rc", argv[1] ) == 0 ) {
      return atoi( argv[2] );
    } else if( strcmp( "-stderr", argv[1] ) == 0 ) {
      fprintf( stderr, "Hello World !\n" );
      return 0;
    } else if( strcmp( "-error", argv[1] ) == 0 ) {
      // npe access
      char* var = NULL;
      var[0]    = 'Z';
      return -1;
    }
  }
  fprintf( stdout, "Hello World !\n" );
  return 0;
}

