package hw08;

import java.util.concurrent.Semaphore;
import java.util.*;

public class Problem2 {
	static volatile int[] request = new int[5];
	static volatile int[] dest = new int[5];
	static volatile Semaphore[] floors = new Semaphore[5];
	static volatile Semaphore[] arrive = new Semaphore[5];
	static volatile Semaphore mutex = new Semaphore(1, false);


	static int c = 5;
	static Random rand = new Random();
	static Occupant2[] p = new Occupant2[20];
	public static void main(String[] args){
		for(int x = 0; x < 5; x++){
			floors[x] = new Semaphore(0,false);
			arrive[x] = new Semaphore(0,false);
		}

		for(int i = 0; i < 20; i++){
			p[i] = new Occupant2(rand.nextInt(5), i, rand.nextInt(5));
			p[i].start();
		}

		Elevator2 elev = new Elevator2();
		elev.start();

	}
}


class Elevator2 extends Thread {
	static Occupant2[] p;
	static volatile Semaphore capacity = new Semaphore(3, false);

	int curFloor;
	int checkFloor;

	static boolean spcBtnClicked = false;
	boolean up = true;
	public void run(){
		while(true){

			if(up){
				for(int checkFloor = curFloor; checkFloor < 5; checkFloor++){
					if(Problem2.dest[checkFloor] >0 || (capacity.availablePermits() > 0 && Problem2.request[checkFloor] > 0)){
						try {Thread.sleep(Math.abs(checkFloor - curFloor)*Problem2.c);} 
						catch (InterruptedException e) {} 

						curFloor = checkFloor;

						while(Problem2.dest[curFloor] >  0){

							try {
								Problem2.mutex.acquire();
							} catch (InterruptedException e1) {
							}
							Problem2.dest[curFloor]--;
							Problem2.mutex.release();
							Problem2.arrive[curFloor].release();
							Elevator2.capacity.release();
						}

						try {Thread.sleep(200);} 
						catch (InterruptedException e1) {}

						while(capacity.availablePermits() > 0 && Problem2.request[curFloor] > 0){
							Problem2.request[curFloor]--;
							if(!spcBtnClicked)Problem2.floors[curFloor].release();
							try {
								Elevator2.capacity.acquire();
							} catch (InterruptedException e) {

							}
						}

					}

				}
				up = false;
			}


			if(!up){
				for(int checkFloor = curFloor; checkFloor >= 0; checkFloor--){
					if(Problem2.dest[checkFloor] >0 || (capacity.availablePermits() > 0 && Problem2.request[checkFloor] > 0)){

						try {
							Thread.sleep(Math.abs(checkFloor - curFloor) * Problem2.c);
						} catch (InterruptedException e) {
						} 
						curFloor = checkFloor;
						while(Problem2.dest[curFloor] > 0){
							try {
								Problem2.mutex.acquire();
							} catch (InterruptedException e1) {
							}
							Problem2.dest[curFloor]--;
							Problem2.mutex.release();
							Problem2.arrive[curFloor].release();
							Elevator2.capacity.release();
						}

						try {Thread.sleep(50);} 
						catch (InterruptedException e1) {}

						while(capacity.availablePermits() > 0 && Problem2.request[curFloor] > 0){
							Problem2.request[curFloor]--;
							Problem2.floors[curFloor].release();
							try {
								Elevator2.capacity.acquire();
							} catch (InterruptedException e) {

							}
						}

					}

				}
				up = true;
			}

		}
	}
}


class Occupant2 extends Thread{
	private Random rand = new Random();
	private int onFloor;
	private int occupantID;
	private boolean isCleaner = false;
	private int reqFloor = 0 ;

	public Occupant2(int curFloor, int occupantID, int reqFloor){
		onFloor = curFloor;
		this.occupantID = occupantID;
		this.reqFloor = getReqFloor(isCleaner);
	}

	public Occupant2(int curFloor, int occupantID, int reqFloor, boolean isCleaner){
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
			runRegularOccupant();

		}
	}

	private void runRegularOccupant() {

		try{ Problem2.mutex.acquire();}
		catch(InterruptedException e){ }

		Problem2.request[onFloor]++;

		Problem2.mutex.release();

		System.out.println("Occupant "+ occupantID + " pushed buttton on floor "+ onFloor);

		try{
			Problem2.floors[onFloor].acquire();
		}
		catch(InterruptedException e){
		}
		System.out.println("Occupant "+ occupantID + " has entered on "+ onFloor);
		try {
			Problem2.mutex.acquire();
		} catch (InterruptedException e1) {
		}
		Problem2.dest[reqFloor]++;
		Problem2.mutex.release();

		System.out.println("Occupant: "+ occupantID + " in elevator wants to go to floor "+ reqFloor);

		try{
			Problem2.arrive[reqFloor].acquire();
		}

		catch(InterruptedException e){}

		System.out.println("Occupant: "+ occupantID + " has left on "+ reqFloor);

		try{
			Thread.sleep(Problem2.c * rand.nextInt(100));
		}
		catch(InterruptedException e){
		}

		reqFloor = rand.nextInt(5);
	}
}