package chap02;

import java.util.concurrent.atomic.AtomicLong;

import base.IBase;
import base.TestExecutor;
/**
 * @author xiuzhu
 * p16
 */
public class AtomicLongTest implements IBase{

	private AtomicLong count = new AtomicLong(0);	//init with 0
	private long count2 = 0;
	
	public void threadSafeAction(){
		count.incrementAndGet();
	}
	
	public void unThreadSafeAction(){
		count2++;
	}
	
	public void displayResult(){
		System.out.println(count + ", " + count2); 
	}
	
	public static void main(String[] args) {
		TestExecutor exe = new TestExecutor(new AtomicLongTest(), 1000);
		exe.execute();	//should be (1000, <1000)
	}

}
