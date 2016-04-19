package chap07;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 测试interruption cancellation。p141
 * @author xiuzhu
 * 160419
 */
public class BlockingQueueCancelTest implements Runnable{

	private final BlockingQueue<String> queue = new LinkedBlockingQueue<String>(3);
	
	@Override
	public void run() {
		try {
			while(!Thread.currentThread().isInterrupted())
				queue.put("a");
		} catch (InterruptedException e) {
			System.out.println("put method interrupted!");
			System.out.println(Thread.currentThread().isInterrupted()); //返回false，原因：http://zhidao.baidu.com/link?url=flhV6VuxUNY6ulkiYFG-RVdqvSncai07sASV8cYETcXBgWlbWQ-rLwkJSJN-zvshTkbLq3GNx6iRHF723Zm3D9hbkfpGA8altQeRJNLsE9G
		}
	}
	
	public void cancel(Thread t) {
		t.interrupt();
	}
	
	public static void main(String[] args) throws InterruptedException {
		BlockingQueueCancelTest bqct = new BlockingQueueCancelTest();
		Thread t = new Thread(bqct);
		t.start();
		Thread.sleep(1000);
		System.out.println(bqct.queue);
		
		bqct.cancel(t);
	}
}
