package hw5.han;
// Szu Han Chang
// halo@bu.edu
// CS350 - Bestavros
// Simulator K

import java.lang.Math;
import java.util.Random;
import java.io.*;

public class Main {

    // Initialize - please set values here for lambda, Ts, and running time!
    // Lambda.
    static double arrivalRate = 40;
    static double serviceRate = 0;

    // Simulation running time.
    static double totaltime = 100;

    static int type, counter, rejectCount, requestCount = 0;
    static int queuesize;
    static double globaltime = 0;
    static double rho, meanQ, meanTq, meanW, meanTw, totalW, rejrate = 0;
    static String simType = null;

    public Main() {
    }
    
    public static void main(String[] args) {

        Simulator simulator = new Simulator(totaltime, arrivalRate, serviceRate);
        
 // Schedules the current event.
        Event current = new Event();
        current = simulator.getNext();

 // Sets the globaltime to the event's new time.
        globaltime = current.getTime();

 // Loop to go through the schedule until time runs out.
        while (globaltime < totaltime) {
 
            type = current.getType();

     switch (type) {
      // CPU arrival from lambda
     case 0: 
  // CPU death
     case 1: 
  // CPU arrival from Disk or Network
     case 2: 
  // CPU monitor
     case 3:
  current.action(simulator.getRequests(), 
          simulator.getSchedule(), 
          simulator.CPUqueue);


  break;
     
  // Disk arrival
     case 4: 
  // Disk death  
     case 5: 
  // Disk monitor
     case 6: 
  current.action(simulator.getRequests(), 
          simulator.getSchedule(), 
          simulator.Diskqueue);
  break;
     
  // Network arrival
     case 7: 
  // Network death
     case 8: 
  // Network monitor
     case 9: 
  current.action(simulator.getRequests(), 
          simulator.getSchedule(), 
          simulator.Networkqueue);
  break;
     

     default: break;
     }

     current = simulator.getNext();
     globaltime = current.getTime();
     //System.out.println("type " + type);
     //System.out.println("count " + simulator.CPUqueue.getCounter());
        }
 
 simType = simulator.getSimType();
 
        System.out.println(simType + " System");
        System.out.println("Lambda: " + arrivalRate);
        System.out.println("Simulation time: " + totaltime);

 
 double totalQ = 0;
 totalQ = simulator.CPUqueue.q/simulator.CPUqueue.qCount 
     + simulator.Diskqueue.q/simulator.Diskqueue.qCount
     + simulator.Networkqueue.q/simulator.Networkqueue.qCount;
     
 System.out.println("Average Tq for total system: " + totalQ/arrivalRate);

 double rejectionRate = (double)simulator.Networkqueue.getCounter()/
          (double)simulator.Networkqueue.getRequests();
 System.out.println("Percentage rejected: " + rejectionRate);
        //System.out.println("CPU averageQ: " + simulator.getSchedule().getRequestCount());
 //CPUqueue.getRequests());
        //System.out.println("CPU W: " + simulator.CPUqueue.getCounter());
 /*
        System.out.println("Disk Q: " + simulator.getSchedule().diskQ);
        System.out.println("Disk W: " + simulator.getSchedule().diskW);
        System.out.println("Network Q: " + simulator.getSchedule().networkQ);
        System.out.println("Network W: " + simulator.getSchedule().networkW);
 */
    }     
}