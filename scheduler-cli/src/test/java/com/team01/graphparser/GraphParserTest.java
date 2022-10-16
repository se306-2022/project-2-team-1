package com.team01.graphparser;

import com.team01.scheduler.cli.io.CommandLineParser;
import com.team01.scheduler.graph.exceptions.InvalidInputException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GraphParserTest {

    /**
     * Junit test, checking if an invalid input graphviz file name is being handled
     * correctly. The correct behaviour should be where it is identified that the
     * input file is incorrect and an InvalidInputException is thrown to indicate that
     * fact.
     * 
     */
    @Test
    void testInvalidInputExceptionForFileName() {

        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
            String [] args = {"not_a_valid_file.txt", "2"};

            /**Create new constructor instance that is public**/
            Constructor<CommandLineParser> constructor = CommandLineParser.class.getDeclaredConstructor();
            Assertions.assertTrue(Modifier.isPrivate(constructor.getModifiers()));
            constructor.setAccessible(true);
            CommandLineParser CLP = constructor.newInstance();
            constructor.setAccessible(false);

            CLP.parseInputArguments(args);
        });

        Assertions.assertEquals(exception.getMessage(), new InvalidInputException("input file name is invalid").getMessage());
    }


}