package chap02;
/**
 * 看p18 2.3.2对Reentrancy的介绍，感觉一个线程获取了子类的锁，那么也获取了父类的锁，换句话说子类父类同一个锁。实验证实到底是不是这样。
 * 结论：上面的结论是对的。下面的方法执行结果是：
 * 		t1: son!
		t2: base!
		t2必须等t1执行完才有机会。
 * 即，t1的执行会block住t2，虽然t1是锁在子类，t2想获取的是同一个子类对象的父类，在t1释放子类的锁前t2是无法获得的。
 * 160123
 */
public class BaseAndThisBothSynchronized{
	
	public static void main(String[] args) {
		//让t1和t2都共享一个ThisisSon变量。
		ThisisSon son = new ThisisSon();
		
		Thread t1 = new Thread(new CallSon(son));
		t1.setName("t1");
		t1.start();
		
		try { Thread.sleep(10); } catch (InterruptedException e) { e.printStackTrace(); }
		
		Thread t2 = new Thread(new CallBase(son));
		t2.setName("t2");
		t2.start();
	}

}

//父类有一个synchronized方法
class ThisisBase{
	public synchronized void baseFunc(){
		System.out.println(Thread.currentThread().getName() + ": base!");
		try { Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace(); }
	}
}
//子类有一个synchronized方法
class ThisisSon extends ThisisBase{
	public synchronized void sonFunc(){
		System.out.println(Thread.currentThread().getName() + ": son!");
		try { Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace(); }
	}
}

//调用父类方法的Runnable类
class CallBase implements Runnable{
	private ThisisSon son = null;
	public CallBase(ThisisSon son){
		this.son = son;	
	}
	@Override
	public void run() {
		son.baseFunc();
	}
}
//调用子类方法的Runnable类
class CallSon implements Runnable{
	private ThisisSon son = null;
	public CallSon(ThisisSon son){
		this.son = son;	
	}
	@Override
	public void run() {
		son.sonFunc();
	}
}

