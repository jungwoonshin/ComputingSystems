package hw07;
// Problem 4 Part(A)(C)
// Jungwoon Shin


import java.util.concurrent.Semaphore; 
import java.util.*; 


public class priorities extends Thread { 

	public static Random random = new Random();
	public static int processes = 0; 
	public static int array_processes[] = new int[5]; 
	public static Semaphore sempahore_array[] = new Semaphore[5]; //initialize 5 processes
	public static Semaphore sempahore_mutex = new Semaphore(1, false); 
	public static Semaphore semaphore_scheduler = new Semaphore(0, false); 
	public static Semaphore semaphore_output = new Semaphore(1, false); 
	public int processID; 
	public int type; //1:process, 2:scheduler
	public priorities(int id, int processType) { 
		type = processType; 
		processID = id; 
		sempahore_array[processID] = new Semaphore(0, false); 
	} 
	public void run() 
	{ 
		if (type ==1) //the type is process
		{ 
			while(true) 
			{ 
				try { sempahore_mutex.acquire(); }

				catch (InterruptedException exception){} 

				processes++; 
				try { semaphore_output.acquire(); } 
				catch (InterruptedException exception){}

				System.out.println("P" + processID + " requesting CS"); 
				semaphore_output.release(); 
				array_processes[processID] = 1; 
				if( processes == 1) { semaphore_scheduler.release(); } 
				sempahore_mutex.release(); 

				/* 
      try { sempahore_mutex.acquire(); }

      catch (InterruptedException exception){} 

      //increase process
      processes++; 

      try { semaphore_output.acquire(); } 

      catch (InterruptedException exception){}

      System.out.println("P" + processID + " requesting CS"); 

      //release output, scheduler, mutex
      semaphore_output.release(); 
      array_processes[processID] = 1; 

      if( processes == 1) { semaphore_scheduler.release(); } 

      sempahore_mutex.release();  */ 

				try { sempahore_array[processID].acquire(); }
				catch (InterruptedException exception){} 

				try { semaphore_output.acquire(); }

				catch (InterruptedException exception){} 

				System.out.println("P" + processID + " in critical section"); 
				busy(200); 

				semaphore_output.release(); 
				try { sempahore_mutex.acquire(); }
				catch (InterruptedException exception){} 
				array_processes[processID] = 0; 
				processes--; 

				//~~~~~~~~~~~~output~~~~~~~~~~~~~~~~~~~~~

				try { semaphore_output.acquire(); } 

				catch (InterruptedException exception){} 

				System.out.println("P" + processID + " exiting the CS"); 
				semaphore_output.release(); 

				//scheduler, mutex relase
				if(processes > 0) { semaphore_scheduler.release(); } 

				sempahore_mutex.release(); 

				busy(200); 
			} 
		} 
		if (type==2) //process is a scheduler 
		{ 
			while(true) 
			{ 
				/*case statements....type==2 is process*/

				//~~~~~~~~~~~~~~scheduler~~~~~~~~~~~~~~~~~~~

				try { semaphore_scheduler.acquire(); } 

				catch (InterruptedException exception){} 

				/*
       try { semaphore_output.acquire(); } 

     catch (InterruptedException exception){}  */ 

				//~~~~~~~~~~~~mutex~~~~~~~~~~~~~~~~~~~~~

				try { sempahore_mutex.acquire(); } 

				catch (InterruptedException exception){} 

				//loop through all processes
				for(int j=0; j<5 ; j=j+1) 
				{ 
					if(array_processes[j] == 1){ 

						//~~~~~~~~~~~~~~output~~~~~~~~~~~~~~~~~~~

						try { semaphore_output.acquire(); } 

						catch (InterruptedException exception){} 

						System.out.println("P" + j + " signaled"); 

						//output, array, mutex release
						semaphore_output.release(); 
						sempahore_array[j].release(); 

						/* semaphore_mutex.release(); 
          sempahore_array[j].release(); 
						 */ 

						break; 
					} 
				} 
				sempahore_mutex.release(); 
			} 
		} 
	} 
	public void busy(int delay) { 
		try { sleep(random.nextInt(delay)); } 
		catch (InterruptedException exception) {} 
	} 
	public static void main(String[] args) { 

		priorities[] array = new priorities[5]; 
		priorities scheduler; 
		scheduler= new priorities(0, 2); //if scheduler
		scheduler.start(); 
		for (int i = 0; i < 5; i++) { 
			array[i] = new priorities(i, 1);  //if process
			array[i].start(); 

		} 
	} 
}
