package org.imdea.vcd.queue;

import org.imdea.vcd.queue.box.CommittedQueueBox;
import com.google.protobuf.ByteString;
import org.imdea.vcd.Generator;
import org.imdea.vcd.pb.Proto.Message;
import org.imdea.vcd.queue.clock.Clock;
import org.imdea.vcd.queue.clock.Dot;
import org.imdea.vcd.queue.clock.ExceptionSet;
import org.imdea.vcd.queue.clock.MaxInt;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author Vitor Enes
 */
public class DepQueueTest {

    @Test
    public void testAdd1() {
        Integer nodeNumber = 2;

        // {{0, 2}, [{0, 2}, {1, 2}]}
        Dot dotA = new Dot(0, 2L);
        HashMap<Integer, ExceptionSet> mapA = new HashMap<>();
        mapA.put(0, new ExceptionSet(2L, 1L));
        mapA.put(1, new ExceptionSet(2L, 1L));

        // {{0, 1}, [{0, 1}, {0, 2}, {0, 3}, {1, 2}]}
        Dot dotB = new Dot(0, 1L);
        HashMap<Integer, ExceptionSet> mapB = new HashMap<>();
        mapB.put(0, new ExceptionSet(3L));
        mapB.put(1, new ExceptionSet(2L, 1L));

        // {{0, 5}, [{0, 1}, {0, 2}, {0, 3}, {0, 4}, {0, 5}, {0, 6}, {1, 1}, {1, 2}]}
        Dot dotC = new Dot(0, 5L);
        HashMap<Integer, ExceptionSet> mapC = new HashMap<>();
        mapC.put(0, new ExceptionSet(6L));
        mapC.put(1, new ExceptionSet(2L));

        // {{0, 6}, [{0, 1}, {0, 2}, {0, 3}, {0, 4}, {0, 5}, {0, 6}, {1, 1}, {1, 2}, {1, 3}]}
        Dot dotD = new Dot(0, 6L);
        HashMap<Integer, ExceptionSet> mapD = new HashMap<>();
        mapD.put(0, new ExceptionSet(6L));
        mapD.put(1, new ExceptionSet(3L));

        // {{0, 3}, [{0, 1}, {0, 2}, {0, 3}, {1, 1}, {1, 2}, {1, 3}]}
        Dot dotE = new Dot(0, 3L);
        HashMap<Integer, ExceptionSet> mapE = new HashMap<>();
        mapE.put(0, new ExceptionSet(3L));
        mapE.put(1, new ExceptionSet(3L));

        // {{1, 2}, [{1, 2}]}
        Dot dotF = new Dot(1, 2L);
        HashMap<Integer, ExceptionSet> mapF = new HashMap<>();
        mapF.put(0, new ExceptionSet(0L));
        mapF.put(1, new ExceptionSet(2L, 1L));

        // {{1, 1}, [{0, 1}, {0, 2}, {0, 3}, {0, 4}, {1, 1}, {1, 2}, {1, 3}]}
        Dot dotG = new Dot(1, 1L);
        HashMap<Integer, ExceptionSet> mapG = new HashMap<>();
        mapG.put(0, new ExceptionSet(4L));
        mapG.put(1, new ExceptionSet(3L));

        // {{0, 4}, [{0, 1}, {0, 2}, {0, 3}, {0, 4}, {0, 6}, {1, 1}, {1, 2}]}
        Dot dotH = new Dot(0, 4L);
        HashMap<Integer, ExceptionSet> mapH = new HashMap<>();
        mapH.put(0, new ExceptionSet(6L, 5L));
        mapH.put(1, new ExceptionSet(2L));

        // {{1, 3}, [{0, 1}, {0, 2}, {0, 3}, {0, 4}, {0, 5}, {0, 6}, {1, 1}, {1, 2}, {1, 3}]}
        Dot dotI = new Dot(1, 3L);
        HashMap<Integer, ExceptionSet> mapI = new HashMap<>();
        mapI.put(0, new ExceptionSet(6L));
        mapI.put(1, new ExceptionSet(3L));

        List<QueueAddArgs> argsList = new ArrayList<>();
        argsList.add(args(dotA, mapA));
        argsList.add(args(dotB, mapB));
        argsList.add(args(dotC, mapC));
        argsList.add(args(dotD, mapD));
        argsList.add(args(dotE, mapE));
        argsList.add(args(dotF, mapF));
        argsList.add(args(dotG, mapG));
        argsList.add(args(dotH, mapH));
        argsList.add(args(dotI, mapI));

        checkTerminationRandomShuffles(nodeNumber, argsList);
    }

