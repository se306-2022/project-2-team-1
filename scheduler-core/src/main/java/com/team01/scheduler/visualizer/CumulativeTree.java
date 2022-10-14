package com.team01.scheduler.visualizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CumulativeTree {

    public ConcurrentLinkedQueue receivedStates;

    // CumulativeTree: Since we are dealing with enormous trees, it is
    // far too expensive to actually store the entire search space at
    // once.
    //
    // CORE ASSUMPTION: The search space is a similar size for each
    // root-level node.
    //
    // To avoid this, we cumulatively generate the tree, not regenerating
    // anything as we push additional partial solutions.
    //
    // We use 'sectors' to describe each node:
    //  - A sector is a fraction of a circle
    //  - Sectors are divided evenly
    //     e.g. If there are two nodes at a given depth, the sectors will be semicircles
    //  - The sectorId is a unique (amongst all depths) identifier for a sector
    //
    // Sectors point to child sectors, which subdivide the parent sector on the
    // radial tree. The total area of all child sectors equals the parent sector
    // (with some offset near the base to indicate the parent sector)
    //
    // The drawing algorithm starts with the root node and draws
    // each sector, subdividing itself further for each child sector.


    public static class State {
        // Drawable
        int pathLength;
        int parentId;
        int numChildren;

        // Dirty
        public boolean dirty;

        public State(int pathLength, /*int numChildren, */int parentId) {
            this.pathLength = pathLength;
            //this.numChildren = numChildren;
            this.parentId = parentId;
        }

        public int getPathLength() {
            return pathLength;
        }

        public boolean isDirty() {
            return dirty;
        }
    }

    // Map between sectorId and sector description
    public Map<Integer, State> stateMap = new HashMap<>();

    // Map between sectorId and child sectorIds
    public Map<Integer, List<Integer>> outwardRelation = new HashMap<>();

    // Map between depth and sectorId
    public Map<Integer, List<Integer>> depthMap = new HashMap<>();

    // Map between depth and total children
    public Map<Integer, Integer> numSolutions = new HashMap();
    private int sectorId = ROOT_ID;

    State rootState;

    public static final int ROOT_ID = 0;
    public static final int INITIAL_DEPTH = 1;
    private static final int EXIT_ID = -1;

    public CumulativeTree() {
        rootState = stateMap.put(ROOT_ID, new State(0, EXIT_ID));
    }

    private void markDirtyAndPropagate(State state) {
        State parent;
        int parentId = state.parentId;
        state.dirty = true;

        while ((parent = stateMap.getOrDefault(parentId, null)) != null) {
            parent.dirty = true;
            parentId = parent.parentId;

            if (parent.parentId == EXIT_ID)
                break;
        }
    }

    private int createChildSector(int parentSector, int depth) {
        outwardRelation.putIfAbsent(parentSector, new ArrayList<>());
        outwardRelation.get(parentSector).add(++sectorId);

        depthMap.putIfAbsent(depth, new ArrayList<>());
        depthMap.get(depth).add(sectorId);

        return sectorId;
    }

    public List<Integer> getStartStates() {
        return outwardRelation.get(ROOT_ID);
    }

    public void addSolutions(int depth, int solutions) {
        /*numSolutions.putIfAbsent(depth, 0);

        var current = numSolutions.get(depth);
        numSolutions.put(depth, current + solutions);*/
    }

    public int pushState(int depth, int pathLength, int parentSector) {

        // Create sector
        int newSectorId = createChildSector(parentSector, depth);
        stateMap.put(newSectorId, new State(pathLength, /*numChildren, */parentSector));

        // Update total children
        // TODO: Don't actually need this?
        // numSolutions.putIfAbsent(depth, 0);
        // var current = numSolutions.get(depth);
        // numSolutions.put(depth, current + numChildren);

        // Mark dirty (for redraw)
        // We only mark the parent sector as dirty, because when drawing we draw within
        // the bounds of our parent sector. This prevents having to reallocate the entire
        // thing.
        markDirtyAndPropagate(stateMap.get(parentSector));

        return newSectorId;
    }
}
