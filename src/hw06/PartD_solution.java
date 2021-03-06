package hw06;

import java.util.concurrent.Semaphore;
import java.util.Random;

class PartD_solution extends Thread
{

  static volatile boolean flag[] = new boolean[2];
  static volatile int Count = 0;  
  static volatile int I;
  static volatile int J;
  
  private int id;
  public PartD_solution(int i)  throws InterruptedException {
    {
      id = i;
    }
  }
  public void run() /*throws InterruptedException*/
  {
    {
      flag[0] = false;
      flag[1] = false;
      int turn    = 0;
      
      flag[I]=true;
      
      turn=J;
      flag[I]=true;
      while (flag[J] && turn==J) {
        //busy waiting
      };
      
      /* CODE OF PROCESS GOES HERE */
      // create new random class
      Random random = new Random();
      // counter 
      int count = 0;
      
      try{
        String [] string = new String [5];
        string[0] = "Thread "+ id + " is starting iteration  ";
        string[1] = "We hold these truths to be self-evident, that all men are created equal,";
        string[2] = "that they are endowed by their Creator with certain unalienable Rights,";
        string[3] = "that among these are Life, Liberty and the pursuit of Happiness.";
        string[4] = "Thread " + id + " is done with iteration ";
        
        // length of string
        int length = string.length;
        
        for (int i=0;i<length;i++)
        { 
          if (i!=4 && i!=0)
            System.out.println(string[i]);
          if (i==4)
            System.out.print(string[i] +i+ "\n");
          if (i==0)
            System.out.print(string[i] +i+ "\n");
          
          Thread.sleep((long)(random.nextDouble()*20));  
        }
      }
      
      catch (Exception e) { System.out.println(e);}
      finally { }   
      flag[I]=false; 
    }
  }
  
  
  
  // ~~~~~~~~~~~~~~~~~~~~~~~~MAIN METHOD~~~~~~~~~~~~~~~~~~~~~~~~
  public static void main(String[] args) throws InterruptedException {
    {       
      final int length = 2;
      PartD_solution[] program = new PartD_solution[length];
      
      for (int i = 0; i < length; i++)
      {
        program[i] = new PartD_solution(i);
        program[i].start();
      }
    }
  }
}
