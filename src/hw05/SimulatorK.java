package hw05;

import java.lang.Math;
import java.util.*;
 
public class SimulatorK {
     
  // rates
  public double service_rate; // service rate
  public double arrival_rate; // arrival rate 
  public double rejection_rate;
   
  public double run_time;
   
  public int requests_count;
   
  public Calendar calendar;
     
  public QueueClass QueueClass_CPU = new QueueClass(10000000); 
  public QueueClass QueueClass_DISK = new QueueClass(10000000);
  public int queues_in_network = 32; 
  public QueueClass QueueClass_NETWORK = new QueueClass(queues_in_network);
   
  public double network_rho = 20.93*0.02;
  public double power = Math.pow(network_rho, queues_in_network),
           
  rejectionRate =((1-network_rho)*power)/(1-Math.pow(network_rho, queues_in_network+1));
   
  public double qForKIsOne = (network_rho/(1-network_rho)) - ((1+1)*Math.pow(network_rho,1+1) / (1-(Math.pow(network_rho, 1+1))));
   
  public double q = (network_rho/(1-network_rho)) - ((queues_in_network+1)*Math.pow(network_rho,queues_in_network+1) / (1-(Math.pow(network_rho, queues_in_network+1))));
 
  public double lambdaPrime = 20.3*(1-rejectionRate);
   
  public double Tq = q/lambdaPrime;
   
  public double slowdown = Tq/0.02062069;
      
  public SimulatorK() { }
   
  public SimulatorK(double t, double rate_arrival, double rate_service) {
     
    requests_count = 0;
     
    arrival_rate = rate_arrival;
    service_rate = rate_service;
     
    run_time = t;
     
    queues_in_network = 0;
     
    calendar = new Calendar(new CPU_DISK_NETWORK(0, random(arrival_rate), service_rate, arrival_rate));
     
    calendar.insert(new CPU_DISK_NETWORK(3, 0, service_rate, arrival_rate));
    calendar.insert(new CPU_DISK_NETWORK(6, 0, service_rate, arrival_rate));
    calendar.insert(new CPU_DISK_NETWORK(9, 0, service_rate, arrival_rate));
  }
   
 
  public CPU_DISK_NETWORK getNext() { return calendar.getNext();}
  public Calendar getCalendar() { return calendar; }
  public int getRequests() { return requests_count; }
   
  public void arrive() { requests_count = requests_count + 1; }
  public void leave() { requests_count = requests_count - 1; } 
   
  public double random(double arrival_rate) {
    double top = -Math.log(1 - Math.random());
    double num = top/arrival_rate;
    return num;
  }
}
 
 
class Calendar {
   
   
  public CPU_DISK_NETWORK top = new CPU_DISK_NETWORK(); 
   
  public double requests_TOTAL = 0; //total number of requests
   
  public int counter = 0;
  public double requests_count = 0; //requests counter
   
  public boolean disk_check  = true; // check if disk is empty 
  public boolean network_check = true; // check if network is empty 
  public boolean cpu_x = false; //check if cpu1 is in use
  public boolean cpu_y = false; //check if cpu2 is in use
   
   
  public Calendar() { }
   
  public Calendar(CPU_DISK_NETWORK event) { top = event; }
   
 
  public void setCount(int count) { counter = count;} 
  public void setRequestCount(double in) { requests_count = in;}
  public void setTop(CPU_DISK_NETWORK head) { top = head; }
   
 
  public CPU_DISK_NETWORK getTop() { return top; }
  public int getCount() { return counter; }
  public double getRequestCount() { return requests_count;}
   
  public CPU_DISK_NETWORK getPrev(double time) {  // Finds the previous event 
     
    CPU_DISK_NETWORK pointer = new CPU_DISK_NETWORK();
    pointer = top;
     
    while (pointer.getNextCPU_DISK_NETWORK() != null) {
       
      if (pointer.getNextCPU_DISK_NETWORK().getTime() >= time) { return pointer; }
       
      pointer = pointer.getNextCPU_DISK_NETWORK();
    }
     
    return pointer;
  }
   
  public CPU_DISK_NETWORK getNext() { // Finds the next event 
     
    CPU_DISK_NETWORK next = new CPU_DISK_NETWORK();
    next = top;
    top = top.getNextCPU_DISK_NETWORK();
     
    return next;
  }
   
  public void insert(CPU_DISK_NETWORK event) {
     
    if (top == null) { top = event; }
     
    else if (top.getTime() >= event.getTime()) {
      CPU_DISK_NETWORK prev = new CPU_DISK_NETWORK();
       
      prev = top;
      event.setNextCPU_DISK_NETWORK(prev);
      top = event;
    }
     
    else {
       
      CPU_DISK_NETWORK previous = new CPU_DISK_NETWORK();
       
      previous = getPrev(event.getTime());
      event.setNextCPU_DISK_NETWORK(previous.getNextCPU_DISK_NETWORK());
      previous.setNextCPU_DISK_NETWORK(event);
    }
  }
  
   
  public void add_CPU() { // add to CPU
    if (!cpu_x) { cpu_x = true; }
    else if (!cpu_y) { cpu_y = true; }
  }
   
