package hw5.han;
// Szu Han Chang
// halo@bu.edu
// CS350 - Bestavros
// Simulator

public class Schedule {
    
    private Event head = new Event();
    private int counter = 0;

    private double reqCount = 0;
    private double totalRequests = 0;

    // Denotes whether the CPUs are in use.
    public boolean cpu1 = false;
    public boolean cpu2 = false;

    public boolean isDiskEmpty = true;
    public boolean isNetworkEmpty = true;
    
    public Schedule(Event event) {
        head = event;
    }
    
    public void insert(Event event) {
 // Nothing in schedule.
        if (head == null) {
            head = event;
        }
        
 // Event to insert is before the head pointer.
 // Insert at beginning of schedule.
        else if (head.getTime() >= event.getTime()) {
            Event temp = new Event();
            temp = head;
            event.setNextEvent(temp);
            head = event;
        }
        
 // Event to insert is somewhere after head pointer.
        else {
            Event previous = new Event();
            previous = findPrevious(event.getTime());
            event.setNextEvent(previous.getNextEvent());
            previous.setNextEvent(event);
        }
    }
    
    private Event findPrevious(double time) {
        Event pointer = new Event();
        pointer = head;
        while (pointer.getNextEvent() != null) {
            if (pointer.getNextEvent().getTime() >= time) {
                return pointer;
            }
            pointer = pointer.getNextEvent();
        }
        
        return pointer;
    }
    
    public Event getNext() {
        Event next = new Event();
        next = head;
        head = head.getNextEvent();
        return next;
    }
    
    public Event getHead() {
        return head;
    }
    
    public int getCount() {
        return counter;
    }
    
    public void setCount(int count) {
        counter = count;
    }

    public double getRequestCount() {
 return reqCount;
    }

    public void setRequestCount(double in) {
 reqCount = in;
    }



    public boolean CPUoccupied() {
 if (!cpu1 || !cpu2)
     return false;
 else
     return true;
    }

    public void CPUinsert() {
 if (!cpu1)
     cpu1 = true;
 else if (!cpu2)
     cpu2 = true;
    }

    public void CPUevacuate() {
 if (cpu1)
     cpu1 = false;
 else if (cpu2)
     cpu2 = false;
    }
}