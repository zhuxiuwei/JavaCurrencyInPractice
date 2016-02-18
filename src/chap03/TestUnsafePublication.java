package chap03;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * p50 unsafe publication. 160218
 * @author xiuzhu
 * 不幸的是，神奇的事情依旧不能发生。。。
 */
public class TestUnsafePublication implements Runnable{

	private CallHolder ch;
	public TestUnsafePublication(CallHolder ch){
		this.ch = ch;
	}
	
	public static void main(String[] args) {
		CallHolder ch = new CallHolder();
		ch.initialize();
		ExecutorService es = Executors.newCachedThreadPool();
		for (int i = 0; i < 50; i++) {
			es.submit(new TestUnsafePublication(ch));
		}
		es.shutdown();
	}

	@Override
	public void run() {
		ch.holder.assertVerify();
	}

}

class CallHolder{
	public Holder holder;
	public void initialize(){
		holder = new Holder(42);
	}
}

class Holder{
	private int n;
	public Holder(int n){this.n = n;}
	public void assertVerify(){
		if(n != n)
			throw new AssertionError("BAD!");		//神奇的事情发生
		
	}
}
