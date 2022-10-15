package com.team01.scheduler.visualizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CumulativeTree {

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

    // TODO: Implement clustering (average out child solutions at higher depths)


    public static class State {
        // Drawable
        public int depth;
        public int pathLength;
        public State parent;

        // Layout
        public double startAngle;
        public double endAngle;
        public double xPos;
        public double yPos;

        // Dirty
        public boolean dirty;
        public boolean dirtyChild;

        public State(int pathLength, int depth, State parent) {
            this.pathLength = pathLength;
            this.depth = depth;
            this.parent = parent;
        }
    }

    // Map between sectorId and sector description
    public Map<Integer, State> stateMap = new HashMap<>();

    // Map between sector and child sectors
    public Map<State, List<State>> outwardRelation = new HashMap<>();

    // Map between depth and sectorId
    public Map<Integer, List<State>> depthMap = new HashMap<>();
    List<State> startStates = new ArrayList<>();

    private int sectorId = ROOT_ID;

    State rootState;

    public static final int ROOT_ID = 0;
    public static final int INITIAL_DEPTH = 1;

    public CumulativeTree() {
        rootState = new State(0, 0, null);
        stateMap.put(ROOT_ID, rootState);
    }

    private void markDirtyAndPropagate(State state) {
        state.dirty = true;
        state = state.parent;

        while (state != null) {
            state.dirtyChild = true;
            state = state.parent;
        }
    }

    private int createChildSector(int pathLength, int depth, State parent) {
        var childState = new State(pathLength, depth, parent);

        if (parent != null) {
            outwardRelation.putIfAbsent(parent, new ArrayList<>());
            outwardRelation.get(parent).add(childState);
        } else {
            startStates.add(childState);
        }

        depthMap.putIfAbsent(depth, new ArrayList<>());
        depthMap.get(depth).add(childState);

        stateMap.put(++sectorId, childState);

        return sectorId;
    }

    public List<State> getStartStates() {
        return startStates;
    }

    public int pushState(int depth, int pathLength, int parentSector) {

        if (parentSector == ROOT_ID) {
            int id;
            synchronized (this) {
                id = createChildSector(pathLength, 1, null);
            }
            return id;
        }

        int newSectorId;

        // Create sector
        synchronized (this) {
            var parent = stateMap.get(parentSector);
            newSectorId = createChildSector(pathLength, depth, parent);

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
        }

        return newSectorId;
    }
}
