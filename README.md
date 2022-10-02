# SOFTENG306 Project 2 - Team-1

#### Matthew Jakeman, Bryan Liu, Jaskaran Sandhu, Jia Tee, Jordan York, Nilay Setiya

### Three Module Structure
For this project, we decided to use a three module structure to separate the different components. These components
were the CLI (Command Line Interface), Core (containing graph models and algorithms)
and the GUI (Graphical User Interface).

The CLI module contains all code relating to passing graph input through the command line. This includes checking input
validity as well as converting to into our created graph models to then run through our algorithms.

As mentioned above, important components such as the graph models are located in the Core module. This module also
contains our algorithms and their initial prototypes.

Lastly, the GUI modules contains all components relating to the visualisation of the scheduler including specific
user views and layout constraints.

The justification behind this design decision was to keep all components for these three large modules
together whilst not overloading the main project with an excessive amount of files and directories.

### Running CLI

#### IntelliJ

To run the CLI of our project via IntelliJ, click on the Gradle tab and locate **scheduler-cli**. Click the dropdown
and then click the Task dropdown. Following on, click the application dropdown then run. 

For this to work, the graph dot
file needs to be in the **scheduler-cli** directory then configurations need to be edited so the **run** tab looks 
like this:

`run --args="graph.dot 2" `

where `graph.dot` is the input file and `2` is the number of processors.

### Running GUI

#### IntelliJ

To run the GUI of our project via IntelliJ, click on the Gradle tab and locate **scheduler-gui**. Click the dropdown
and then click the Task dropdown. Following on, click the application dropdown then run.

Input components like the graph and the number of processors can be edited in the GUI.

### Command Line Options for CLI



### Building the JAR


### Running the JAR

