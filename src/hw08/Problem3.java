package hw08;

import java.util.concurrent.Semaphore;
import java.util.*;

public class Problem3 {
	static volatile int[] request = new int[5];
	static volatile int[] dest = new int[5];
	static volatile Semaphore[] floors = new Semaphore[5];
	static volatile Semaphore[] arrive = new Semaphore[5];
	static volatile Semaphore mutex = new Semaphore(1, false);


	static int c = 5;
	static Random rand = new Random();
	static Occupant[] p = new Occupant[20];
	public static void main(String[] args){
		for(int x = 0; x < 5; x++){
			floors[x] = new Semaphore(0,false);
			arrive[x] = new Semaphore(0,false);
		}

		for(int i = 0; i < 20; i++){
			p[i] = new Occupant(rand.nextInt(5), i, rand.nextInt(5));
			//create a cleaner guy
			if(i==19){
				p[i] = new Occupant(1, i, rand.nextInt(5), true);

			}


			p[i].start();
		}

		Elevator elev = new Elevator();
		elev.setOccupants(p);
		elev.start();

	}
}


class Elevator extends Thread {
	static Occupant[] p;
	static volatile Semaphore capacity = new Semaphore(3, false);
	static volatile Semaphore speicalButton =  new Semaphore(1, false);

	int curFloor;
	int checkFloor;

	static boolean spcBtnClicked = false;
	boolean up = true;
	public void run(){
		while(true){
			if(spcBtnClicked){
				Problem3.c = 5;

				Elevator.capacity.release();
				Elevator.capacity.release();
				Elevator.capacity.release();

			}

			if( Problem3.c==1){
				try {
					speicalButton.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				spcBtnClicked = true;
				System.out.println("****speical btn is set to true");

			} 


			if(up){
				for(int checkFloor = curFloor; checkFloor < 5; checkFloor++){
					if(Problem3.dest[checkFloor] >0 || (capacity.availablePermits() > 0 && Problem3.request[checkFloor] > 0)){
						try {Thread.sleep(Math.abs(checkFloor - curFloor)*Problem3.c);} 
						catch (InterruptedException e) {} 

						curFloor = checkFloor;

						while(Problem3.dest[curFloor] >  0){

							try {
								Problem3.mutex.acquire();
							} catch (InterruptedException e1) {
							}
							Problem3.dest[curFloor]--;
							Problem3.mutex.release();
							Problem3.arrive[curFloor].release();
							Elevator.capacity.release();
						}

						try {Thread.sleep(200);} 
						catch (InterruptedException e1) {}

						while(capacity.availablePermits() > 0 && Problem3.request[curFloor] > 0){
							Problem3.request[curFloor]--;
							if(!spcBtnClicked)Problem3.floors[curFloor].release();
							try {
								Elevator.capacity.acquire();
							} catch (InterruptedException e) {

							}
						}

					}

				}
				up = false;
			}


			if(!up){
				for(int checkFloor = curFloor; checkFloor >= 0; checkFloor--){
					if(Problem3.dest[checkFloor] >0 || (capacity.availablePermits() > 0 && Problem3.request[checkFloor] > 0)){

						try {
							Thread.sleep(Math.abs(checkFloor - curFloor) * Problem3.c);
						} catch (InterruptedException e) {
						} 
						curFloor = checkFloor;
						while(Problem3.dest[curFloor] > 0){
							try {
								Problem3.mutex.acquire();
							} catch (InterruptedException e1) {
							}
							Problem3.dest[curFloor]--;
							Problem3.mutex.release();
							Problem3.arrive[curFloor].release();
							Elevator.capacity.release();
						}

						try {Thread.sleep(50);} 
						catch (InterruptedException e1) {}

						while(capacity.availablePermits() > 0 && Problem3.request[curFloor] > 0){
							Problem3.request[curFloor]--;
							Problem3.floors[curFloor].release();
							try {
								Elevator.capacity.acquire();
							} catch (InterruptedException e) {

							}
						}

					}

				}
				up = true;
			}

		}
	}
	public void setOccupants(Occupant[] p) {
		this.p = p;
	}
}


class Occupant extends Thread{
	private Random rand = new Random();
	private int onFloor;
	private int occupantID;
	private boolean isCleaner = false;
	private int reqFloor = 0 ;

