package com.team01.graphparser;

import com.team01.scheduler.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GraphModelTest {


    @Test
    void testInvalidName() {
        String name = "Does_Not_Exist.dot";

        String graph = Utils.loadResource(Utils.class, name);

        Assertions.assertNull(graph);
    }


}