package org.imdea.vcd.queue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import org.imdea.vcd.queue.box.QueueBox;

/**
 *
 * @author Vitor Enes
 * @param <E>
 */
public class RandomQueue<E extends QueueBox> implements Queue<E> {

    private static final int BUCKETS = 10;
    private final Map<Integer, List<E>> BUCKET_MAP = new HashMap<>();

    private int size;

    public RandomQueue() {
        size = 0;
        for (int i = 0; i < BUCKETS; i++) {
            resetBucket(i);
        }
    }

    @Override
    public boolean isEmpty() {
        throw new UnsupportedOperationException("Method not supported.");
    }

    @Override
    public void add(QueueAddArgs<E> args) {
        E e = args.getBox();

        // add box to random bucket
        int bucket = randomBucket();
        BUCKET_MAP.get(bucket).add(e);

        // update size        
        size++;
    }

    @Override
    public List<E> tryDeliver() {
        // get boxes from random bucket
        int bucket = randomBucket();
        List<E> boxes = resetBucket(bucket);

        if (size > 0 && boxes.isEmpty()) {
            // if there's something to deliver,
            // and we picked and empty bucket,
            // pick a new one
            return tryDeliver();
        }

        // update size
        size -= boxes.size();

        // return boxes
        return boxes;
    }

    @Override
    public List<E> toList() {
        throw new UnsupportedOperationException("Method not supported.");
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int elements() {
        return size();
    }

    private List<E> resetBucket(int bucket) {
        // retrieve current value
        List<E> current = BUCKET_MAP.get(bucket);

        // create bottom value and update entry
        List<E> bottom = new ArrayList<>();
        BUCKET_MAP.put(bucket, bottom);

        // return old entry
        return current;
    }

    private int randomBucket() {
        return ThreadLocalRandom.current().nextInt(BUCKETS);
    }
}
