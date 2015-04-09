package hw5.han;

import java.lang.Math;

public class Event {
    // Sets the monitoring interval.    
    static private double monitor = 1;

    private int eventType;
    private double time, serviceRate, arrivalRate;
    private Event nextInSchedule, nextInQueue;
    private double cpuServiceRate, diskServiceRate, networkServiceRate;

    // Constructors.
    public Event() {
    }
    
    public Event(int type, double t, double service, double arrival) {
        time = t;
        eventType = type;
        serviceRate = service;
        arrivalRate = arrival;
        nextInSchedule = null;
        nextInQueue = null;
 cpuServiceRate = uniformRand(0.01, 0.05);
 diskServiceRate = normRand(0.06, 0.01);
 networkServiceRate = .085;
    }
     
    // Methods.
    // Performs an action (Arrival, Departure, or Monitor).
    public void action(int requests, Schedule schedule, Queue queue) {
 switch (eventType) {

            // CPU arrival
 case 0: {
     queue.enqueue(this);
     // Schedule the event's own departure, since it's the only
     // event in the system.
     if (!schedule.CPUoccupied()) {
  schedule.insert(new Event(1, cpuServiceRate + time, serviceRate, arrivalRate));
  queue.dequeue();
  schedule.CPUinsert(); 
     }
     // Inserts the arrival into the schedule.
     schedule.insert(new Event(0, expRand(arrivalRate) + time, serviceRate, arrivalRate));
     break;
 }
     
     // CPU departure
 case 1: {
     if (queue.getCounter() >= 1) {
  Event nextBirth = new Event();
  nextBirth = queue.getHead();   
  queue.dequeue();
  schedule.insert(new Event(1, time + cpuServiceRate, serviceRate, arrivalRate));
     }
     else { // queue is empty
  schedule.CPUevacuate();
     }
     if (Math.random() <= 0.2) {
  // Goes to disk.
  schedule.insert(new Event(4, time, serviceRate, arrivalRate));
     }
     // else the process is complete!
     
     break;
 }

 // CPU special
 case 2: {
            queue.enqueue(this);
            // Schedule the event's own departure, since it's the only
            // event in the system.
            if (!schedule.CPUoccupied()) {
                schedule.insert(new Event(1, cpuServiceRate + time, serviceRate, arrivalRate));
                queue.dequeue();
                schedule.CPUinsert();
            }

            break;
 
 }
     
     // CPU monitoring
 case 3: {
     //queue.setCounter(queue.getCounter() + 1);
     //schedule.setRequestCount(requests + schedule.getRequestCount());
     //schedule.setCount(schedule.getCount() + 1);
     //System.out.println(queue.getCounter());

     System.out.println(queue.getCounter());
     int temp = 0;
     if (schedule.cpu1)
  temp += 1;
     if (schedule.cpu2)
  temp += 1;

     queue.q = queue.getCounter() + temp + queue.q;
     queue.qCount++;
     schedule.insert(new Event(3, time + monitor, serviceRate, arrivalRate));
     break;
 }


     // Disk arrival
 case 4: {
     queue.enqueue(this);
     // Schedule event's own departure, since it's the only event in the system.
     if (schedule.isDiskEmpty) {
  schedule.insert(new Event(5, diskServiceRate + time, serviceRate, arrivalRate)); 
  queue.dequeue();
  schedule.isDiskEmpty = false;
     }
     
     break;
 }

     // Disk departure
 case 5: {
     
            if (queue.getCounter() >= 1) {
                Event nextBirth = new Event();
                nextBirth = queue.getHead();
                queue.dequeue();
  schedule.insert(new Event(5, time + diskServiceRate, serviceRate, arrivalRate));
            }
     else {
  schedule.isDiskEmpty = true;
     }
     if (Math.random() <= 0.3) {
  // Back to CPUs.
  schedule.insert(new Event(2, time, serviceRate, arrivalRate));
     }
     else {
  // Down to Network.
  schedule.insert(new Event(7, time, serviceRate, arrivalRate));
     }

     break;
 }

     // Disk monitoring
 case 6: {
            //schedule.setRequestCount(requests + schedule.getRequestCount());
            //schedule.setCount(schedule.getCount() + 1);
     int temp = 0;
     if (!schedule.isDiskEmpty)
  temp = 1;
     queue.q = queue.q + queue.getCounter() + temp;
     queue.qCount++;
            schedule.insert(new Event(6, time + monitor, serviceRate, arrivalRate));
            break;
 }

     // Network arrival
 case 7: {
            queue.enqueue(this);
            // Schedule event's own departure, since it's the only event in the system.
            if (schedule.isNetworkEmpty) {
                schedule.insert(new Event(8, networkServiceRate + time, serviceRate, arrivalRate));
                queue.dequeue();
  schedule.isNetworkEmpty = false;
            }

            break;
 }

     // Network departure
 case 8: {
            
            if (queue.getCounter() >= 1) {
                Event nextBirth = new Event();
                nextBirth = queue.getHead();
                queue.dequeue();
  schedule.insert(new Event(8, time + networkServiceRate, serviceRate, arrivalRate));
            }

     else {
  schedule.isNetworkEmpty = true;
     }

            if (Math.random() <= 0.4) {
  // Goes back to disk
                schedule.insert(new Event(4, time, serviceRate, arrivalRate));
     }
            else {
  // Goes back to CPUs
                schedule.insert(new Event(2, time, serviceRate, arrivalRate));
     }

     break;
 }


     // Network monitoring
 case 9: {
     //schedule.setRequestCount(requests + schedule.getRequestCount());
            //schedule.setCount(schedule.getCount() + 1);
     int temp = 0;
     if (!schedule.isNetworkEmpty)
  temp = 1;
     queue.q = queue.q + queue.getCounter() + temp;
     queue.qCount++;
            schedule.insert(new Event(9, time + monitor, serviceRate, arrivalRate));
            break;
 }
 }
    }
    
    public double getTime() {
        return time;
    }    
    
    private double expRand(double lambda) {
        return -Math.log(1 - Math.random()) / lambda;
    }

    private double uniformRand(double a, double b) {
 double rand = Math.random();
 while (rand < a | rand > b) {
     rand = Math.random();
 }
 return rand;
    }

    private static double normStdDist(int n) {
        double sum = 0;
        double clt = n;
        for (int i = 0; i <= clt; i++) {
            sum += Math.random();
        }
        sum = (sum - clt * 0.5) / (Math.sqrt(100) / Math.sqrt(12));
        return sum;
    }

    private static double normRand(double u, double s) {
        double sigma = s;
        double mu = u;
        double ret = normStdDist(50) * sigma + mu;
        if (ret < 0)
            ret = 0;
        return ret;
    }

    public Event getNextEvent() {
        return nextInSchedule;
    }
    
    public Event getNextInQueue() {
        return nextInQueue;
    }
    
    public void setNextEvent(Event event) {
        nextInSchedule = event;
    }
    
    public void setNextEvent() {
        nextInSchedule = null;
    }
    
    public void setNextInQueue(Event event) {
        nextInQueue = event;
    }
    
    public int getType() {
        return eventType;
    }
   
    
}