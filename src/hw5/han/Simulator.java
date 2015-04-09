package hw5.han;
// Szu Han Chang
// halo@bu.edu
// CS350 - Bestavros
// Simulator

import java.lang.Math;

public class Simulator {
    // Enter Network K here.
    private int networkQueueSize = 40;

    private String simType;
    private int requests;
    private double runTime;
    private double serviceRate;
    private double arrivalRate;

    private Schedule schedule;

    public Queue CPUqueue = new Queue((int)Float.POSITIVE_INFINITY);
    public Queue Diskqueue = new Queue((int)Float.POSITIVE_INFINITY);
    public Queue Networkqueue = new Queue(networkQueueSize);
    
    public Simulator(double t, double arrive, double service) {
 simType = "Queuing Network";
        runTime = t;
        arrivalRate = arrive;
        serviceRate = service;
        requests = 0;
 networkQueueSize = 0;
        
 // First event initialized and placed in schedule.
        schedule = new Schedule(new Event(0, expRand(arrivalRate), serviceRate, arrivalRate));

 // Start up the monitoring event as well.
        schedule.insert(new Event(3, 0, serviceRate, arrivalRate));
 schedule.insert(new Event(6, 0, serviceRate, arrivalRate));
 schedule.insert(new Event(9, 0, serviceRate, arrivalRate));
    }
    
    public String getSimType() {
 return simType;
    }

    public Event getNext() {
        return schedule.getNext();
    }
    
    public Schedule getSchedule() {
        return schedule;
    }
    
    public int getRequests() {
        return requests;
    }
    
    public void arrival() {
        requests += 1;
    }
    
    public void departure() {
        requests -= 1;
    }
    
    private double expRand(double lambda) {
        return -Math.log(1 - Math.random())/lambda;
    }
    
}