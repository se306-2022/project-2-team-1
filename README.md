# project-2-team-1

## Running in IntelliJ
Use the Gradle `application->run` task with the following parameters:

To run for a given dot file in the root directory:
```
# <filename> <num processes>
run --args="graph.dot 3"
```

To run with visualisation enabled:
```
# <filename> <num processes> --visualize
run --args="graph.dot 3 -v"
```

To see help options:
```
# --help
run --args="-h"
```

To run with Debug Mode GUI (i.e. Does not run scheduler):
```
run --args="-d"
```