  public boolean isFull_CPU() {
    if (!cpu_x || !cpu_y) { return false; }
    else { return true; }
  }
   
  public void remove_CPU() { // removes from CPU
    if (cpu_x) { cpu_x = false; }
    else if (cpu_y) { cpu_y = false; }
  }
}
 
class QueueClass {
   
   
  public int requests_counter = 0;
  public int queue_count = 0;
  public int q_count = 0;
   
  public int K = 0;
   
  public int q = 0;
  public int w = 0;
   
  public CPU_DISK_NETWORK top = new CPU_DISK_NETWORK();
   
   
   
  public QueueClass () { } 
   
  public QueueClass(int size) {
    K = size;
    top = null;
    queue_count = 0;  
    w = 0;
  }
   
   
  public CPU_DISK_NETWORK getTop() { return top; }  
  public int getCounter() { return queue_count; } 
  public int getK() { return K;}  
  public int getRequests() { return requests_counter; }  
   
  public void add(CPU_DISK_NETWORK event) {
    requests_counter++;
     
    if (K > queue_count) { 
       
      queue_count++;
       
      if (top != null) { 
         
        CPU_DISK_NETWORK pointer = new CPU_DISK_NETWORK();
        pointer = top;
         
        while (pointer.getNextCPU_DISK_NETWORK_QUEUE() != null) { pointer = pointer.getNextCPU_DISK_NETWORK_QUEUE(); }
         
        pointer.setNextInQueueClass(event);
      }
       
      if (top == null) { top = event; }
    } 
     
  }
   
  public void remove() {
     
    if (top == null) {
      queue_count = queue_count;
    }  
     
    if (top != null) {
      queue_count--;
      top = top.getNextCPU_DISK_NETWORK_QUEUE();
    } 
     
  }
}
 
 
class CPU_DISK_NETWORK {
   
   
  public double time;
  public double lifetime; 
   
  public double service_rate; 
  public double arrival_rate;
  public double service_rate_CPU;
  public double service_rate_DISK;
  public double service_rate_NETWORK;
   
  public CPU_DISK_NETWORK calendar_next;
  public CPU_DISK_NETWORK queue_next;
  public int eventType;
  
  static public double mon = 1;
   
   
  public CPU_DISK_NETWORK() {
  }
   
  public CPU_DISK_NETWORK(int cpu_disk_network, double t, double service, double arrival) {
     
 
    service_rate_CPU = random_UNIFORM(0.01, 0.05);
    service_rate_DISK = random_NORMAL(0.06, 0.01);
    service_rate_NETWORK = .085;
    service_rate = service;
    arrival_rate = arrival;
     
    time = t;
     
    calendar_next = null;
    queue_next = null; 
    eventType = cpu_disk_network;
  }
   
   
  public int getType() { return eventType; }
  public double getTime() { return time; }   
  public CPU_DISK_NETWORK getNextCPU_DISK_NETWORK() { return calendar_next; }
  public CPU_DISK_NETWORK getNextCPU_DISK_NETWORK_QUEUE() { return queue_next; }
   
  public void setNextCPU_DISK_NETWORK() { calendar_next = null; }
  public void setNextInQueueClass(CPU_DISK_NETWORK event) { queue_next = event; }
  public void setNextCPU_DISK_NETWORK(CPU_DISK_NETWORK event) { calendar_next = event; }
   
  public double random(double arrival_rate) {
    double top = -Math.log(1 - Math.random());
    double num = top / arrival_rate;
    return num;
  }
   
  public double random_UNIFORM(double x, double y) { //for uniform distribution 
    double random_num = Math.random();
    while (random_num < x | random_num > y) { random_num = Math.random();}
    return random_num;
  }
   
  public static double random_NORMAL(double u, double s) { //for normal distribution 
    double sig = s;
    double m = u;
     
    double top = sd_NORMAL(50);
    double bottom =  sig + m;
     
    double num = top * bottom;
     
    if (num < 0) { num = 0; }
     
    return num;
  }
   