    @Test
    public void testAdd2() {
        Integer nodeNumber = 2;

        // {{1, 4}, [{0, 2}, {0, 3}, {1, 1}, {1, 2}, {1, 3}, {1, 4}]}
        Dot dotA = new Dot(1, 4L);
        HashMap<Integer, ExceptionSet> mapA = new HashMap<>();
        mapA.put(0, new ExceptionSet(3L, 1L));
        mapA.put(1, new ExceptionSet(4L));

        // {{1, 3}, [{1, 2}, {1, 3}]}
        Dot dotB = new Dot(1, 3L);
        HashMap<Integer, ExceptionSet> mapB = new HashMap<>();
        mapB.put(0, new ExceptionSet());
        mapB.put(1, new ExceptionSet(3L, 1L));

        // {{0, 3}, [{0, 3}, {1, 2}, {1, 3}]}
        Dot dotC = new Dot(0, 3L);
        HashMap<Integer, ExceptionSet> mapC = new HashMap<>();
        mapC.put(0, new ExceptionSet(3L, 1L, 2L));
        mapC.put(1, new ExceptionSet(3L, 1L));

        // {{0, 1}, [{0, 1}, {0, 2}, {0, 3}, {1, 1}, {1, 2}, {1, 3}, {1, 4}]}
        Dot dotD = new Dot(0, 1L);
        HashMap<Integer, ExceptionSet> mapD = new HashMap<>();
        mapD.put(0, new ExceptionSet(3L));
        mapD.put(1, new ExceptionSet(4L));

        // {{1, 2}, [{1, 2}]}
        Dot dotE = new Dot(1, 2L);
        HashMap<Integer, ExceptionSet> mapE = new HashMap<>();
        mapE.put(0, new ExceptionSet());
        mapE.put(1, new ExceptionSet(2L, 1L));

        // {{0, 2}, [{0, 1}, {0, 2}, {0, 3}, {1, 1}, {1, 2}, {1, 3}]}
        Dot dotF = new Dot(0, 2L);
        HashMap<Integer, ExceptionSet> mapF = new HashMap<>();
        mapF.put(0, new ExceptionSet(3L));
        mapF.put(1, new ExceptionSet(3L));

        // {{1, 1}, [{0, 1}, {0, 3}, {1, 1}, {1, 2}, {1, 3}]}
        Dot dotG = new Dot(1, 1L);
        HashMap<Integer, ExceptionSet> mapG = new HashMap<>();
        mapG.put(0, new ExceptionSet(3L, 2L));
        mapG.put(1, new ExceptionSet(3L));

        List<QueueAddArgs> argsList = new ArrayList<>();
        argsList.add(args(dotA, mapA));
        argsList.add(args(dotB, mapB));
        argsList.add(args(dotC, mapC));
        argsList.add(args(dotD, mapD));
        argsList.add(args(dotE, mapE));
        argsList.add(args(dotF, mapF));
        argsList.add(args(dotG, mapG));

        checkTerminationRandomShuffles(nodeNumber, argsList);
    }

