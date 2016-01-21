package base;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * Use this class to facility test.
 * @author xiuzhu
 */
public class TestExecutor{
	
	private IBase base = null;
	private int threadCount = 0;

	public TestExecutor(IBase base, int threadCount){
		this.base = base;
		this.threadCount = threadCount;
	}
	
	public void execute(){
		//test ThreadSafeActions of IBase
		ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
		for (int i = 0; i < threadCount; i++) 
			executorService.submit(new ThreadSafeActionsTest());
		executorService.shutdown();	//don't forget to call shutdown otherwise JVM will not quit.
		
		try { Thread.sleep(100); } catch (InterruptedException e) { }
		
		//test UnThreadSafeActions of IBase
		ExecutorService executorService2 = Executors.newFixedThreadPool(threadCount);
		for (int i = 0; i < threadCount; i++) 
			executorService2.submit(new UnThreadSafeActionsTest());
		executorService2.shutdown();
		
		//display result
		base.displayResult();
	}

	class ThreadSafeActionsTest implements Runnable{
		@Override
		public void run() {
			base.threadSafeAction();
		}
	}
	
	class UnThreadSafeActionsTest implements Runnable{
		@Override
		public void run() {
			base.unThreadSafeAction();
		}
	}
}
