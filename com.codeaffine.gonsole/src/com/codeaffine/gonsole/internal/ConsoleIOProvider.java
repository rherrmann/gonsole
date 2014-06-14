package com.codeaffine.gonsole.internal;

import java.io.InputStream;
import java.io.OutputStream;

public interface ConsoleIOProvider {

  OutputStream newOutputStream();
  InputStream getInputStream();
  String getEncoding();
  String getLineDelimiter();
}
