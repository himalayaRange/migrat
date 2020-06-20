package com.ymy.xxb.migrat.common.test;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MutilTheradTest {
	/*
	 * 基于AQS共享模式的使用实现
	 */
	@Test
	public void countDownLatchTest() throws InterruptedException {
		final CountDownLatch countDownLatch = new CountDownLatch(4); // 容纳四个线程的线程池
		Runnable T1 = new Runnable() {

			@Override
			public void run() {
				try {
					TimeUnit.MILLISECONDS.sleep(3000);
					log.info("统计T1");
					countDownLatch.countDown();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};

		Runnable T2 = new Runnable() {

			@Override
			public void run() {
				try {
					TimeUnit.MILLISECONDS.sleep(3000);
					log.info("统计T2");
					countDownLatch.countDown();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};

		Runnable T3 = new Runnable() {

			@Override
			public void run() {
				try {
					TimeUnit.MILLISECONDS.sleep(3000);
					log.info("统计T3");
					countDownLatch.countDown();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};

		Runnable T4 = new Runnable() {

			@Override
			public void run() {
				try {
					TimeUnit.MILLISECONDS.sleep(3000);
					log.info("统计T4");
					countDownLatch.countDown();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};

		ExecutorService service = Executors.newFixedThreadPool(4);
		service.submit(T1);
		service.submit(T2);
		service.submit(T3);
		service.submit(T4);

		countDownLatch.await();
		service.shutdown();
	}

	/*
	 * 基于cyclicBarrier栅栏,底层基于ReentrantLock 和 Condition 的组合使用
	 * <pre>
	 * 	 CyclicBarrier内部维护了两个变量：计数器 count 和 拦截的线程数 parties
	 * 
	 *   每次await()将count减1，知道减为0，唤醒所有线程
	 * 
	 *   Generation表示栅栏的当前代，完成任务时通过barrierCommand来执行自己的任务
	 *   
	 *   线程通过Condition.signalAll()
	 * </pre>
	 */
	@Test
	public void cyclicBarrierTest() throws InterruptedException {
		final int threadNum = 4;
		// 主线程，需要在栅栏子线程中await，等待所有线程执行完毕
		final CyclicBarrier barrier = new CyclicBarrier(threadNum, new Runnable() {

			@Override
			public void run() {
				log.info("统计T1, T2, T3, T4");
			}

		});

		Runnable T1 = new Runnable() {

			@Override
			public void run() {
				try {
					TimeUnit.MILLISECONDS.sleep(3000);
					log.info("统计T1");
					barrier.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (BrokenBarrierException e) {
					e.printStackTrace();
				}
			}
		};

		Runnable T2 = new Runnable() {

			@Override
			public void run() {
				try {
					TimeUnit.MILLISECONDS.sleep(3000);
					log.info("统计T2");
					barrier.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (BrokenBarrierException e) {
					e.printStackTrace();
				}
			}
		};

		Runnable T3 = new Runnable() {

			@Override
			public void run() {
				try {
					TimeUnit.MILLISECONDS.sleep(3000);
					log.info("统计T3");
					barrier.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (BrokenBarrierException e) {
					e.printStackTrace();
				}
			}
		};

		Runnable T4 = new Runnable() {

			@Override
			public void run() {
				try {
					TimeUnit.MILLISECONDS.sleep(3000);
					log.info("统计T4");
					barrier.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (BrokenBarrierException e) {
					e.printStackTrace();
				}
			}
		};

		ExecutorService service = Executors.newFixedThreadPool(threadNum);
		service.submit(T1);
		service.submit(T2);
		service.submit(T3);
		service.submit(T4);
		service.shutdown();
		// 程序等待
		TimeUnit.MILLISECONDS.sleep(100000);
	}

	/*
	 * 概念： t.join是将当前线程加入到主线程中去
	 * 底层基于的wait()实现的，如果当前线程并不是启动状态，则会直接跳过
	 * 注意： t.join只是使主线程进去线程池等待状态，不会暂停其他在运行中的线程，等待t线程完成才执行后继续执行主线程
	 */
	@Test
	public void joinTest() throws InterruptedException {
		Thread T1 = new Thread(new T("T1"));
		Thread T2 = new Thread(new T("T2"));
		Thread T3 = new Thread(new T("T3"));
		Thread T4 = new Thread(new T("T4"));

		T1.start();
		T2.start();
		T3.start();
		T4.start();

		T1.join();
		T2.join();
		T3.join();
		T4.join();

		System.out.println("统计T1，T2，T3，T4");
		TimeUnit.MILLISECONDS.sleep(100000);
	}

	class T extends Thread {
		public String tips;

		public T(String tips) {
			this.tips = tips;
		}

		public void run() {
			try {
				Thread.sleep(3000);
				System.out.println("统计" + tips);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
