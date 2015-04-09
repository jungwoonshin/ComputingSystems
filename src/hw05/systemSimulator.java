package hw05;
import hw05.MM1KSimulator.Event;

import java.util.ArrayList;
import java.util.Random;

public class systemSimulator {

	ArrayList<Double> DISKdis_values=new ArrayList<Double>();
	ArrayList<Double> CPUdis_values = new ArrayList<Double>();

	int K=3;
	
	//declare many global variables of things that need to be kept track of. 
	double lambda = 50;
	//	static double lambda = 1;
	double Ts= .015;
	double SR = 1 /Ts;

	double q_cpu=0;
	double rho_cpu=0;

	double q_disk=0;
	double rho_disk=0;

	double q_network=0;
	double rho_network=0;


	int rejections=0;
	int rejectiontrials=0;

	static int type;

	double runningTime = 0;
	double totaltime = 100;
	public double runTime;

	double rho;
	double Q;
	double Tq;
	double W;
	double Tw;

	double Wcounter = 0;
	int counter=0;  

	public int requests;


	public Schedule S;

	//make a new queue object
	public Queue queue = new Queue();
	public Random r = new Random();

	public systemSimulator(int k, double t, double l, double sr ) {
		k = k;
		runTime = t;
		lambda = l;
		SR = sr;

		//you start the number of requests at 0 
		requests = 0;

		//create a new birth event, create a new schedule with that event.
		Event newb = new Event (0, exponentialdistribution(lambda), 1/CPUdistr(), lambda);
		S = new Schedule();
		S.head=newb; 

		// start up the monitor event, which has the event number of 2. 
		Event m= new Event(2,0,1/CPUdistr(),lambda);
		S.insert(m);
	}

	//actually generates the exponentially distributed values. 
	public double exponentialdistribution(double lambda) {
		double y = Math.random();
		double x = -(Math.log(1-y))/lambda;
		return (x);//return the exponentially generated values
	}

	public static double Zrand() {
		int tally=30;
		double sum=0; 
		for (int i=0;i<tally; i++) {
			double y = Math.random();
			sum+=y;
		}

		return sum-(tally*.5);
	}

	public static double DISKdistr() {
		double mean=100;
		double stdev=20;
		//f you want to make a distribution with mean m and deviation s, simply multiply by s and then add m. 
		double x = Zrand();
		x=x * stdev+ mean;
		return x/1000;
	}

	public double CPUdistr() {
		double y=0;
		y=Math.random()*20+10; 
		return (y/1000);
	}

	public class Schedule {
		public Event head = new Event();
		public double CPUdistr;
		//the head event 
		public int counter = 0;
		//counter for the number of requests in the queue

		public void insert(Event event) {
			CPUdistr = event.CPUdistr;
			// Nothing in schedule.
			if (head == null) {
				head = event;
			}

			//if the event time of the head is GREATER than the event time.
			else if (head.time >= event.time) {

				Event x = new Event();
				x = head;
				event.nextevent=x;
				//set the head, insert event x
				head = event;
			}
			//insert an event into the schedule
			else {
				Event prev = new Event();
				prev = findPrevious(event.time);
				event.nextevent= prev.nextevent;
				prev.nextevent=event;
			}
		}

		//find the previous event 
		public Event findPrevious(double time) {
			Event p = new Event();
			p = head;
			for(p=head;p.nextevent != null;p = p.nextevent) {
				//go through and while the next event is not null, you keep checking the next event
				if (p.nextevent.time >= time) {
					return p;
				}
			}
			return p;
		}

		public Event returnNext() {
			Event next = new Event();
			next = head;
			head = head.nextevent;
			return next;
		}
		//		public double getCPUdistr(){
		//			return event.CPUdistr;
		//		}
		public double ReturnWValue() {
			return Wcounter/counter;
		}
	}
	
	public class Network_Queue{
		public Event head = new Event();
		//queue keeps track of the head. 
		public int queueCounter;

		
		
		public double Tqvalue; 
		public int tracker;
		public double birthtracker; 
		
		
		public Network_Queue() {
			//Queue constructor. 
			head = null;
			queueCounter = 0;
		}

		//		//enqueue, change head, add to counter.  
		//		public void enqueue(Event event) {
		//			queueCounter++;
		//			if (head == null) {
		//				head = event;
		//			}
		//			else {
		//				Event i = new Event();
		//				for (i =head;i.nextInQueue != null; i = i.nextInQueue) {
		//				}
		//				i.nextInQueue=event;
		//			}
		//		}

		//enqueue, change head, add to counter.  
		public void enqueue(Event event) {   
			tracker=event.Qvalue;
			birthtracker=event.time;
			rejectiontrials++;
			if (head == null) {
				queueCounter++;
				head = event;
			}
			else if (queueCounter<K){
				queueCounter++;

				Event i = new Event();
				for (i =head;i.nextInQueue != null; i = i.nextInQueue) {
				}
				i.nextInQueue=event;
			}
			else { 
				rejections++;
				Event i = new Event();
				for (i =head;i.nextInQueue != null; i = i.nextInQueue) {
				}
				i.nextInQueue=event;

			}
		}

		//dequeue, make new head
		public void dequeue() {
			queueCounter --;
			head = head.nextInQueue;
		}
	}

	public class Queue {
		public Event head = new Event();
		//queue keeps track of the head. 
		public int queueCounter;
		
		public Queue() {
			//Queue constructor. 
			head = null;
			queueCounter = 0;
		}

				//enqueue, change head, add to counter.  
				public void enqueue(Event event) {
					queueCounter++;
					if (head == null) {
						head = event;
					}
					else {
						Event i = new Event();
						for (i =head;i.nextInQueue != null; i = i.nextInQueue) {
						}
						i.nextInQueue=event;
					}
				}


