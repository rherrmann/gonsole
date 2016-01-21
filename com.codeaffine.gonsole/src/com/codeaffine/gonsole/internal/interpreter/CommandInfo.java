package com.codeaffine.gonsole.internal.interpreter;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.pgm.TextBuiltin;
import org.eclipse.jgit.pgm.opt.SubcommandHandler;
import org.kohsuke.args4j.Argument;

public class CommandInfo {

  @Argument(index = 0, metaVar = "metaVar_command", required = true, handler = SubcommandHandler.class)
  TextBuiltin command;

  @Argument(index = 1, metaVar = "metaVar_arg")
  final List<String> arguments;

  public CommandInfo() {
    arguments = new ArrayList<>();
  }

  public TextBuiltin getCommand() {
    return command;
  }

  public String[] getArguments() {
    return arguments.stream().toArray( String[]::new );
  }
}