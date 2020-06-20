package com.ymy.xxb.migrat.common.test;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.junit.Test;

/**
 * Condition  await()   singal()    singalAll() 
 * 
 * Objects    wait()    notifty()   notifyAll()   
 * 
 * @author wangyi
 *
 */
public class ConditionTest {
	
	@Test
	public void test() throws InterruptedException {
		ConditionTest conditionTest = new ConditionTest();
		Producer producer = conditionTest.new Producer();
		Consumer consumer = conditionTest.new Consumer();
		
		producer.start();
		consumer.start();
		
		TimeUnit.SECONDS.sleep(1000);
	}
	
	private int maxQueueSize = 10;
	private PriorityBlockingQueue<Integer> queue = new PriorityBlockingQueue<Integer>(maxQueueSize);
	private Lock lock = new ReentrantLock();
	private Condition notFull = lock.newCondition(); // 未满的线程拦截器
	private Condition notEmpty = lock.newCondition(); // 非空的线程拦截器
	
	class Consumer extends Thread {
		@Override
		public void run () {
			while (true) {
				lock.lock();
				try {
					while (queue.size() == 0) {
						try {
							System.out.println("消费者数据为空，等待队列...");
							notEmpty.await();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					queue.poll(); // 移走队首
					notFull.signalAll();
					System.out.println("从队列取走一个元素，队列剩余"+queue.size()+"个元素");
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					lock.unlock();
				}
			}
		}
	}
	
	class Producer extends Thread {
		@Override
		public void run () {
			while (true) {
				lock.lock();
				try {
					while (queue.size() == maxQueueSize) {
						try {
							System.out.println("生产者队列已满，等待剩余空间");
							notFull.await();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					queue.offer(1);
					notEmpty.signalAll();
					System.out.println("向队列取中插入一个元素，队列剩余空间："+(maxQueueSize - queue.size()));
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					lock.unlock();
				}
			}
		}
	}
}
