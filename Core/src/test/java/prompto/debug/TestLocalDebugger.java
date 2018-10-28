package prompto.debug;

import static org.junit.Assert.assertEquals;

import java.lang.Thread.State;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.error.PromptoError;
import prompto.runtime.Context;
import prompto.runtime.Interpreter;
import prompto.runtime.utils.Out;

public class TestLocalDebugger extends TestDebuggerBase {

	protected Thread thread; // in debug mode

	@Before
	public void before() {
		Out.init();
	}
	
	@After
	public void after() {
		Out.restore();
	}
	
	@Override
	protected String readOut() {
		return Out.read();
	}
	
	@Override
	protected void start() {
		thread.start();
	}
	
	@Override
	protected void join() throws InterruptedException {
		thread.join();
	}

	@Override
	protected void waitBlockedOrKilled() throws InterruptedException {
		State state = thread.getState();
		while(state!=State.WAITING && state!=State.TERMINATED) {
			Thread.sleep(10);
			state = thread.getState();
		}
	}

	@Override
	protected void debugResource(String resourceName) throws Exception {
		loadResource(resourceName);
		LocalDebugger debugger = new LocalDebugger();
		final Context local = context.newLocalContext();
		local.setDebugger(debugger);
		this.debugger = debugger;
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Interpreter.interpretMainNoArgs(local);
				} catch (PromptoError e) {
					// TODO Auto-generated catch block
				}
			}
		});
		
	}
	
	
	@Test
	public void testStackNoDebug() throws Exception {
		interpretResource("debug/stack.pec", false);
		assertEquals("test123-ok", readOut());
	}



}