  public static double sd_NORMAL(int n) { //for normal distribution 
     
    double total = 0;
    double number = n;
     
    for (int i = 0; i <= number; i++) { total = total + Math.random(); }
     
    double den = (Math.sqrt(100) / Math.sqrt(12));
    double num = (total - number * 0.5);
     
    total =  num / den;
    return total;
  }
   
   
   
   
  public void process(int requests, Calendar calendar, QueueClass queue) {
     
    if (eventType==0) {
       
      queue.add(this);
       
      if (!calendar.isFull_CPU()) {
         
        CPU_DISK_NETWORK event_cpu = new CPU_DISK_NETWORK(1, service_rate_CPU + time, service_rate, arrival_rate);
        calendar.insert(event_cpu);
         
        queue.remove();
         
        calendar.add_CPU(); 
      }
      CPU_DISK_NETWORK event_arrival = new CPU_DISK_NETWORK(0, random(arrival_rate) + time, service_rate, arrival_rate);
      calendar.insert(event_arrival);
       
    }
     
    if (eventType==1) {
       
      if (queue.getCounter() >= 1) {
         
        CPU_DISK_NETWORK birth = new CPU_DISK_NETWORK();
        birth = queue.getTop();  
         
        queue.remove();
        calendar.insert(new CPU_DISK_NETWORK(1, time + service_rate_CPU, service_rate, arrival_rate));
      }
       
      if (queue.getCounter() < 1) { calendar.remove_CPU(); }
       
      if (Math.random() <= 0.2) {
        CPU_DISK_NETWORK disk_event = new CPU_DISK_NETWORK(4, time, service_rate, arrival_rate);
         
        calendar.insert(disk_event);
      }
       
       
    }
     
    if (eventType==2) {
       
      queue.add(this);
 
      if (!calendar.isFull_CPU()) {
         
        calendar.insert(new CPU_DISK_NETWORK(1, service_rate_CPU + time, service_rate, arrival_rate));
        queue.remove();
         
        calendar.add_CPU();
      }     
    }
     
    if (eventType==3) {
 
      int num = 0;
      if (calendar.cpu_x) { num += 1; }
      if (calendar.cpu_y) { num += 1; }
       
      queue.q = queue.getCounter() + num + queue.q;
      queue.q_count++;
       
      CPU_DISK_NETWORK monitoring_event = new CPU_DISK_NETWORK(3, time + mon, service_rate, arrival_rate);
      calendar.insert(monitoring_event);
       
    }
     
     
    if (eventType==4) {
       
      queue.add(this);
 
      if (calendar.disk_check ) {
        CPU_DISK_NETWORK event_disk = new CPU_DISK_NETWORK(5, service_rate_DISK + time, service_rate, arrival_rate);
        calendar.insert(event_disk); 
         
        // remove from queue and add to calendar 
        queue.remove();
        calendar.disk_check  = false;
      }
    }
     
    if (eventType==5) {
       
      if (queue.getCounter() >= 1) {
         
        CPU_DISK_NETWORK birth = new CPU_DISK_NETWORK();
        birth = queue.getTop();
         
        queue.remove();
         
        CPU_DISK_NETWORK event_disk = new CPU_DISK_NETWORK(5, time + service_rate_DISK, service_rate, arrival_rate);
        calendar.insert(event_disk);
      }
      else { calendar.disk_check  = true;}
       
      if (Math.random() <= 0.3) { calendar.insert(new CPU_DISK_NETWORK(2, time, service_rate, arrival_rate)); }
       
      if (Math.random() > 0.3) { calendar.insert(new CPU_DISK_NETWORK(7, time, service_rate, arrival_rate)); }
    }
     
    if (eventType==6) {
 
      int temp = 0;
      if (!calendar.disk_check ) { temp = 1; }
       
      queue.q = queue.q + queue.getCounter() + temp;
      queue.q_count++;
       
      CPU_DISK_NETWORK event_disk = new CPU_DISK_NETWORK(6, time + mon, service_rate, arrival_rate);
      calendar.insert(event_disk);
    }
     
    if (eventType==7) {
       
      queue.add(this);
 
      if (calendar.network_check) {
         
        CPU_DISK_NETWORK event_network = new CPU_DISK_NETWORK(8, service_rate_NETWORK + time, service_rate, arrival_rate);
        calendar.insert(event_network);
         
        queue.remove();
         
        calendar.network_check = false;
      }
    }
     
    if (eventType==8) {
       
      if (queue.getCounter() >= 1) {
        CPU_DISK_NETWORK nextBirth = new CPU_DISK_NETWORK();
         
        nextBirth = queue.getTop();
         
        queue.remove();
         
        CPU_DISK_NETWORK event_network = new CPU_DISK_NETWORK(8, time + service_rate_NETWORK, service_rate, arrival_rate);
         
        calendar.insert(event_network);
      }
       
      else {calendar.network_check = true; }
       
      if (Math.random() <= 0.4) { calendar.insert(new CPU_DISK_NETWORK(4, time, service_rate, arrival_rate)); }
       
      if (Math.random() > 0.4) { calendar.insert(new CPU_DISK_NETWORK(2, time, service_rate, arrival_rate));}  
    }
     
     
    if (eventType==9) {
 
      int temp = 0;
      if (!calendar.network_check) { temp = 1; }
       
      queue.q = queue.q + queue.getCounter() + temp;
      queue.q_count++;
       
      CPU_DISK_NETWORK event_network = new CPU_DISK_NETWORK(9, time + mon, service_rate, arrival_rate);
       
      calendar.insert(event_network);
    }
  } 
   
  
	
}