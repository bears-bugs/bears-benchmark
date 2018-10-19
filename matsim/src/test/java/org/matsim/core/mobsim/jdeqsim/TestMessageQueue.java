package org.matsim.core.mobsim.jdeqsim;

import org.matsim.core.mobsim.jdeqsim.util.DummyMessage;
import org.matsim.testcases.MatsimTestCase;


public class TestMessageQueue extends MatsimTestCase {
	public void testPutMessage1(){
		MessageQueue mq=new MessageQueue();
		Message m1=new DummyMessage();
		m1.setMessageArrivalTime(1);
		
		Message m2=new DummyMessage();
		m2.setMessageArrivalTime(2);
		
		mq.putMessage(m1);
		mq.putMessage(m2);
		assertEquals(2, mq.getQueueSize());
		assertEquals(true, mq.getNextMessage()==m1);
	}
	
	public void testPutMessage2(){
		MessageQueue mq=new MessageQueue();
		Message m1=new DummyMessage();
		m1.setMessageArrivalTime(2);
		
		Message m2=new DummyMessage();
		m2.setMessageArrivalTime(1);
		
		mq.putMessage(m1);
		mq.putMessage(m2);
		assertEquals(2, mq.getQueueSize());
		assertEquals(true, mq.getNextMessage()==m2);
	}
	
	public void testPutMessage3(){
		MessageQueue mq=new MessageQueue();
		Message m1=new DummyMessage();
		m1.setMessageArrivalTime(2);
		
		Message m2=new DummyMessage();
		m2.setMessageArrivalTime(1);
		
		Message m3=new DummyMessage();
		m3.setMessageArrivalTime(1);
		
		mq.putMessage(m1);
		mq.putMessage(m2);
		mq.putMessage(m3);
		assertEquals(3, mq.getQueueSize());
		assertEquals(true, mq.getNextMessage().getMessageArrivalTime()==1);
	}
	
	public void testRemoveMessage1(){
		MessageQueue mq=new MessageQueue();
		Message m1=new DummyMessage();
		m1.setMessageArrivalTime(1);
		
		Message m2=new DummyMessage();
		m2.setMessageArrivalTime(2);
		
		mq.putMessage(m1);
		mq.putMessage(m2);
		mq.removeMessage(m1);
		assertEquals(1, mq.getQueueSize());
		assertEquals(true, mq.getNextMessage()==m2);
		assertEquals(0, mq.getQueueSize());
	}
	
	public void testRemoveMessage2(){
		MessageQueue mq=new MessageQueue();
		Message m1=new DummyMessage();
		m1.setMessageArrivalTime(1);
		
		Message m2=new DummyMessage();
		m2.setMessageArrivalTime(2);
		
		mq.putMessage(m1);
		mq.putMessage(m2);
		mq.removeMessage(m2);
		assertEquals(1, mq.getQueueSize());
		assertEquals(true, mq.getNextMessage()==m1);
		assertEquals(0, mq.getQueueSize());
	}
	
	public void testRemoveMessage3(){
		MessageQueue mq=new MessageQueue();
		Message m1=new DummyMessage();
		m1.setMessageArrivalTime(1);
		
		Message m2=new DummyMessage();
		m2.setMessageArrivalTime(1);
		
		mq.putMessage(m1);
		mq.putMessage(m2);
		mq.removeMessage(m1);
		assertEquals(1, mq.getQueueSize());
		assertEquals(false, mq.isEmpty());
		assertEquals(true, mq.getNextMessage()==m2);
		assertEquals(0, mq.getQueueSize());
		assertEquals(true, mq.isEmpty());
	}
	
	// a higher priority message will be at front of queue, if there are 
	// several messages with same time
	public void testMessagePriority(){
		MessageQueue mq=new MessageQueue();
		Message m1=new DummyMessage();
		m1.setMessageArrivalTime(1);
		m1.setPriority(10);
		
		Message m2=new DummyMessage();
		m2.setMessageArrivalTime(1);
		m2.setPriority(5);
		
		Message m3=new DummyMessage();
		m3.setMessageArrivalTime(1);
		m3.setPriority(20);
		
		mq.putMessage(m1);
		mq.putMessage(m2);
		mq.putMessage(m3);

		assertEquals(true, mq.getNextMessage()==m3);
		assertEquals(true, mq.getNextMessage()==m1);
		assertEquals(true, mq.getNextMessage()==m2);
		assertEquals(0, mq.getQueueSize());
		assertEquals(true, mq.isEmpty());
	}
	
	
	
}
