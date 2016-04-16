package chap06;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 模仿p128，练习Callable和Future(ExecutroService submit获得)增加并发度。
 * @author xiuzhu
 * 160414
 */
public class FutureRender {

	ExecutorService exs = Executors.newCachedThreadPool();
	
	public void renderPage(){
		
		//download images
		Callable<List<DownloadedImages>> downloadTask = new Callable<List<DownloadedImages>>(){
			List<DownloadedImages> res = new ArrayList<DownloadedImages>();
			public List<DownloadedImages> call(){
				for (int i = 1; i <= 10; i++) {
					DownloadedImages image = new DownloadedImages(i);
					System.out.println(Thread.currentThread().getName() + " download image " + i);
					res.add(image);
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				return res;
			}
		};
		
		Future<List<DownloadedImages>> future = exs.submit(downloadTask);	//WILL NOT BLOCK main thread!
		renderText();
		
		try {
			System.out.println("Wating for images downloaded...");
			List<DownloadedImages> images = future.get();
			System.out.print("All images downloaded!:" );
			for (DownloadedImages image: images) {
				System.out.print(Thread.currentThread().getName() + " " + image + " ");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		System.out.println("main done!");
	}
	
	//render text
	private void renderText(){
		System.out.println(Thread.currentThread().getName() + " Start render Text...");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getName() + " Finished render Text!");
	}
	
	public static void main(String[] args) {
		FutureRender fr = new FutureRender();
		fr.renderPage();
	}

}

//Downloaded Image class
class DownloadedImages{
	int id;
	public DownloadedImages(int id){this.id = id;}
	public String toString(){
		return "Image-" + id;
	}
}
