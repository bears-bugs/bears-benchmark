package org.imdea.vcd.queue;

import org.imdea.vcd.queue.box.QueueBox;
import org.imdea.vcd.queue.clock.Clock;
import org.imdea.vcd.queue.clock.ExceptionSet;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Some methods are copied from java.util.LinkedList.
 *
 * @author Vitor Enes
 * @param <E>
 */
public class DepQueue<E extends QueueBox> implements Queue<E> {

    private Node<E> first;
    private Node<E> last;
    private int size;
    private int elements;

    private Clock<ExceptionSet> delivered;

    public DepQueue(Integer nodeNumber) {
        this.delivered = Clock.eclock(nodeNumber);
        this.size = 0;
        this.elements = 0;
    }

    public DepQueue(Clock<ExceptionSet> delivered) {
        this.delivered = delivered;
        this.size = 0;
        this.elements = 0;
    }

    @Override
    public boolean isEmpty() {
        return first == null;
    }

    @Override
    public void add(QueueAddArgs<E> args) {
        E e = args.getBox();

//        System.out.println("Adding " + e);
        Node<E> x = findDependsOnE(e);
        Node<E> y = findEDependsOn(e);
//        if (x != null) {
//            System.out.println("X: " + x.item);
//        }
//        if (y != null) {
//            System.out.println("Y: " + y.item);
//        }

        if (x == null && y == null) {
//            System.out.println("a)");
            size++;
            linkFirst(e);
        } else if (x == null) {
//            System.out.println("b)");
            size++;
            linkAfter(e, y);
        } else if (y == null) {
//            System.out.println("c)");
            size++;
            linkBefore(e, x);
        } else if (y.next == x) {
//            System.out.println("e)");
            // insert in between both
            size++;
            linkBetween(e, y, x);
        } else {
//            System.out.println("d)");
            // found cycle: merge all
            merge(e, x, y);
        }

        elements++;
    }

    @Override
    public List<E> tryDeliver() {
        List<E> result = new ArrayList<>();
        boolean flag = true;

        while (first != null && flag) {
            Node<E> candidate = first;

            // copy 'delivered' clock
            Clock<ExceptionSet> nextDelivered = (Clock<ExceptionSet>) delivered.clone();

            // NOTE function 'canDeliver' mutates 'nextDelivered' clock
            flag = candidate.item.canDeliver(nextDelivered);

            if (flag) {
                // update delivered clock
                this.delivered = nextDelivered;

                // add to results
                result.add(candidate.item);

                // update first
                first = first.next;
                if (first == null) {
                    last = null;
                } else {
                    first.prev = null;
                }

                size--;
                elements -= candidate.item.size();

            }
        }

//        System.out.println("final queue:");
//        System.out.println(this);
        return result;
    }

    @Override
    public List<E> toList() {
        List<E> result = new ArrayList<>();
        Node<E> it = first;

        while (it != null) {
            result.add(it.item);
            it = it.next;
        }

        return result;
    }

    /**
     * Find element that depends on e.
     */
    private Node<E> findDependsOnE(E e) {
        Node<E> it = first;

        while (it != null) {
            if (e.before(it.item)) {
                return it;
            }
            it = it.next;
        }

        return null;
    }

    /**
     * Find elements that e depends on.
     */
    private Node<E> findEDependsOn(E e) {
        Node<E> it = last;
        while (it != null) {
            if (it.item.before(e)) {
                return it;
            }
            it = it.prev;
        }

        return null;
    }

    /**
     * Links e as first element.
     */
    private void linkFirst(E e) {
        final Node<E> f = first;
        final Node<E> newNode = new Node<>(null, e, f);
        first = newNode;
        if (f == null) {
            last = newNode;
        } else {
            f.prev = newNode;
        }
    }

    /**
     * Inserts element e before non-null Node succ.
     */
    private void linkBefore(E e, Node<E> succ) {
        // assert n != null;
        final Node<E> pred = succ.prev;
        final Node<E> newNode = new Node<>(pred, e, succ);
        succ.prev = newNode;
        if (pred == null) {
            first = newNode;
        } else {
            pred.next = newNode;
        }
    }

    /**
     * Inserts element e after non-null Node pred.
     */
    private void linkAfter(E e, Node<E> pred) {
        // assert n != null;
        final Node<E> succ = pred.next;
        final Node<E> newNode = new Node<>(pred, e, succ);
        pred.next = newNode;
        if (succ == null) {
            last = newNode;
        } else {
            succ.prev = newNode;
        }
    }

    /**
     * Inserts element e between Node pred and Node succ.
     */
    private void linkBetween(E e, Node<E> pred, Node<E> succ) {
        final Node<E> newNode = new Node<>(pred, e, succ);
        if (pred == null) {
            first = newNode;
        } else {
            pred.next = newNode;
        }
        if (succ == null) {
            last = newNode;
        } else {
            succ.prev = newNode;
        }
    }

    /**
     * Merge e with all from Node a to Node b.
     */
    private void merge(E e, Node<E> a, Node<E> b) {
        Node<E> pred = a.prev;
        Node<E> succ = b.next;

        // merge a with e
        e.merge(a.item);

        while (a.next != b.next) {
            // while not pointing to the same element, merge
            a = a.next;
            e.merge(a.item);
            size--;
        }

        linkBetween(e, pred, succ);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node<E> it = first;
        int i = 0;
        while (it != null) {
            if (i > 0) {
                sb.append("\n");
            }
            sb.append(i)
                    .append("> ")
                    .append(it.item.toString());
            it = it.next;
            i++;
        }
        if (i == 0) {
            sb.append("[empty]");
        }
        return sb.toString();
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public int elements() {
        return this.elements;
    }

    private static class Node<E> {

        Node<E> prev;
        E item;
        Node<E> next;

        Node(Node<E> prev, E element, Node<E> next) {
            this.prev = prev;
            this.item = element;
            this.next = next;
        }
    }
}
