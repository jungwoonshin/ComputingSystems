package hw05;

import java.util.Random;
import java.util.Scanner; 
public class MM1KSimulator {
	//declare many global variables of things that need to be kept track of. 
	static double lambda = 0;
	double Ts= 0;
	double SR = 1 /Ts;
	static int K=3;


	static double[] qs = new double[100];
	static int qs_counter =0;


	static double GlobalTq=0;
	static int GlobalTqCounter=0;

	static int GlobalQvalue=0; 

	static int type;
	static boolean serverFull=false; 

	static double runningTime = 0;
	static double totaltime = 100;
	public double runTime;

	static double rho;

	static double Q;
	static double Tq;
	static double W;
	static double Tw;

	static int rejections=0;
	static int rejectiontrials=0;

	static double Wcounter = 0;
	static int counter=0;  

	public int requests;

	public Schedule S;

	//make a new queue object
	public Queue queue = new Queue();
	public Random r = new Random();

	public MM1KSimulator(double lamb, double t, double sr) {
		runTime = t;
		lambda = lamb;
		SR = sr;

		//requests start at 0
		requests = 0;

		//create a new birth event, create a new schedule with that event.
		GlobalQvalue++;
		Event newb = new Event (0, exponentialdistribution(lambda), SR, lambda,GlobalQvalue);
		S = new Schedule();
		S.head=newb; 

		// start up the monitor event, which has the event number of 2. 
		Event m = new Event(2,0,SR,lambda,-1);
		S.insert(m);
	}

	public MM1KSimulator(double lambda, double totaltime, double sr,double service_time) {
		runTime = totaltime;
		this.lambda = lambda;
		SR = sr;
		Ts = service_time;

		//requests start at 0
		requests = 0;

		//create a new birth event, create a new schedule with that event.
		GlobalQvalue++;
		Event newb = new Event (0, exponentialdistribution(lambda), SR, lambda,GlobalQvalue);
		S = new Schedule();
		S.head=newb; 

		// start up the monitor event, which has the event number of 2. 
		Event m = new Event(2,0,SR,lambda,-1);
		S.insert(m);
	}

	//actually generates the exponentially distributed values. 
	public double exponentialdistribution(double lambda) {
		double y = Math.random();
		double x = -(Math.log(1-y))/lambda;
		return (x);//return the exponentially generated values
	}

	public class Schedule {
		public Event head = new Event();
		//the head event 
		public int counter = 0;

		public void insert(Event event) {
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

		public double ReturnWValue() {
			return Wcounter/counter;
		}
	}

	public class Queue {
		public double Tqvalue; 
		public int tracker;
		public double birthtracker; 

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
		public void dequeue(Event event) {
			if(event.Qvalue==tracker){
				this.Tqvalue=event.time-birthtracker;
			}
			if(queueCounter!=0) {
				queueCounter --;
			}
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

		public Event() {
		}

		public Event(int type, double t, double s, double b, int Qvalue) {
			time = t;
			action = type;
			SR = s;
			birthRate = b;
			nextevent = null;
			nextInQueue = null;
			this.Qvalue=Qvalue; 
		}

		// Performs an action given a certain request, schedule and queue state
		public void action(int requests, Schedule schedule, Queue queue) {
			if (action==0 ){ 
				queue.enqueue(this);
				//if the number of request is just 1, then schedule the death as well 
				if (requests == 1) {
					//if the number of requests is just one, schedule the death 

					schedule.insert(new Event(1, exponentialdistribution(SR) + time, SR, birthRate,GlobalQvalue));
				}
				//insert a birth into the schedule 
				GlobalQvalue++;
				schedule.insert(new Event(0, exponentialdistribution(birthRate) + time, SR, birthRate,GlobalQvalue));    
			}

			// if the event type is that of a death
			else if (action==1) {
				queue.dequeue(this);
				if (requests >= 1) {
					//if there are more than 1 request in the queue.  
					if (queue.head!=null) {
						Event nextBirth = queue.head;

						double arriveTime = nextBirth.time; 

						//set the time of the next birth and the next death 
						double deathTime = time;
						double start = max(arriveTime, deathTime);

						//the next starttime must occur after the last arrival or death time
						//insert the event exponentially to generate a service time 
						schedule.insert(new Event(1, start + exponentialdistribution(SR), SR, birthRate,GlobalQvalue));
					}
				}
			}

			//if the event type is that of a monitoring event. 
			else if (action==2) {
				GlobalTq+=queue.Tqvalue;
				GlobalTqCounter++;
				Wcounter+=queue.queueCounter;
				//update the monitor, take W and add the counter to it to set the NEW w. 
				S.counter++;


				qs[qs_counter]= queue.queueCounter;
				qs_counter++;

				//update counter variable, which is the number of monitor events
				//insert the monitor event.  
				schedule.insert(new Event(2, time + monitor, SR, birthRate,-1));
			}
		}
	}

