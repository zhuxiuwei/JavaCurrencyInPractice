package chap07;

import java.util.Timer;
import java.util.TimerTask;
/**
 * ShutdownHook demo。 P165
 * @author xiuzhu
 * 160426
 */
public class TestShutdownHooks implements Runnable{
	private AwsClient client = new AwsClient();
	
	public TestShutdownHooks(){
		//!!!!!!!!!!!add ShutdownHook!!!!!!!!!!!!!!!!!!!
		Runtime.getRuntime().addShutdownHook(new Thread(){
			public void run(){
				client.close();
			}
		});
	}
	
	@Override
	public void run() {
		while(!Thread.currentThread().isInterrupted()){		//responsive to interrupt
			client.send();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {	//responsive to interrupt
				System.out.println("TestShutdownHooks Interrupted!");
				Thread.currentThread().interrupt();		//restore interruption state
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		Thread t = new Thread(new TestShutdownHooks());
		t.start();
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//System.exit(0);	//!!!!This will shutdown JVM, then trigger ShutdownHook.!!!!!!!!!!!!
		
		System.out.println("\r\n******* Test isInterrupted again! *******");
		t.interrupt();	////!!!!This can also shutdown JVM, as AutoFlush is a daemon thread. then trigger ShutdownHook!!!!!!!!!!!!
		/**测试isInterrupted为啥总返回false。这个帖子一句话提醒了我："@rAy except your thread immediately dies. – Peter Lawrey Dec 19 '13 at 9:47" http://stackoverflow.com/questions/20677604/thread-isinterrupted-always-returns-false 
		        所以，在Thread isAlive的时候，isInterrupted能正确返回true。但是Thread dies后，看起来interrupt flag又被reset了。 */
		boolean isInterrupt = t.isInterrupted();
		boolean isAlive = t.isAlive();
		/** 因为在interrupt()后的第一时间保存好isAlive和isInterrupt，此时在极短的瞬间t还是alive的，所以返回true true */
		System.out.println("isInterrupt: " + isInterrupt + ", isAlive: " + isAlive);	//true true
		/** 貌似调用System.out.println会耗费一些时间，此时Thread已经die了，所以打印false false */
		Thread.sleep(5);
		System.out.println("isInterrupt: " + t.isInterrupted() + ", isAlive: " + t.isAlive());	//false false
	}
}

class AwsClient
{
	public AutoFlush flush;
	public AwsClient(){
		flush = new AutoFlush();
		new Timer("Flusher" + Thread.currentThread().getName() + System.currentTimeMillis(), true)	//'true' means AutoFlush runs as daemon thread.
			.schedule(flush, 1000, 1000);
	}
	public void send() {
		System.out.println("AwsClient send..");
	}
	public void close(){	//cancel auto flush
		flush.quit();
		flush = null;
	}
}

class AutoFlush extends TimerTask {
	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName()+" auto flush!");
	}
	
	public void quit(){
		System.out.println("@@@flusher cancel now!");
		cancel();
		System.out.println("@@@flusher canceled!");
	}
}
