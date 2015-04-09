package hw06;

import java.util.concurrent.Semaphore;
import java.util.Random;

class MyProcess extends Thread
{
  private int id;
  public MyProcess(int i)  throws InterruptedException {
    {
      id = i;
    }
  }
  public void run() /*throws InterruptedException*/
  {
    {
      /* CODE OF PROCESS GOES HERE */
      Random R = new Random();
      
      //System.out.println("Thread "+ id + " is starting iteration k ");
      try{
//        Thread.sleep((long)(R.nextDouble()*2000));
//        System.out.println("We hold these truths to be self-evident, that all men are created equal,");
//        Thread.sleep((long)(R.nextDouble()*2000));
//        System.out.println("that they are endowed by their Creator with certain unalienable Rights,");
//        Thread.sleep((long)(R.nextDouble()*2000));
//        System.out.println("that among these are Life, Liberty and the pursuit of Happiness.");
//        Thread.sleep((long)(R.nextDouble()*2000));
//        System.out.println("Thread "+ id + "  is done with iteration k");
//        Thread.sleep((long)(R.nextDouble()*2000));
        
        String [] str = new String [5];
        str[0] = "Thread "+ id + " is starting iteration k ";
        str[1] = "We hold these truths to be self-evident, that all men are created equal,";
        str[2] = "that they are endowed by their Creator with certain unalienable Rights,";
        str[3] = "that among these are Life, Liberty and the pursuit of Happiness.";
        str[4] = "Thread i is done with iteration k";
        
        int N = str.length;
       
        for (int i=0;i<N;i++)
        { 
         
          System.out.println(str[i]);
          Thread.sleep((long)(R.nextDouble()*2000)); 
        }
        
        
      }
      
      catch (Exception e)
      {
        System.out.println(e);
      }
      finally
      {
      }
      
    }
  }
  public void start()
  {
   run();
    
  }
  
  public static void main(String[] args) throws InterruptedException {
    { 
      
      final int N = 2;
      MyProcess[] p = new MyProcess[N];
      for (int i = 0; i < N; i++)
      {
        p[i] = new MyProcess(i);
        p[i].start();
      }

      
    }
  }
}