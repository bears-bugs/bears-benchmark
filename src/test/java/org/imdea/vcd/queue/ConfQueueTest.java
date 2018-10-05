package org.imdea.vcd.queue;

import org.imdea.vcd.queue.box.CommittedQueueBox;
import com.google.protobuf.ByteString;
import org.imdea.vcd.Generator;
import org.imdea.vcd.pb.Proto.Message;
import org.imdea.vcd.queue.clock.Clock;
import org.imdea.vcd.queue.clock.Dot;
import org.imdea.vcd.queue.clock.MaxInt;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.imdea.vcd.queue.clock.Dots;
import org.imdea.vcd.queue.clock.ExceptionSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author Vitor Enes
 */
public class ConfQueueTest {

    public static final int ITERATIONS = 100;

    @Test
    public void testRandom() {
        for (int i = 0; i < ITERATIONS; i++) {
            Integer nodeNumber = 2;
            Map<Dot, Clock<MaxInt>> dotToConf = Generator.dotToConf(nodeNumber);

            List<QueueAddArgs> argsList = new ArrayList<>();
            for (Map.Entry<Dot, Clock<MaxInt>> e : dotToConf.entrySet()) {
                argsList.add(args(e.getKey(), e.getValue()));
            }

            checkTerminationRandomShuffles(nodeNumber, argsList);
        }
    }

    @Test
    public void testFailures() {
        for (int i = 0; i < ITERATIONS; i++) {
            Integer nodeNumber = 2;
            Map<Dot, Clock<MaxInt>> dotToConf;
            do {
                // make sure we don't generate an empty map
                dotToConf = Generator.dotToConf(nodeNumber);
            } while (dotToConf.isEmpty());

            List<QueueAddArgs> argsList = new ArrayList<>();
            for (Map.Entry<Dot, Clock<MaxInt>> e : dotToConf.entrySet()) {
                argsList.add(args(e.getKey(), e.getValue()));
            }

            Collections.shuffle(argsList);

            // create the commit clock after failure
            QueueAddArgs committedArgs = argsList.remove(0);
            Clock<MaxInt> committed = Clock.eclock(committedArgs.getConf());

            checkTerminationRandomShuffles(committed, argsList);
        }
    }

    @Test
    public void testAdd1() {
        Integer nodeNumber = 2;

        // {0, 2}, [2, 2]
        Dot dotA = new Dot(0, 2L);
        Clock<MaxInt> confA = vclock(2L, 2L);

        // {0, 1}, [3, 2]
        Dot dotB = new Dot(0, 1L);
        Clock<MaxInt> confB = vclock(3L, 2L);

        // {0, 5}, [6, 2]
        Dot dotC = new Dot(0, 5L);
        Clock<MaxInt> confC = vclock(6L, 2L);

        // {0, 6}, [6, 3]
        Dot dotD = new Dot(0, 6L);
        Clock<MaxInt> confD = vclock(6L, 3L);

        // {0, 3}, [3, 3]
        Dot dotE = new Dot(0, 3L);
        Clock<MaxInt> confE = vclock(3L, 3L);

        // {1, 2}, [0, 2]
        Dot dotF = new Dot(1, 2L);
        Clock<MaxInt> confF = vclock(0L, 2L);

        // {1, 1}, [4, 3]
        Dot dotG = new Dot(1, 1L);
        Clock<MaxInt> confG = vclock(4L, 3L);

        // {0, 4}, [6, 2]
        Dot dotH = new Dot(0, 4L);
        Clock<MaxInt> confH = vclock(6L, 2L);

        // {1, 3}, [6, 3]
        Dot dotI = new Dot(1, 3L);
        Clock<MaxInt> confI = vclock(6L, 3L);

        List<QueueAddArgs> argsList = new ArrayList<>();
        argsList.add(args(dotA, confA));
        argsList.add(args(dotB, confB));
        argsList.add(args(dotC, confC));
        argsList.add(args(dotD, confD));
        argsList.add(args(dotE, confE));
        argsList.add(args(dotF, confF));
        argsList.add(args(dotG, confG));
        argsList.add(args(dotH, confH));
        argsList.add(args(dotI, confI));

        checkTerminationRandomShuffles(nodeNumber, argsList);
    }

