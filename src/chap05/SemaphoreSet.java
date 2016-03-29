package chap05;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Semaphore;

/**
 * Test Semaphore -- A limited size set.
 * @author xiuzhu 160329
 *
 */
public class SemaphoreSet<T> {
	
	private Set<T> set = Collections.synchronizedSet(new HashSet<T>());
	private Semaphore sem;
	
	public SemaphoreSet(int limitedSize){
		sem = new Semaphore(limitedSize);
	}
	
	public boolean add(T obj) throws InterruptedException{
		boolean wasAdded = false;
		try{
			sem.acquire();
			wasAdded = set.add(obj);
		}finally{
			if(!wasAdded)
				sem.release();
		}
		return wasAdded;
	}
	
	public boolean remove(T obj){
		boolean wasRemoved = false;
		try{
			wasRemoved = set.remove(obj);
		}finally{
			if(wasRemoved)
				sem.release();
		}
		return wasRemoved;
	}
	
	public static void main(String[] args) {
		SemaphoreSet<String> test = new SemaphoreSet<String>(5);
		try {
			test.add("a");
			test.add("b");
			test.add("c");
			test.add("d");
			test.add("e");
			test.add("f");
			test.add("g");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

}
