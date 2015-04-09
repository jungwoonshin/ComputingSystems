package hw5.han;
// Szu Han Chang
// halo@bu.edu
// CS350 - Bestavros
// Simulator

public class Queue {
    
    private Event head = new Event();
    private int queueCounter;
    private int K;
    private int requests = 0;
    public int q = 0;
    public int qCount = 0;

    public Queue(int size) {
        head = null;
        queueCounter = 0;
 K = size;
    }
    
    public void enqueue(Event event) {
 requests++;
 if (queueCounter < K) { // Queue is not yet full.
     queueCounter++;
            if (head == null) {
                head = event;
       }
            else {
                Event pointer = new Event();
                pointer = head;
                while (pointer.getNextInQueue() != null) {
                    pointer = pointer.getNextInQueue();
                }
            
                pointer.setNextInQueue(event);
            }
 }

    }
    
    public void dequeue() {
 //requests--;
 if (head == null) {
          queueCounter = queueCounter;
 }
 else {
     queueCounter--;
     head = head.getNextInQueue();
 }
    }
    
    public Event getHead() {
        return head;
    }
    
    public int getCounter() {
        return queueCounter;
    }

    public int getK() {
 return K;
    }

    public int getRequests() {
 return requests;
    }

}