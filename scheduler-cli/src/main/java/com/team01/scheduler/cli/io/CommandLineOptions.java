package com.team01.scheduler.cli.io;

import com.google.devtools.common.options.Option;
import com.google.devtools.common.options.OptionsBase;

/**
 * Uses google dev options to pass in command line options into application
 */
public class CommandLineOptions extends OptionsBase{
    // Options are defined
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
            name = "algorithm",
            abbrev = 'a',
            help = "The algorithm to use (BRANCH_AND_BOUND or A_STAR)",
            defaultValue = "BRANCH_AND_BOUND"
    )
    public String algorithm;
}
