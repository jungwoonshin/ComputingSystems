package hw07;
import java.util.concurrent.Semaphore;
import java.util.ArrayList;
import java.util.Random;

class MyProcess extends Thread
{
  private int id;
  private int[] semaphore;
  public MyProcess(int i)  throws InterruptedException {
    {
      id = i;
      semaphore = new int[100];
    }
  }
  public void run(ArrayList<MyProcess> queue) throws InterruptedException /*throws InterruptedException*/
  {
    for(int i=0;i<100;i++){
    	wait(i, queue);
    }
  }
  public void start(ArrayList<MyProcess> queue) throws InterruptedException
  {
   run(queue);
    
  }
  
  public void wait(int i,ArrayList<MyProcess> queue) throws InterruptedException{
	  Random r = new Random();
	  semaphore[i]--;
	  if( semaphore[i]<0){
          Thread.sleep((long)(r.nextDouble()*20000));  
		  queue.add(this);
	  }
  }
  
  public void signal(int i, ArrayList<MyProcess> queue){
	  semaphore[i]++;
	  if(semaphore[i]<=0){
		  queue.remove(MyProcess.class);
	  }
	  
	  
  }
  
  public static void main(String[] args) throws InterruptedException {
    { 
      ArrayList<MyProcess> queue = new ArrayList<MyProcess>();
      final int N = 2;
      MyProcess[] p = new MyProcess[N];
      for (int i = 0; i < N; i++)
      {
        p[i] = new MyProcess(i);
        p[i].start(queue);
      }

      
    }
  } 
  
}





