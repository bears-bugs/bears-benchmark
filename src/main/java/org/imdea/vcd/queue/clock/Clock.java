package org.imdea.vcd.queue.clock;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import org.imdea.vcd.pb.Proto;

/**
 *
 * @author Vitor Enes
 * @param <T>
 */
public class Clock<T extends IntSet> {

    private final HashMap<Integer, T> map;

    public Clock(HashMap<Integer, T> map) {
        this.map = map;
    }

    public Clock(Clock<T> clock) {
        this.map = new HashMap<>();
        for (Map.Entry<Integer, T> entry : clock.map.entrySet()) {
            this.map.put(entry.getKey(), (T) entry.getValue().clone());
        }
    }

    public Clock(Integer nodeNumber, T bottom) {
        this.map = new HashMap<>();
        for (Integer actor = 0; actor < nodeNumber; actor++) {
            this.map.put(actor, (T) bottom.clone());
        }
    }

    public IntSet get(Integer id) {
        return this.map.get(id);
    }

    public boolean isBottom() {
        for (T t : this.map.values()) {
            if (!t.isBottom()) {
                return false;
            }
        }

        return true;
    }

    public Dots frontier() {
        Dots dots = new Dots();
        for (Map.Entry<Integer, T> entry : this.map.entrySet()) {
            Dot dot = new Dot(entry.getKey(), entry.getValue().current());
        }
        return dots;
    }

    public boolean contains(Dot dot) {
        return this.map.get(dot.getId()).contains(dot.getSeq());
    }

    public void merge(Clock<T> clock) {
        BiFunction<T, T, T> f = (a, b) -> {
            a.merge(b);
            return a;
        };
        for (Map.Entry<Integer, T> entry : clock.map.entrySet()) {
            this.map.merge(entry.getKey(), entry.getValue(), f);
        }
    }

    public boolean intersects(Dots dots) {
        for (Dot dot : dots) {
            if (this.contains(dot)) {
                return true;
            }
        }
        return false;
    }

    public Dots subtract(Clock<T> clock) {
        Dots dots = new Dots();

        for (Map.Entry<Integer, T> entry : this.map.entrySet()) {
            // get actor
            Integer actor = entry.getKey();

            // subtract b from a
            T a = entry.getValue();
            T b = clock.map.get(actor);
            List<Long> seqs = a.subtract(b);

            // create dots from subtract result
            for (Long seq : seqs) {
                Dot dot = new Dot(actor, seq);
                dots.add(dot);
            }
        }

        return dots;
    }

    public boolean subtractIsBottom(Clock<T> clock) {
        for (Map.Entry<Integer, T> entry : this.map.entrySet()) {
            // get actor
            Integer actor = entry.getKey();

            // subtract b from a
            T a = entry.getValue();
            T b = clock.map.get(actor);

            if (!a.subtractIsBottom(b)) {
                return false;
            }
        }

        return true;
    }

    public void addDot(Dot dot) {
        this.map.get(dot.getId()).add(dot.getSeq());
    }

    public void addDots(Dots dots) {
        for (Dot dot : dots) {
            addDot(dot);
        }
    }

    public int size() {
        return this.map.size();
    }

    public Dots nextDots() {
        Dots dots = new Dots();

        for (Map.Entry<Integer, T> entry : this.map.entrySet()) {
            Dot dot = new Dot(entry.getKey(), entry.getValue().next());
            dots.add(dot);
        }

        return dots;
    }

    @Override
    public boolean equals(Object o) {
        // self check
        if (this == o) {
            return true;
        }
        // null check
        if (o == null) {
            return false;
        }
        // type check and cast
        if (getClass() != o.getClass()) {
            return false;
        }
        Clock<T> t = (Clock<T>) o;
        return this.map.equals(t.map);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (T t : this.map.values()) {
            sb.append(t).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");
        return sb.toString();
    }

    @Override
    public Object clone() {
        Clock<T> clock = new Clock(this);
        return clock;
    }

    public static Clock<MaxInt> vclock(Integer nodeNumber) {
        HashMap<Integer, MaxInt> map = new HashMap<>();
        for (int id = 0; id < nodeNumber; id++) {
            map.put(id, new MaxInt());
        }
        Clock<MaxInt> clock = new Clock<>(map);
        return clock;
    }

    public static Clock<MaxInt> vclock(Map<Integer, Long> o) {
        HashMap<Integer, MaxInt> map = new HashMap<>();
        for (Map.Entry<Integer, Long> entry : o.entrySet()) {
            map.put(entry.getKey(), new MaxInt(entry.getValue()));
        }
        Clock<MaxInt> clock = new Clock<>(map);
        return clock;
    }

    public static Clock<ExceptionSet> eclock(Integer nodeNumber) {
        HashMap<Integer, ExceptionSet> map = new HashMap<>();
        for (int id = 0; id < nodeNumber; id++) {
            map.put(id, new ExceptionSet());
        }
        Clock<ExceptionSet> clock = new Clock<>(map);
        return clock;
    }

    public static Clock<ExceptionSet> eclock(Map<Integer, Proto.ExceptionSet> o) {
        HashMap<Integer, ExceptionSet> map = new HashMap<>();
        for (Map.Entry<Integer, Proto.ExceptionSet> entry : o.entrySet()) {
            map.put(entry.getKey(), new ExceptionSet(entry.getValue()));
        }
        Clock<ExceptionSet> clock = new Clock<>(map);
        return clock;
    }

    public static Clock<ExceptionSet> eclock(Clock<MaxInt> conf) {
        HashMap<Integer, ExceptionSet> map = new HashMap<>();
        for (Map.Entry<Integer, MaxInt> entry : conf.map.entrySet()) {
            map.put(entry.getKey(), entry.getValue().toExceptionSet());
        }
        Clock<ExceptionSet> clock = new Clock<>(map);
        return clock;
    }
}
