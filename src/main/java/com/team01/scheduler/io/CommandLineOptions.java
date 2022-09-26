package com.team01.scheduler.io;

import com.google.devtools.common.options.Option;
import com.google.devtools.common.options.OptionsBase;

public class CommandLineOptions extends OptionsBase{

    @Option(
            name = "num_cores",
            abbrev = 'p',
            help = "Uses this many cores for execution in parallel. Default is sequential.",
            category = "startup",
            defaultValue = "1"
    )
    public int numCores;

    @Option(
            name = "output",
            abbrev = 'o',
            help = "Specify the name of the output file. The default is INPUT-output.dot",
            category = "startup",
            defaultValue = ""
    )
    public String outputFileName;

    @Option(
            name = "visualize",
            abbrev = 'v',
            help = "Enable/disable visualizing the search",
            category = "startup",
            defaultValue = "false"
    )
    public boolean isVisualize;

    @Option(
            name = "help",
            abbrev = 'h',
            help = "Prints usage info.",
            defaultValue = "false"
    )
    public boolean help;

    @Option(
            name = "debugGui",
            abbrev = 'd',
            help = "Launches the debug GUI. This will not run the scheduler.",
            defaultValue = "false"
    )
    public boolean debugGui;
}
