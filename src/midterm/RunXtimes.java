package midterm;

import java.util.concurrent.Semaphore;

public class RunXtimes {

	/* Initialization */
	static int N = 10;
	static int X = 4;

	static volatile Semaphore lock=new Semaphore(1);
	static volatile Semaphore[] semaphore_count = new Semaphore[N];
	static volatile Semaphore[] semaphore_xcounter = new Semaphore[N];

	static int[] x_counter = new int[N];
	static int[] count = new int[N];

	public static void main(String[] args) {
		N=10;
		System.out.println("++++++++Start+++++++++");
		for(int i=0; i<N;i++){
			semaphore_count[i] = new Semaphore(1);
			semaphore_xcounter[i] = new Semaphore(1);
			count[i] = X;
			x_counter[i] = 0;
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
					RunXtimes.semaphore_count[ RunXtimes.x_counter[id] ].acquire();
				} catch (InterruptedException e) {
				}
				if(RunXtimes.x_counter[id]  != id){
					RunXtimes.count[ RunXtimes.x_counter[id]  ]=RunXtimes.X;
				}
				RunXtimes.semaphore_count[  RunXtimes.x_counter[id]  ].release();

				RunXtimes.x_counter[id]++;

				if(RunXtimes.x_counter[id]>=RunXtimes.N)	break;

			}

			RunXtimes.x_counter[id] = 0;	

			RunXtimes.count[id]--;

		}
	}

	/* With some shared variable protecting semaphores*/
	public void run1()
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
					RunXtimes.semaphore_count[ RunXtimes.x_counter[id] ].acquire();
				} catch (InterruptedException e) {
				}
				if(RunXtimes.x_counter[id]  != id){
					RunXtimes.count[ RunXtimes.x_counter[id]  ]=RunXtimes.X;
				}
				RunXtimes.semaphore_count[  RunXtimes.x_counter[id]  ].release();


				try {
					RunXtimes.semaphore_xcounter[id].acquire();
				} catch (InterruptedException e) {
				}		
				RunXtimes.x_counter[id]++;
				RunXtimes.semaphore_xcounter[id].release();
				if(RunXtimes.x_counter[id]>=RunXtimes.N)	break;


			}



			try {
				RunXtimes.semaphore_xcounter[id].acquire();
			} catch (InterruptedException e) {
			}	
			RunXtimes.x_counter[id] = 0;	
			RunXtimes.semaphore_xcounter[id].release();




			try {
				RunXtimes.semaphore_count[id].acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			RunXtimes.count[id]--;
			RunXtimes.semaphore_count[id].release();



		}
	}
}