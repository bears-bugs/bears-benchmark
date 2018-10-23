/*
 * Copyright 2015 MiLaboratory.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.milaboratory.core.clustering;

import gnu.trove.procedure.TObjectProcedure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 * Representation of single cluster.
 *
 * @param <T>
 */
public final class Cluster<T> implements java.io.Serializable {
    final T head;
    final Cluster<T> parent;
    ArrayList<Cluster<T>> children = null;

    public Cluster(T head) {
        this.head = head;
        this.parent = null;
    }

    public Cluster(T head, Cluster<T> parent) {
        this.head = head;
        this.parent = parent;
    }

    public T getHead() {
        return head;
    }

    public Cluster<T> getRoot() {
        Cluster<T> current = parent;
        while (current.parent != null)
            current = current.parent;
        return current;
    }

    public void add(Cluster<T> t) {
        if (children == null)
            children = new ArrayList<>();

        assert t.parent == this;
        children.add(t);
    }

    public int totalCount() {
        int r = 1;
        if (children != null)
            for (Cluster<T> child : children)
                r += child.totalCount();
        return r;
    }

    //do not process this cluster
    public void processAllChildren(TObjectProcedure<Cluster<T>> procedure) {
        if (children == null)
            return;
        for (Cluster<T> child : children) {
            procedure.execute(child);
            child.processAllChildren(procedure);
        }
    }

    void sort(final Comparator<Cluster<T>> comparator) {
        if (children != null) {
            Collections.sort(children, comparator);
            for (Cluster<T> cl : children)
                cl.sort(comparator);
        }
    }

    int size() {
        if (children != null)
            return children.size();
        return 0;
    }

    static <T> ArrayList<T> getAll(Cluster<T>... clusters) {
        ArrayList<T> r = new ArrayList<>();
        for (Cluster<T> cluster : clusters)
            addToList(cluster, r);
        return r;
    }

    static <T> void addToList(Cluster<T> cluster, ArrayList<T> list) {
        list.ensureCapacity(list.size() + 1 + cluster.size());
        list.add(cluster.head);
        if (cluster.children != null)
            for (Cluster<T> child : cluster.children)
                addToList(child, list);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cluster cluster = (Cluster) o;

        assert children == null || children.size() > 0;
        if (!head.equals(cluster.head)) return false;
        if (children != null ? !children.equals(cluster.children) : cluster.children != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = head.hashCode();
        result = 31 * result + (children != null ? children.hashCode() : 0);
        return result;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        toString(sb, 0);
        return sb.toString();
    }

    private void toString(StringBuilder sb, int level) {
        sb.append(spaces(2 * level) + head + "\n");
        if (children == null)
            return;
        for (Cluster<T> child : children)
            child.toString(sb, level + 1);
    }

    private static String spaces(int n) {
        char[] charArray = new char[n];
        Arrays.fill(charArray, ' ');
        return new String(charArray);
    }
}
