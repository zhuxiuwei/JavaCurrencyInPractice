package chap05;
/**
 * Runnable接口是执行工作的单独任务，但是它不返回任何值。如果希望任务在完成时返回一个值，那么可以实现Callable接口而不是Runnable接口。
 * JDK 5引入的Callable是具有参数类型的泛型，类型参数表示的是它的call()方法(而不是run())返回值的类型，而且必须使用ExecurorServie的submit()方法(而不是execute()方法)调用它。
 * submit()方法会产生Future对象，它用Callable返回结果的特定类型进行了参数化。用isDone()方法查询Future是否完成。当任务完成，它具有一个返回结果，可用get()方法来获取结果。
 */

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author eminemheaton 2010-10-13
 */
class myCallableTask implements Callable<String> {
	@Override
	public String call() {
		try {
			TimeUnit.MILLISECONDS.sleep(10);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			System.out.println("我的执行被打断了！is interrupted: " + Thread.interrupted());
		}
		System.out.println("我执行完了!");
		return "这是返回结果！";
	}
}

public class CallableTest {
	public static void main(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool();
		Future<String> future = exec.submit(new myCallableTask());
		while (!future.isDone()) {
			System.out.println("task还没有执行完成....");
			//future.cancel(true);		//！！！可以把这个注释去电看看效果！！！ true和false效果也不一样。
		}
		try {
			System.out.println("isCancelled: " + future.isCancelled());
			System.out.println("isDone: " + future.isDone());
			String res = future.get();	//如果future被cancel，调用这个的话，会抛异常： java.util.concurrent.CancellationException
			System.out.println(res);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		exec.shutdown();
	}
}

// 输出：
//task还没有执行完成....
//task还没有执行完成....
//task还没有执行完成....
//task还没有执行完成....
//task还没有执行完成....
//task还没有执行完成....
//我执行完了!
//task还没有执行完成....
//isCancelled: false
//isDone: true
//这是返回结果！