    @Test
    public void testAdd2() {
        Integer nodeNumber = 2;

        // {1, 4}, [3, 4]
        Dot dotA = new Dot(1, 4L);
        Clock<MaxInt> confA = vclock(3L, 4L);

        // {1, 3}, [0, 3]
        Dot dotB = new Dot(1, 3L);
        Clock<MaxInt> confB = vclock(0L, 3L);

        // {0, 3}, [3, 3]
        Dot dotC = new Dot(0, 3L);
        Clock<MaxInt> confC = vclock(3L, 3L);

        // {0, 1}, [3, 4]
        Dot dotD = new Dot(0, 1L);
        Clock<MaxInt> confD = vclock(3L, 4L);

        // {1, 2}, [0, 2]
        Dot dotE = new Dot(1, 2L);
        Clock<MaxInt> confE = vclock(0L, 2L);

        // {0, 2}, [3, 3]
        Dot dotF = new Dot(0, 2L);
        Clock<MaxInt> confF = vclock(3L, 3L);

        // {1, 1}, [3, 3]
        Dot dotG = new Dot(1, 1L);
        Clock<MaxInt> confG = vclock(3L, 3L);

        List<QueueAddArgs> argsList = new ArrayList<>();
        argsList.add(args(dotA, confA));
        argsList.add(args(dotB, confB));
        argsList.add(args(dotC, confC));
        argsList.add(args(dotD, confD));
        argsList.add(args(dotE, confE));
        argsList.add(args(dotF, confF));
        argsList.add(args(dotG, confG));

        checkTerminationRandomShuffles(nodeNumber, argsList);
    }

    @Test
    public void testAdd3() {
        Integer nodeNumber = 3;

        // {{2, 2}, [1, 0, 2]
        Dot dotA = new Dot(2, 2L);
        Clock<MaxInt> confA = vclock(1L, 0L, 2L);

        // {{2, 3}, [1, 1, 3]
        Dot dotB = new Dot(2, 3L);
        Clock<MaxInt> confB = vclock(1L, 1L, 3L);

        // {{2, 1}, [1, 1, 3]
        Dot dotC = new Dot(2, 1L);
        Clock<MaxInt> confC = vclock(1L, 1L, 3L);

        // {{0, 1}, [1, 0, 0]
        Dot dotD = new Dot(0, 1L);
        Clock<MaxInt> confD = vclock(1L , 0L, 0L);

        // {{1, 1}, [1, 1, 2]
        Dot dotE = new Dot(1, 1L);
        Clock<MaxInt> confE = vclock(1L, 1L, 2L);

        List<QueueAddArgs> argsList = new ArrayList<>();
        argsList.add(args(dotA, confA));
        argsList.add(args(dotB, confB));
        argsList.add(args(dotC, confC));
        argsList.add(args(dotD, confD));
        argsList.add(args(dotE, confE));

        checkTerminationRandomShuffles(nodeNumber, argsList);
    }