		//dequeue, make new head
		public void dequeue() {
			queueCounter --;
			head = head.nextInQueue;
		}
	}

	public double max(double a, double b) {
		if (a>=b) {
			return a; 
		} else {
			return b;  
		}
	}

	public class Event {
		public double monitor = 1;
		//set a new monitor event
		public int action;
		public double time;
		public double SR;
		public double birthRate;
		public Event nextevent; 
		public Event nextInQueue;
		public int Qvalue; 

		public double CPUdistr;
		public double NETWORKdistr;
		public double DISKdistr;

		public Event() {
		}

		public Event(int type, double t, double service_rate, double b) {
			time = t;
			action = type;
			SR = service_rate;
			birthRate = b;
			nextevent = null;
			nextInQueue = null;
			CPUdistr = 1/service_rate;
		}

		// Performs an action given a certain request, schedule and queue state
		public void action(int requests, Schedule schedule, Queue queue) {
			if (action==0){
				queue.enqueue(this);
				//if the number of request is just 1, then schedule the death as well 
				if (requests == 1) {

					schedule.insert(new Event(1, CPUdistr() + time,1/ CPUdistr(), birthRate));
				}
				//insert the birth into the schedule
				schedule.insert(new Event(0, exponentialdistribution(lambda) + time, 1/CPUdistr(), birthRate));    
			}

			// if the event type is that of a death
			else if (action==1) {
				queue.dequeue();
				double R= Math.random();
				//				System.out.println("R: " + R);
				if (0.5<=R) {
					//if just a regular death, then you just do nothing
				} else if (R>=0.1 && R<.5) {
					schedule.insert(new Event(0, .025 + time, 1/CPUdistr(), birthRate));
				} else if (R>=0. && R<0.1) {
					R = Math.random(); 
					if (R>=.5) { 
						double a =DISKdistr();
						DISKdis_values.add(a);
						// go to CPU after disk I/O
						schedule.insert(new Event(0,   a+ time, 1/CPUdistr(), birthRate));
					} else {
						double a =DISKdistr();
						DISKdis_values.add(a);
						// go to network
						schedule.insert(new Event(0,a + .025 + time, 1/CPUdistr(), birthRate));
					}
				}
			}

			//if the event type is that of a monitoring event. 
			else if (action==2) {
				Wcounter+=queue .queueCounter;
				//update the monitor, take W and add the counter to it to set the NEW w. 
				S.counter++;
				//update the counter, which is the number of monitor events
				//actually insert the monitor event.  
				schedule.insert(new Event(2, time + monitor, 1/CPUdistr(), birthRate));
			}
		}
		//most of these access and set functions allow for easy access and ajustment of various variables
	}

	public Event makenewcurrentm(systemSimulator mm1f) {
		Event current= new Event();
		//make a new current event, and make it the next event.  
		current = S.returnNext();
		return current;
	}

	public void controller(systemSimulator simulator, double l) {//what actually runs the main simulation
		Event current = simulator.makenewcurrentm(simulator);

		// Sets the overall global time variable  to the event's new time.
		runningTime = current.time;

		// Loop to go through the schedule until time runs out, this is the main controller loop. 
		while (runningTime < totaltime) {
			type = current.action;

			if(type==0) {
				requests++;
			}
			else if (type==1) { 
				requests--;
			}

			//current action is based on the current schedule and the current queue.  
			current.action(requests, S, queue);
			
			
			
			current = S.returnNext();
			//set the new global time.  
			runningTime = current.time;
			//      System.out.println(queue.queueCounter);
		}

		double DISKdist=0.0;
		for(double a :DISKdis_values){
			DISKdist+=a;
		}
		if(DISKdis_values.isEmpty()){
			DISKdis_values.add(0.);
		}
		DISKdist/=DISKdis_values.size();

		//acutal
		getQ_all(DISKdist, l);

		//minimum response time
		//getQ_all(DISKdist, 1);

	}

	private void getQ_all(double DISKdist, double l) {

		rho_cpu = l * S.CPUdistr;
		//		rho_cpu /=2.0;

		rho_disk = (l/5.0)*DISKdist;
		rho_network=l*0.9*0.025;

		q_cpu = rho_cpu/(1-rho_cpu);
		q_disk =rho_disk/(1-rho_disk);
		q_network = rho_network/(1-rho_network);

		double q_all =0.;
		q_all =+ q_cpu;
		q_all+=q_disk;
		q_all+=q_network;
		//		System.out.println("rho_cpu: " + rho_cpu +", rho_disk: " + rho_disk + ", rho_network: " + rho_network);

		/* For problem 3 (a),(c)-cpu-queue*/

		//				System.out.println("ro_cpu: " +rho_cpu +"q_cpu: "+q_cpu);
		if(q_cpu>0)System.out.println(q_cpu);

		/* For problem 3 (c)-response-time*, 4 (a)-actual*/
		//		if(q_all/lambda>=0)
		//		System.out.println(q_all/lambda);
		//if(q_cpu>=0&&q_cpu<=30)
		//		System.out.println(q_cpu);
	}



	public static void main(String[] args) {
		//run 100 times
		runSimulator(40, 100); // lambda = 40, normal.
		//		runSimulator(1, 100); // lambda = 1, minimum.


		// run once
		//		runSimulator(40, 1);
		//		runSimulator(1, 1);
	}

	private static void runSimulator(double l, int times) {
		for (int i=0; i<times; i++){
			systemSimulator simulator = new systemSimulator(1,i, l, 50);
			simulator.controller(simulator,l);
		}
	}
}