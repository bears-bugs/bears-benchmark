package org.imdea.vcd;

import org.imdea.vcd.pb.Proto;
import org.imdea.vcd.pb.Proto.MessageSet;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

public class DummySocket extends Socket {

    BlockingQueue<Proto.MessageSet> queue;
    Executor executor;

    private DummySocket(DataRW rw) {
        super(rw);
        queue = new LinkedBlockingDeque<>();
        executor = Executors.newCachedThreadPool();
    }

    public static Socket create(Config config) throws IOException {
        return new DummySocket(null);
    }

    public void send(final Proto.MessageSet messageSet) throws IOException {

        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                MessageSet.Builder builder = MessageSet.newBuilder();
                for (Proto.Message msg : messageSet.getMessagesList()) {
                    builder.addMessages(msg);
                    builder.setStatus(MessageSet.Status.DURABLE);
                }
                try {
                    queue.put(builder.build());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                MessageSet.Builder builder = MessageSet.newBuilder();
                for (Proto.Message msg : messageSet.getMessagesList()) {
                    builder.addMessages(msg);
                    builder.setStatus(MessageSet.Status.DELIVERED);
                }
                try {
                    queue.put(builder.build());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Proto.MessageSet receive() throws IOException {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

}