    @Test
    public void testAdd3() {
        Integer nodeNumber = 3;

        // {{2, 2}, [{0, 1}, {2, 2}]}
        Dot dotA = new Dot(2, 2L);
        HashMap<Integer, ExceptionSet> mapA = new HashMap<>();
        mapA.put(0, new ExceptionSet(1L));
        mapA.put(1, new ExceptionSet());
        mapA.put(2, new ExceptionSet(2L, 1L));

        // {{2, 3}, [{0, 1}, {1, 1}, {2, 2}, {2, 3}]}
        Dot dotB = new Dot(2, 3L);
        HashMap<Integer, ExceptionSet> mapB = new HashMap<>();
        mapB.put(0, new ExceptionSet(1L));
        mapB.put(1, new ExceptionSet(1L));
        mapB.put(2, new ExceptionSet(3L, 1L));

        // {{2, 1}, [{0, 1}, {1, 1}, {2, 1}, {2, 2}, {2, 3}]}
        Dot dotC = new Dot(2, 1L);
        HashMap<Integer, ExceptionSet> mapC = new HashMap<>();
        mapC.put(0, new ExceptionSet(1L));
        mapC.put(1, new ExceptionSet(1L));
        mapC.put(2, new ExceptionSet(3L));

        // {{0, 1}, [{0, 1}]}
        Dot dotD = new Dot(0, 1L);
        HashMap<Integer, ExceptionSet> mapD = new HashMap<>();
        mapD.put(0, new ExceptionSet(1L));
        mapD.put(1, new ExceptionSet());
        mapD.put(2, new ExceptionSet());

        // {{1, 1}, [{0, 1}, {1, 1}, {2, 2}]}
        Dot dotE = new Dot(1, 1L);
        HashMap<Integer, ExceptionSet> mapE = new HashMap<>();
        mapE.put(0, new ExceptionSet(1L));
        mapE.put(1, new ExceptionSet(1L));
        mapE.put(2, new ExceptionSet(2L, 1L));

        List<QueueAddArgs> argsList = new ArrayList<>();
        argsList.add(args(dotA, mapA));
        argsList.add(args(dotB, mapB));
        argsList.add(args(dotC, mapC));
        argsList.add(args(dotD, mapD));
        argsList.add(args(dotE, mapE));

        checkTerminationRandomShuffles(nodeNumber, argsList);
    }

    @Test
    public void testAdd4() {
        Integer nodeNumber = 1;

        // {{0, 5}, [{0, 5}]}
        Dot dotA = new Dot(0, 5L);
        HashMap<Integer, ExceptionSet> mapA = new HashMap<>();
        mapA.put(0, new ExceptionSet(5L, 1L, 2L, 3L, 4L));

        // {{0, 4}, [{0, 1}, {0, 3}, {0, 4}, {0, 5}, {0, 6}]}
        Dot dotB = new Dot(0, 4L);
        HashMap<Integer, ExceptionSet> mapB = new HashMap<>();
        mapB.put(0, new ExceptionSet(6L, 2L));

        // {{0, 1}, [{0, 1}, {0, 3}, {0, 5}]}
        Dot dotC = new Dot(0, 1L);
        HashMap<Integer, ExceptionSet> mapC = new HashMap<>();
        mapC.put(0, new ExceptionSet(5L, 2L, 4L));

        // {{0, 2}, [{0, 1}, {0, 2}, {0, 3}, {0, 4}, {0, 5}, {0, 6}]}
        Dot dotD = new Dot(0, 2L);
        HashMap<Integer, ExceptionSet> mapD = new HashMap<>();
        mapD.put(0, new ExceptionSet(6L));

        // {{0, 3}, [{0, 3}, {0, 5}]}
        Dot dotE = new Dot(0, 3L);
        HashMap<Integer, ExceptionSet> mapE = new HashMap<>();
        mapE.put(0, new ExceptionSet(5L, 1L, 2L, 4L));

        // {{0, 6}, [{0, 1}, {0, 3}, {0, 5}, {0, 6}]}
        Dot dotF = new Dot(0, 6L);
        HashMap<Integer, ExceptionSet> mapF = new HashMap<>();
        mapF.put(0, new ExceptionSet(6L, 2L, 4L));

        List<QueueAddArgs> argsList = new ArrayList<>();
        argsList.add(args(dotA, mapA));
        argsList.add(args(dotB, mapB));
        argsList.add(args(dotC, mapC));
        argsList.add(args(dotD, mapD));
        argsList.add(args(dotE, mapE));
        argsList.add(args(dotF, mapF));

        checkTerminationRandomShuffles(nodeNumber, argsList);
    }

