package org.imdea.vcd.queue.clock;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;

/**
 *
 * @author Vitor Enes
 */
public class Dots implements Iterable<Dot> {

    private final HashSet<Dot> set;

    public Dots() {
        this.set = new HashSet<>();
    }

    public Dots(Dot dot) {
        this.set = new HashSet<>(Arrays.asList(dot));
    }

    public Dots(Dots dots) {
        this.set = new HashSet<>();
        for (Dot dot : dots.set) {
            this.set.add((Dot) dot.clone());
        }
    }

    public void add(Dot dot) {
        this.set.add(dot);
    }

    public void remove(Dot dot) {
        this.set.remove(dot);
    }

    public void merge(Dots dots) {
        this.set.addAll(dots.set);
    }

    public boolean contains(Dot dot) {
        return this.set.contains(dot);
    }

    public boolean isEmpty() {
        return this.set.isEmpty();
    }

    public int size() {
        return this.set.size();
    }

    @Override
    public Iterator<Dot> iterator() {
        return this.set.iterator();
    }

    @Override
    public String toString() {
        return this.set.toString();
    }

    @Override
    public Object clone() {
        Dots dots = new Dots(this);
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
        Dots t = (Dots) o;
        return Objects.equals(this.set, t.set);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.set);
        return hash;
    }
}