    @Test
    public void testAdd4() {
        Integer nodeNumber = 1;

        // {{0, 5}, [5]
        Dot dotA = new Dot(0, 5L);
        Clock<MaxInt> confA = vclock(5L);

        // {{0, 4}, [6]
        Dot dotB = new Dot(0, 4L);
        Clock<MaxInt> confB = vclock(6L);

        // {{0, 1}, [5]
        Dot dotC = new Dot(0, 1L);
        Clock<MaxInt> confC = vclock(5L);

        // {{0, 2}, [6]
        Dot dotD = new Dot(0, 2L);
        Clock<MaxInt> confD = vclock(6L);

        // {{0, 3}, [5]
        Dot dotE = new Dot(0, 3L);
        Clock<MaxInt> confE = vclock(5L);

        // {{0, 6}, [6]
        Dot dotF = new Dot(0, 6L);
        Clock<MaxInt> confF = vclock(6L);

        List<QueueAddArgs> argsList = new ArrayList<>();
        argsList.add(args(dotA, confA));
        argsList.add(args(dotB, confB));
        argsList.add(args(dotC, confC));
        argsList.add(args(dotD, confD));
        argsList.add(args(dotE, confE));
        argsList.add(args(dotF, confF));

        checkTerminationRandomShuffles(nodeNumber, argsList);
    }

    @Test
    public void testAdd5() {
        Integer nodeNumber = 2;

        // {{0, 1}, [1, 1]
        Dot dotA = new Dot(0, 1L);
        Clock<MaxInt> confA = vclock(1L, 1L);

        // {{0, 2}, [2, 0]
        Dot dotB = new Dot(0, 2L);
        Clock<MaxInt> confB = vclock(2L, 0L);

        // {{1, 1}, [1, 1]
        Dot dotC = new Dot(1, 1L);
        Clock<MaxInt> confC = vclock(1L, 1L);

        List<QueueAddArgs> argsList = new ArrayList<>();
        argsList.add(args(dotA, confA));
        argsList.add(args(dotB, confB));
        argsList.add(args(dotC, confC));

        checkTerminationRandomShuffles(nodeNumber, argsList);
    }

    @Test
    public void testAdd6() {
        Integer nodeNumber = 2;

        // {{0, 1}, [1, 0]
        Dot dotA = new Dot(0, 1L);
        Clock<MaxInt> confA = vclock(1L, 0L);

        // {{0, 2}, [4, 1]
        Dot dotB = new Dot(0, 2L);
        Clock<MaxInt> confB = vclock(4L, 1L);

        // {{0, 3}, [3, 0]
        Dot dotC = new Dot(0, 3L);
        Clock<MaxInt> confC = vclock(3L, 0L);

        // {{0, 4}, [4, 0]
        Dot dotD = new Dot(0, 4L);
        Clock<MaxInt> confD = vclock(4L, 0L);

        // {{1, 1}, [0, 1]
        Dot dotE = new Dot(1, 1L);
        Clock<MaxInt> confE = vclock(0L, 1L);

        // {{1, 2}, [3, 2]
        Dot dotF = new Dot(1, 2L);
        Clock<MaxInt> confF = vclock(3L, 2L);

        List<QueueAddArgs> argsList = new ArrayList<>();
        argsList.add(args(dotA, confA));
        argsList.add(args(dotB, confB));
        argsList.add(args(dotC, confC));
        argsList.add(args(dotD, confD));
        argsList.add(args(dotE, confE));
        argsList.add(args(dotF, confF));

        checkTerminationRandomShuffles(nodeNumber, argsList);
    }

    @Test
    public void testFailure1() {
        // {0, 1} [4, 0]
        Dot dotA = new Dot(0, 1L);
        Clock<MaxInt> confA = vclock(4L, 0L);

        // {0, 3} [3, 0]
        Dot dotB = new Dot(0, 3L);
        Clock<MaxInt> confB = vclock(3L, 0L);

        // {0, 4} [4, 0]
        Dot dotC = new Dot(0, 4L);
        Clock<MaxInt> confC = vclock(4L, 0L);

        // [2, 0]
        Clock<ExceptionSet> delivered = eclock(2L, 0L);

        List<QueueAddArgs> argsList = new ArrayList<>();
        argsList.add(args(dotA, confA));
        argsList.add(args(dotB, confB));
        argsList.add(args(dotC, confC));

        checkTerminationRandomShuffles(delivered, argsList);
    }

