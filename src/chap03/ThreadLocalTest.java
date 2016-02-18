package chap03;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * Test ThreadLocal usage
 * @author xiuzhu
 * 160218
 */
public class ThreadLocalTest implements Runnable{

	public static ThreadLocal<DBConnectionResource> resHolder 
		= new ThreadLocal<DBConnectionResource>(){
//		@Override
//		public DBConnectionResource initialValue(){	//初始值。不覆盖的话，基类返回的是null。
//			return new DBConnectionResource();
//		}
	};
	
	public static DBConnectionResource getConnection(){
		if(resHolder.get() == null)
			resHolder.set(new DBConnectionResource());		//加上这两句的话，就可以不用initialValue()设初值了。
		return resHolder.get();
	}
	
	public static void main(String[] args) {
		ExecutorService executorService = Executors.newCachedThreadPool();
		for (int i = 0; i < 10; i++) 
			executorService.submit(new ThreadLocalTest());
		executorService.shutdown();	//don't forget to call shutdown otherwise JVM will not quit.
		
		try { Thread.sleep(100); } catch (InterruptedException e) { }
	}

	@Override
	public void run() {
		//每个Thread 打印资源三次。同一个Thread这三次应该都是同一个资源。而不同Thread之间的资源应该又是不同的。
		System.out.println(ThreadLocalTest.getConnection());
		System.out.println(ThreadLocalTest.getConnection());
		System.out.println(ThreadLocalTest.getConnection());
	}
}

//fake resource
class DBConnectionResource{
	
}