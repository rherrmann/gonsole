package com.codeaffine.gonsole.test.io;

import java.io.File;
import java.io.IOException;

public class Files {

  public static File toCanonicalFile( File file ) {
    try {
      return file.getCanonicalFile();
    } catch( IOException ioe ) {
      throw new RuntimeException( ioe );
    }
  }
}
