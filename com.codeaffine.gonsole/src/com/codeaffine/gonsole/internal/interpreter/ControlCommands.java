package com.codeaffine.gonsole.internal.interpreter;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ControlCommands {

  public static final String USE = "use";
  public static final String HELP = "help";

  public static final Collection<String> COMMANDS = Arrays.asList( USE, HELP );

  public static final Map<String,String> DESCRIPTION;
  public static final Map<String,String> USAGE;

  private static final String USE_DESCRIPTION = "Change the current repository";
  private static final String USE_USAGE
    = USE_DESCRIPTION
    + "\n"
    + " repository\n\n"
    + " repository: Name or full path to the .git directory of the repository to use. "
    + "The name is taken from the last segment of the path to the repository (without .git). "
    + "E.g. the name of /path/to/repo/.git is 'repo'.";

  private static final String HELP_DESCRIPTION = "List available commands or show more information about a command";
  private static final String HELP_USAGE
    = HELP_DESCRIPTION
    + "\n"
    + " [command]\n\n"
    + " command : Name of the command to show help for. If no command is specified, a list of"
    + "available commands is shown.";


  static {
    DESCRIPTION = new HashMap<>();
    DESCRIPTION.put( USE, USE_DESCRIPTION );
    DESCRIPTION.put( HELP, HELP_DESCRIPTION );
    USAGE = new HashMap<>();
    USAGE.put( USE, USE_USAGE );
    USAGE.put( HELP, HELP_USAGE );
  }
}
