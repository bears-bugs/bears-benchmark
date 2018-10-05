package org.imdea.vcd.queue;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.imdea.vcd.queue.box.QueueBox;
import org.imdea.vcd.queue.clock.Clock;
import org.imdea.vcd.queue.clock.Dot;
import org.imdea.vcd.queue.clock.Dots;
import org.imdea.vcd.queue.clock.ExceptionSet;
import org.imdea.vcd.queue.clock.MaxInt;

/**
 *
 * @author Vitor Enes
 * @param <E>
 */
public class ConfQueue<E extends QueueBox> implements Queue<E> {

    // these two should always have the same size
    private final HashMap<Dot, E> dotToBox = new HashMap<>();
    private final HashMap<Dot, Clock<MaxInt>> dotToConf = new HashMap<>();
    private List<E> toDeliver = new ArrayList<>();

    private final Clock<ExceptionSet> committed;
    private final Clock<ExceptionSet> delivered;

    public ConfQueue(Integer nodeNumber) {
        this.committed = Clock.eclock(nodeNumber);
        this.delivered = Clock.eclock(nodeNumber);
    }

    public ConfQueue(Clock<ExceptionSet> committed) {
        this.committed = (Clock<ExceptionSet>) committed.clone();
        this.delivered = (Clock<ExceptionSet>) committed.clone();
    }

    @Override
    public boolean isEmpty() {
        return this.dotToConf.isEmpty();
    }

    @Override
    public void add(QueueAddArgs<E> args) {
        // fetch box, dot and conf
        E e = args.getBox();
        Dot dot = args.getDot();
        Clock<MaxInt> conf = args.getConf();

        // update committed
        this.committed.addDot(dot);

        // save box
        this.dotToBox.put(dot, e);
        // save info
        this.dotToConf.put(dot, conf);

        if (this.delivered.contains(dot)) {
            // FOR THE CASE OF FAILURES: just deliver again?
            // this probably shouldn't happen
            Dots scc = new Dots();
            scc.add(dot);
            saveSCC(scc);
            return;
        }

        // try to find an SCC
        findSCC(dot);
    }

    private void findSCC(Dot dot) {
        // if not committed or already delivered, return
        if (!this.committed.contains(dot) || this.delivered.contains(dot)) {
            return;
        }

        TarjanSCCFinder finder = new TarjanSCCFinder();
        FinderResult res = finder.strongConnect(dot);
        switch (res) {
            case FOUND:
                List<Dots> sccs = finder.getSCCs();

                // deliver all sccs by the order they were found
                for (Dots scc : sccs) {
                    saveSCC(scc);
                }

                // try to deliver the next dots after delivered
                for (Dot next : this.delivered.nextDots()) {
                    findSCC(next);
                }
            default:
                break;
        }
    }

    private void saveSCC(Dots scc) {
        // update delivered
        this.delivered.addDots(scc);

        // merge boxes of dots in SCC
        // - remove dot's box and conf along the way
        Iterator<Dot> it = scc.iterator();
        Dot member = it.next();
        E box = this.dotToBox.remove(member);
        resetMember(member);

        while (it.hasNext()) {
            member = it.next();
            box.merge(this.dotToBox.remove(member));
            resetMember(member);
        }

        // add to toDeliver list
        this.toDeliver.add(box);
    }

    private void resetMember(Dot member) {
        this.dotToConf.remove(member);
    }

    @Override
    public List<E> tryDeliver() {
        // return current list to be delivered,
        // and create a new one
        List<E> result = this.toDeliver;
        this.toDeliver = new ArrayList<>();
        return result;
    }

    @Override
    public List<E> toList() {
        throw new UnsupportedOperationException("Method not supported.");
    }

    @Override
    public int size() {
        return this.dotToConf.size();
    }

    @Override
    public int elements() {
        return size();
    }

    private enum FinderResult {
        FOUND, NOT_FOUND, MISSING_DEP
    }

    /**
     * Find a SCC using Tarjan's algorithm.
     *
     * Based on
     * https://github.com/williamfiset/Algorithms/blob/master/com/williamfiset/algorithms/graphtheory/TarjanSccSolverAdjacencyList.java
     *
     */
    private class TarjanSCCFinder {

        private final Deque<Dot> stack;
        private final Map<Dot, Integer> ids;
        private final Map<Dot, Integer> low;
        private final Set<Dot> onStack;
        private Integer index;

        private final List<Dots> sccs;

        public TarjanSCCFinder() {
            this.stack = new ArrayDeque<>();
            this.ids = new HashMap<>();
            this.low = new HashMap<>();
            this.onStack = new HashSet<>();
            this.index = 0;
            this.sccs = new ArrayList();
        }

        public FinderResult strongConnect(Dot v) {
            // get conf
            Clock<MaxInt> conf = dotToConf.get(v);

            // get neighbors: subtract delivered
            Dots deps = new Dots();

            // if not all deps are committed, give up
            for (Dot dep : conf.frontier()) {
                if (!committed.contains(dep)) {
                    return FinderResult.MISSING_DEP;
                }
                if (!delivered.contains(dep)) {
                    deps.add(dep);
                }
            }
            // subtract self
            deps.remove(v);

            // add to the stack
            stack.push(v);
            onStack.add(v);
            // set id and low
            Integer vIndex = index;
            ids.put(v, vIndex);
            low.put(v, vIndex);
            // update id
            index++;

            // for all neighbors
            for (Dot w : deps) {
                // if not visited, visit
                boolean visited = ids.containsKey(w);
                if (!visited) {
                    FinderResult result = strongConnect(w);

                    switch (result) {
                        case MISSING_DEP:
                            // propagate missing dep
                            return result;
                        default:
                            break;
                    }
                    low.put(v, Math.min(low.get(v), low.get(w)));

                } // if visited neighbor is on stack, min lows
                else if (onStack.contains(w)) {
                    low.put(v, Math.min(low.get(v), ids.get(w)));
                }
            }

            // if after visiting all neighbors, an SCC was found if
            // good news: the SCC members are in the stack
            if (Objects.equals(vIndex, low.get(v))) {
                Dots scc = new Dots();

                for (Dot w = stack.pop();; w = stack.pop()) {
                    // remove from stack
                    onStack.remove(w);
                    // add to SCC
                    scc.add(w);

                    // exit if done
                    if (w.equals(v)) {
                        break;
                    }
                }

                // add scc to the set of sccs
                sccs.add(scc);

                return FinderResult.FOUND;
            }

            return FinderResult.NOT_FOUND;
        }

        private List<Dots> getSCCs() {
            return this.sccs;
        }
    }
}
