package chap06;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 带超时的Future task。模仿p132 listing 6.16
 * ！！！需要注意一点。貌似如果future task被cancel了，catch语句里的return是没有用的。
 * @author xiuzhu
 * 160418
 */
public class TimedFutureTest {

	private ExecutorService es = Executors.newCachedThreadPool();
	
	public void displayAds(){
		
		Future<Ad> f = es.submit(new Callable<Ad>(){
			public Ad call(){
				long sleepTime = new Random().nextInt(1000);
				try {
					System.out.println("Will take " + sleepTime + " ms to select Ads.");
					Thread.sleep(sleepTime);
					return new Ad("SELECTED AD");
				} catch (InterruptedException e) {
					System.out.println("Interrupted!!!");
					return new Ad("DEFAULT AD");	//!!! Note!!! seems useless. If interrupted, f.get can get nothing.
				}
			}
		});
		
		Ad ad = null;
		try {
			ad = f.get(500, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			System.out.println("300ms Time out. Will cancel task and dispaly defalt Ads.");
			System.out.println(f.isCancelled());
			System.out.println(f.isDone());
			//f.cancel(true);
			ad = new Ad("DEFAULT AD");	//The code in !!!Note!!! does not work so got to add this line.
		}finally{
			f.cancel(false);	//160422：把cancel放到finally里。模仿p147 7.10.对于正常完成的task，这是harmless的。
		}
		System.out.println(f.isCancelled());	//160422：如果Task正常complete，这里会显示false！
		System.out.println(f.isDone());
		System.out.println(ad.name);
		
		//shut down Executor service to quit JVM
		es.shutdown();
	}
		
	public static void main(String[] args) {
		new TimedFutureTest().displayAds();
	}

}

//Advertisement class.
class Ad{
	public String name;
	public Ad(String name){
		this.name = name;
	}
}
