package chap03;
/**
 * Listing 3.1
 * @author zxw
 */
public class NoVisibility {
	private static boolean ready;
	private static int number; 
	private static class ReadThread extends Thread{
		public void run(){
			while(!ready)
				Thread.yield();
			System.out.println(number);
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
			    System.out.println(System.getProperty("java.runtime.version"));
		new ReadThread().start();
//		Thread.sleep(1000);
		number = 42;
		ready = true;
	}

}
