package chap05;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * Latches test
 * @author xiuzhu
 * 160324
 */
public class CountDownLatchTest {

	public long calRunTimeOfNThreads(int threadCount){
		
		//master threads holds the CountDownLatch.
		final CountDownLatch startGate = new CountDownLatch(1);
		final CountDownLatch exitGate = new CountDownLatch(threadCount);
		
		//can't write like below! will block master thread(main thread)!!!
//		ExecutorService es = Executors.newFixedThreadPool(threadCount);
//		for (int i = 0; i < threadCount; i++) {
//			try {
//				startGate.await();
//				es.submit(new RunTask());
//				exitGate.countDown();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//		es.shutdown();
		
		for (int i = 0; i < threadCount; i++) {
			
			//can't write like below! will block master thread(main thread)!!!
//			RunTask rt = new RunTask();
//			try {
//				startGate.await();
//				rt.run();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			exitGate.countDown();
			
			//below is OK
			Thread t = new Thread() {
				public void run() {
					try {
						startGate.await();	//wait before master thread says "GO!"
						new RunTask().run();
						exitGate.countDown();	//Tell master thread this runnable is done.
					} catch (InterruptedException ignored) { }
				}
			};
			t.start();
		}
		
		System.out.println("Master: 'On your mark!'");
		try {
			Thread.sleep(2000);	//All runnable has to wait for master thread's 'go' command. The 'go' command is an event.
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		System.out.println("Master: 'GO!'");
		startGate.countDown();
		long startTime = System.currentTimeMillis();
		try {
			exitGate.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long endTime = System.currentTimeMillis();
		return endTime - startTime;
	}
	
	public static void main(String[] args) {
		CountDownLatchTest latch = new CountDownLatchTest();
		System.out.println("Master: 'All tasks took: " + latch.calRunTimeOfNThreads(5) + " ms to finish'");
	}

}

class RunTask implements Runnable{

	@Override
	public void run() {
		int s = new Random().nextInt(5);
		System.out.println("Thread " + Thread.currentThread().getName() + " will work for seconds: " + s);
		try {
			Thread.sleep(s * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Thread " + Thread.currentThread().getName() + " job done!");
	}
	
}
