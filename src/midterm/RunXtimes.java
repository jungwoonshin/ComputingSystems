package midterm;

import java.util.concurrent.Semaphore;

public class RunXtimes {

	/* Initialization */
	static int N = 10;
	static int X = 4;

	static volatile Semaphore lock=new Semaphore(1);
	static volatile Semaphore[] semaphore_count = new Semaphore[N];

	static int[] j = new int[N];
	static int[] count = new int[N];

	public static void main(String[] args) {
		N=10;
		System.out.println("++++++++Start+++++++++");
		for(int i=0; i<N;i++){
			semaphore_count[i] = new Semaphore(1);
			count[i] = X;
			j[i] = 0;
		}
		MyProcess[] p = new MyProcess[N];
		for (int i = 0; i < N; i++)
		{
			p[i] = new MyProcess(i);
			p[i].start();
		}
	}

}

class MyProcess extends Thread
{
	private int id;
	public MyProcess(int i)
	{
		id = i;
	}

	/* More concise version:  Without shared variable protecting semaphores*/
	public void run()
	{
		while(true){
			while(RunXtimes.count[id] <= 0) { 
				System.out.println("Process " + id + " cannot run more than X+1 times");
			}
			System.out.println("Process " + id + " can run more than X+1 times");
			System.out.println("Process " + id + " 's remaining is  " + RunXtimes.count[id]+ " times");

			try {
				RunXtimes.lock.acquire();
			} catch (InterruptedException e) {
			}				
			System.out.println("Process " + id + " entered critical section");
			RunXtimes.lock.release();
			System.out.println("Process " + id + " exited critical section");

			while(true){

				try {
					RunXtimes.semaphore_count[ RunXtimes.j[id] ].acquire();
				} catch (InterruptedException e) {
				}
				if(RunXtimes.j[id]  != id){
					RunXtimes.count[ RunXtimes.j[id]  ]=RunXtimes.X;
				}
				RunXtimes.semaphore_count[  RunXtimes.j[id]  ].release();

				RunXtimes.j[id]++;

				if(RunXtimes.j[id]>=RunXtimes.N)	break;

			}

			RunXtimes.j[id] = 0;	

			RunXtimes.count[id]--;

		}
	}
}