    private void checkTerminationRandomShuffles(Integer nodeNumber, List<QueueAddArgs> argsList) {
        List<List<QueueAddArgs>> permutations = Permutations.of(argsList);
        List<Message> totalOrder = checkTermination(nodeNumber, argsList);

        for (int i = 0; i < permutations.size(); i++) {
            List<Message> sorted = checkTermination(nodeNumber, permutations.get(i));
            checkTotalOrderPerColor(totalOrder, sorted);
        }
    }

    private List<Message> checkTermination(Integer nodeNumber, List<QueueAddArgs> argsList) {
        DepQueue<CommittedQueueBox> queue = new DepQueue<>(nodeNumber);
        List<CommittedQueueBox> results = new ArrayList<>();
        for (QueueAddArgs args : argsList) {
            queue.add((QueueAddArgs) args.clone());
            List<CommittedQueueBox> result = queue.tryDeliver();
            results.addAll(result);
        }

        // check queue is empty and all dots were delivered
        assertTrue(queue.isEmpty() && queue.size() == 0 && queue.elements() == 0);
        checkAllDotsDelivered(argsList, results);

        // return messages sorted
        List<Message> sorted = new ArrayList<>();
        for (CommittedQueueBox box : results) {
            sorted.addAll(box.sortMessages());
        }
        return sorted;
    }

    private void checkAllDotsDelivered(List<QueueAddArgs> argsList, List<CommittedQueueBox> results) {
        List<CommittedQueueBox> boxes = new ArrayList<>();
        for (QueueAddArgs args : argsList) {
            boxes.add((CommittedQueueBox) args.getBox());
        }

        List<Dot> boxesDots = boxListToDots(boxes);
        List<Dot> resultsDots = boxListToDots(results);

        // all dots (and no more) were delivered
        assertTrue(boxesDots.size() == resultsDots.size());
        for (Dot dot : boxesDots) {
            assertTrue(resultsDots.contains(dot));
        }
    }

    private List<Dot> boxListToDots(List<CommittedQueueBox> list) {
        List<Dot> dots = new ArrayList<>();
        for (CommittedQueueBox e : list) {
            for (Dot dot : e.getDots()) {
                dots.add(dot);
            }
        }
        return dots;
    }

    private QueueAddArgs args(Dot dot, HashMap<Integer, ExceptionSet> depMap) {
        // build random message
        Message message = Generator.message();

        // create conf, given dep
        HashMap<Integer, MaxInt> confMap = new HashMap<>();
        for (Map.Entry<Integer, ExceptionSet> entry : depMap.entrySet()) {
            confMap.put(entry.getKey(), entry.getValue().toMaxInt());
        }
        Clock<MaxInt> conf = new Clock<>(confMap);
        Clock<ExceptionSet> dep = new Clock<>(depMap);

        CommittedQueueBox box = new CommittedQueueBox(dot, dep, message, conf);
        QueueAddArgs args = new QueueAddArgs(dot, conf, box);
        return args;
    }

    private void checkTotalOrderPerColor(List<Message> a, List<Message> b) {
        HashSet<ByteString> colors = new HashSet<>();

        for (Message m : a) {
            colors.add(m.getHashes(0));
        }

        for (ByteString color : colors) {
            List<Message> perColorA = messagesPerColor(color, a);
            List<Message> perColorB = messagesPerColor(color, b);
            assertEquals(perColorA, perColorB);
        }
    }

    private List<Message> messagesPerColor(ByteString color, List<Message> l) {
        List<Message> perColor = new ArrayList<>();
        for (Message m : l) {
            if (m.getHashes(0).equals(color)) {
                perColor.add(m);
            }
        }
        return perColor;
    }
}