	public Occupant(int curFloor, int occupantID, int reqFloor){
		onFloor = curFloor;
		this.occupantID = occupantID;
		this.reqFloor = getReqFloor(isCleaner);
	}

	public Occupant(int curFloor, int occupantID, int reqFloor, boolean isCleaner){
		onFloor = 1;
		this.occupantID = occupantID;
		this.reqFloor = getReqFloor(isCleaner);
		this.isCleaner = isCleaner;
	}

	private int getReqFloor(boolean specialButtonIsClicked) {
		if(specialButtonIsClicked ){
			return 1;
		}
		return rand.nextInt(5);
	}

	public void run(){
		while(true){
			if(!isCleaner){
				runRegularOccupant();
			} else {
				runCleanerOccupant();
			}

		}
	}

	private void runCleanerOccupant() {
		try{ Problem3.mutex.acquire();}
		catch(InterruptedException e){ }
		Problem3.request[onFloor]++;
		Problem3.mutex.release();
		System.out.println("!!!cleaner "+ occupantID + " pushed buttton on floor "+ onFloor);
		try{
			Problem3.floors[onFloor].acquire();
		}
		catch(InterruptedException e){
		}
		System.out.println("!!!cleaner "+ occupantID + " has entered on "+ onFloor);
		Problem3.c = 1;
		Elevator.speicalButton.release();
		System.out.println("!!!cleaner "+ occupantID + " has Pushed the special Button");
		try {
			Elevator.capacity.acquire();
			Elevator.capacity.acquire();
			Elevator.capacity.acquire();

		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		try {
			Problem3.mutex.acquire();
		} catch (InterruptedException e1) {
		}
		Problem3.dest[reqFloor]++;
		Problem3.mutex.release();

		System.out.println("!!!cleaner: "+ occupantID + " in elevator wants to go to floor "+ reqFloor);

		try{
			Problem3.arrive[reqFloor].acquire();
		}

		catch(InterruptedException e){}

		//		clean the elevator 
		try{
			Thread.sleep(rand.nextInt(5000));
		}
		catch(InterruptedException e){
		}

		//indicating cleaning is complete
		//		Controller.c = 5;
		//		System.out.println("Cleaning is complete");

		System.out.println("****speical btn is set to false  because cleaning is complete");
		Elevator.spcBtnClicked = false;

		System.out.println("!!!cleaner: "+ occupantID + " has left on "+ reqFloor);


		try{
			Thread.sleep(Problem3.c * rand.nextInt(10000));
		}
		catch(InterruptedException e){
		}

		//		reqFloor = rand.nextInt(5);		
	}

	private void runRegularOccupant() {

		try{ Problem3.mutex.acquire();}
		catch(InterruptedException e){ }

		Problem3.request[onFloor]++;

		Problem3.mutex.release();

		System.out.println("Occupant "+ occupantID + " pushed buttton on floor "+ onFloor);

		try{
			Problem3.floors[onFloor].acquire();
		}
		catch(InterruptedException e){
		}
		if(!Elevator.spcBtnClicked)System.out.println("Occupant "+ occupantID + " has entered on "+ onFloor);
		try {
			Problem3.mutex.acquire();
		} catch (InterruptedException e1) {
		}
		Problem3.dest[reqFloor]++;
		Problem3.mutex.release();

		System.out.println("Occupant: "+ occupantID + " in elevator wants to go to floor "+ reqFloor);

		try{
			Problem3.arrive[reqFloor].acquire();
		}

		catch(InterruptedException e){}

		System.out.println("Occupant: "+ occupantID + " has left on "+ reqFloor);

		try{
			Thread.sleep(Problem3.c * rand.nextInt(100));
		}
		catch(InterruptedException e){
		}

		reqFloor = rand.nextInt(5);
	}
}