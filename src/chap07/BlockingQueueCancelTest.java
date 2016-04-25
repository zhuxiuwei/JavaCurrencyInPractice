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
			System.out.println("Thread.currentThread().isInterrupted() 1: " + Thread.currentThread().isInterrupted()); //返回false，原因：http://zhidao.baidu.com/link?url=flhV6VuxUNY6ulkiYFG-RVdqvSncai07sASV8cYETcXBgWlbWQ-rLwkJSJN-zvshTkbLq3GNx6iRHF723Zm3D9hbkfpGA8altQeRJNLsE9G
			
			//160425 可以通过以下方式，给interrupt状态置位。 （即书上的Restore the interrupt. P93）
			Thread.currentThread().interrupt();
			System.out.println("Thread.currentThread().isInterrupted() 2: " + Thread.currentThread().isInterrupted());
		}
	}
	
	public void cancel(Thread t) {
		t.interrupt();
	}
	
	public static void main(String[] args) throws InterruptedException {
		BlockingQueueCancelTest bqct = new BlockingQueueCancelTest();
		Thread t = new Thread(bqct);
		t.start();
		
		BlockingQueueCancelTestStateChecker checker = new BlockingQueueCancelTestStateChecker(t);
		Thread t2 = new Thread(checker);
		t2.start();
		
		Thread.sleep(100);
		bqct.cancel(t);
		//System.out.println(bqct.queue);	//让System.out.println("interrupt!"); 能work，需要注释掉这个
		
		while(t.isAlive()){
			if(t.isInterrupted())
				System.out.println("interrupt!");		//！！！！不知道为什么，不work！！！！！ -- (貌似把前面几行的打印语句去掉，有时候能work了)
			try {
				Thread.sleep(11);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("main done");
	}
}

//Use this class track BlockingQueueCancelTest thread state
class BlockingQueueCancelTestStateChecker implements Runnable{

	private Thread BlockingQueueCancelTestThread;
	public BlockingQueueCancelTestStateChecker(Thread BlockingQueueCancelTestThread){
		this.BlockingQueueCancelTestThread = BlockingQueueCancelTestThread;
	}
	
	@Override
	public void run() {
		while(BlockingQueueCancelTestThread.isAlive()){
			//System.out.print(".");
			if(BlockingQueueCancelTestThread.isInterrupted()){
				System.out.println("BlockingQueueCancelTestStateChecker: Interrupt detected!!!!!!!!!!!");
				break;
			}
			//System.out.println("BlockingQueueCancelTestThread isAlive: " + BlockingQueueCancelTestThread.isAlive() + ", isInterrupted:" + BlockingQueueCancelTestThread.isInterrupted());
		}
		
//		while(!BlockingQueueCancelTestThread.isInterrupted()){
//		}
//		System.out.println("BlockingQueueCancelTestStateChecker: Interrupt detected!!!!!!!!!!!");
	}
	
}