    private void checkTerminationRandomShuffles(Object queueArg, List<QueueAddArgs> argsList) {
        List<List<QueueAddArgs>> permutations = Permutations.of(argsList);
        Map<Dots, List<Message>> totalOrder = checkTermination(queueArg, argsList);

        for (int i = 0; i < permutations.size(); i++) {
            Map<Dots, List<Message>> sorted = checkTermination(queueArg, permutations.get(i));
            checkTotalOrderPerColor(totalOrder, sorted);
        }
    }

    private Map<Dots, List<Message>> checkTermination(Object queueArg, List<QueueAddArgs> argsList) {
        ConfQueue<CommittedQueueBox> queue;
        if (queueArg instanceof Integer) {
            queue = new ConfQueue<>((Integer) queueArg);
        } else {
            queue = new ConfQueue<>((Clock<ExceptionSet>) queueArg);
        }

        return checkTermination(queue, argsList);
    }

    private Map<Dots, List<Message>> checkTermination(ConfQueue<CommittedQueueBox> queue, List<QueueAddArgs> argsList) {
        List<CommittedQueueBox> results = new ArrayList<>();
        for (QueueAddArgs args : argsList) {
            queue.add((QueueAddArgs) args.clone());
            List<CommittedQueueBox> result = queue.tryDeliver();
            results.addAll(result);
        }

        // check queue is empty and all dots were delivered
        boolean emptyQueue = queue.isEmpty() && queue.size() == 0 && queue.elements() == 0;
        boolean allDots = checkAllDotsDelivered(argsList, results);
        boolean termination = emptyQueue && allDots;

        if (!termination) {
            System.out.println(argsList);
        }
        assertTrue(termination);

        // return messages sorted
        Map<Dots, List<Message>> sorted = new HashMap<>();
        for (CommittedQueueBox box : results) {
            sorted.put(box.getDots(), box.sortMessages());
        }
        return sorted;
    }

    private boolean checkAllDotsDelivered(List<QueueAddArgs> argsList, List<CommittedQueueBox> results) {
        List<CommittedQueueBox> boxes = new ArrayList<>();
        for (QueueAddArgs args : argsList) {
            boxes.add((CommittedQueueBox) args.getBox());
        }

        List<Dot> boxesDots = boxListToDots(boxes);
        List<Dot> resultsDots = boxListToDots(results);

        // all dots (and no more) were delivered
        return boxesDots.size() == resultsDots.size() && resultsDots.containsAll(boxesDots);
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

    private QueueAddArgs args(Dot dot, Clock<MaxInt> conf) {
        // build random message
        Message message = Generator.message();
        return args(dot, message, conf);
    }

    private QueueAddArgs args(Dot dot, Message message, Clock<MaxInt> conf) {
        CommittedQueueBox box = new CommittedQueueBox(dot, null, message, conf);
        QueueAddArgs args = new QueueAddArgs(dot, conf, box);
        return args;
    }

    private void checkTotalOrderPerColor(Map<Dots, List<Message>> ma, Map<Dots, List<Message>> mb) {
        for (Map.Entry<Dots, List<Message>> entry : ma.entrySet()) {
            List<Message> a = entry.getValue();
            List<Message> b = mb.get(entry.getKey());
            checkTotalOrderPerColor(a, b);
        }
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

    public Clock<MaxInt> vclock(Long... seqs) {
        HashMap<Integer, MaxInt> map = new HashMap<>();
        for (Integer i = 0; i < seqs.length; i++) {
            map.put(i, new MaxInt(seqs[i]));
        }
        return new Clock<>(map);
    }

    public Clock<ExceptionSet> eclock(Long... seqs) {
        HashMap<Integer, ExceptionSet> map = new HashMap<>();
        for (Integer i = 0; i < seqs.length; i++) {
            map.put(i, new ExceptionSet(seqs[i]));
        }
        return new Clock<>(map);
    }

}
