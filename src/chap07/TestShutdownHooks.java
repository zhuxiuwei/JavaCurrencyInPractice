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
				System.out.println("AwsClient Interrupt!");
				Thread.currentThread().interrupt();		//restore interruption state
			}
		}
	}

	public static void main(String[] args) {
		Thread t = new Thread(new TestShutdownHooks());
		t.start();
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//System.exit(0);	//!!!!This will shutdown JVM, then trigger ShutdownHook.!!!!!!!!!!!!
		t.interrupt();	////!!!!This can also shutdown JVM, as AutoFlush is a daemon thread. then trigger ShutdownHook!!!!!!!!!!!!
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