	public Event makenewcurrentm(MM1KSimulator mm1f) {
		Event current= new Event();
		//make a new current event, and make it the next event.  
		current = S.returnNext();
		return current;
	}

	public void controller(MM1KSimulator simulator) {
		Event current = simulator.makenewcurrentm(simulator);

		runningTime = current.time;

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
		}
		Q = S.ReturnWValue();
		Tq = Q / lambda;
		double rejectionprobability=(double)rejections/(double)rejectiontrials;
//		System.out.println("THE CALCULATED VALUES FOR THIS MM1K Simulator ARE: \n");
//		System.out.println("Total time of simulation: " + totaltime);
//		System.out.println("K: " + K);
//		System.out.println("Ts:  " + Ts);
//
//		System.out.println("q = " + Q);
//		System.out.println("Tq = " + Tq);
		System.out.println("Rejection Probability: " + rejectionprobability);
	}

	public static void main(String[] args) {

		Scanner kb = new Scanner(System.in);
//		computeEvalue("(A)",3, 50, 0.020, 100); // q=1.45, Tq = 0.0378
//		computeEvalue("(B)",3, 50, 0.015, 100); // q=1.14  Tq = 0.02
		computeEvalue("(C)",3, 65, 0.020, 100); //



	}

	private static void computeEvalue(String str, int k_value, int l, double serivce_time, int simulation_time) {
		K = k_value;
		lambda= l;
		totaltime= simulation_time;
		double sr = 1/serivce_time;
		MM1KSimulator simulator = new MM1KSimulator(lambda, totaltime, sr, serivce_time);
		simulator.controller(simulator);


		double average=0;
		for(int i=0;i<100;i++){
			average+=qs[i];
		}
		average = average/100.0;

		double variance=0;
		for(int i=0;i<100;i++){
			variance+= Math.pow(qs[i]-average, 2.0);
		}
		variance = variance/100;
		System.out.println("========"+str+" ===========");
		System.out.println("===== q value =====");
		System.out.println("q: " + average);
		System.out.println("variance: " + variance);
		System.out.println("st.dev: " + Math.sqrt(variance));
		System.out.println("e: " + 1.96*Math.sqrt(variance)/Math.sqrt(100));

		for(int i=0;i<100;i++){
			qs[i]/=lambda;
		}

		average=0;
		for(int i=0;i<100;i++){
			average+=qs[i];
		}
		average = average/100.0;

		variance=0;
		for(int i=0;i<100;i++){
			variance+= Math.pow(qs[i]-average, 2.0);
		}
		variance = variance/100;
		System.out.println("===== Tq value =====");
		System.out.println("Tq: " + average);
		System.out.println("variance: " + variance);
		System.out.println("st.dev: " + Math.sqrt(variance));
		System.out.println("e: " + 1.96*Math.sqrt(variance)/Math.sqrt(100));
		qs_counter = 0;
	}

}