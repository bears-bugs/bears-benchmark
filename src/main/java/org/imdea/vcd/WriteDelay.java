package org.imdea.vcd;

import java.util.concurrent.ConcurrentHashMap;
import org.imdea.vcd.pb.Proto.Init;
import org.imdea.vcd.queue.clock.Clock;
import org.imdea.vcd.queue.clock.Dot;
import org.imdea.vcd.queue.clock.ExceptionSet;
import org.imdea.vcd.queue.clock.MaxInt;

/**
 *
 * @author Vitor Enes
 *
 * - wait until non-delivered commands have non-committed deps from my site
 */
public class WriteDelay {

    private static final boolean ENABLED = false;

    private Integer site;
    private ExceptionSet committed;
    private ConcurrentHashMap<Dot, Long> dond; // deps (from my site) of non-delivered cmds

    private final Object monitor = new Object();

    public void init(Init init) {
        if (ENABLED) {
            this.site = init.getSite();
            Clock<ExceptionSet> committedClock = Clock.eclock(init.getCommittedMap());
            this.committed = (ExceptionSet) committedClock.get(this.site);
            this.dond = new ConcurrentHashMap<>();
            monitorNotify();
        }
    }

    public void commit(Dot dot, Clock<MaxInt> conf) {
        if (ENABLED) {
            if (dot.getId().equals(this.site)) {
                this.committed.add(dot.getSeq());
            }
            this.dond.put(dot, conf.get(this.site).current());
        }
    }

    public void deliver(Dot dot) {
        if (ENABLED) {
            this.dond.remove(dot);
            monitorNotify();
        }
    }

    public void waitDepsCommitted() throws InterruptedException {
        if (ENABLED) {
            // wait until it's initialized
            if (this.dond == null) {
                monitorWait();
                waitDepsCommitted();
            }
            // wait all deps are committed
            boolean allCommitted = true;
            for (Long dep : this.dond.values()) {
                allCommitted = allCommitted && this.committed.containsAll(dep);
            }
            if (!allCommitted) {
                monitorWait();
                waitDepsCommitted();
            }
        }
    }

    private void monitorNotify() {
        synchronized (this.monitor) {
            this.monitor.notify();
        }
    }

    private void monitorWait() throws InterruptedException {
        synchronized (this.monitor) {
            this.monitor.wait(1);
        }
    }
}
