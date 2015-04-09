package hw05;


import java.lang.Math;

public class MainK {

	static double arrival_rate = 	40; // arrival rate 
	static double service_rate = 0; // service rate 
	static double reject_rate = 0.0;
	static double running_time = 100;
	static double time_global = 0.0;
	static double requests_time = 0.0;
	static double waiting_time = 0.0; 
	static double requests_waiting = 0.0;
	static double requests_waiting_TOTAL = 0.0;
	static int counter = 0; 
	static int count_rejects = 0; 
	static int count_requests = 0;
	static double requests_count = 0.0;
	static double utilization = 0.0;
	static int type = 0;
	static int size_queue = 0;

	static double getQdisk(double c) {
		double ro_disk =c*0.1;
		return ro_disk/(1-ro_disk);
	}
	static double getQcpu(double b) {
		double ro_cpu = b*0.02;
		ro_cpu=ro_cpu/2;
		return ro_cpu/(1-ro_cpu);
	}

	static double getQnetwork(double a) {
		double ro_network = a* 0.025;
		return ro_network/(1-ro_network);
	}

	static double getLambdaCPU(double lambdaPrimeNetwork, double incoming_lambda) {
		double lambda_cpu=0.0;
		lambda_cpu = (lambdaPrimeNetwork +incoming_lambda)* 100/95;
		return lambda_cpu;
	}
	static double getLambdaDisk(double lambda_cpu){
		return 0.1*lambda_cpu;
	}

	static double getLambdaPrimeNetwork(double lambda_network, double k) {
		double lambda_prime_network;
		double ro_network = lambda_network*0.025;
		double rejection_rate = (1- ((1-ro_network)* Math.pow(ro_network,k))/(1-Math.pow(ro_network, k+1.0)));
		System.out.println("\nrejection rate: " +rejection_rate );
		lambda_prime_network = lambda_network * (1- ((1-ro_network)* Math.pow(ro_network,k))/(1-Math.pow(ro_network, k+1.0)));
		//		System.out.println("lambda_prime_network: "+ lambda_prime_network);
		return lambda_prime_network;
	}
	static double getLambdaPrimeNetwork(double lambda_network, double k, int i) {
		double lambda_prime_network;
		double ro_network = lambda_network*0.025;
		double rejection_rate = (1- ((1-ro_network)* Math.pow(ro_network,k))/(1-Math.pow(ro_network, k+1.0)));
		System.out.println("rejection rate: " +rejection_rate );
		lambda_prime_network = lambda_network * (1- ((1-ro_network)* Math.pow(ro_network,k))/(1-Math.pow(ro_network, k+1.0)));
		return lambda_prime_network;
	}
	static double getRejectionRate(double ro_network, double k) {
		double rejection_rate = (1.0- ((1.0-ro_network)* Math.pow(ro_network,k))/(1-Math.pow(ro_network, k+1.0)));
		return rejection_rate;
	}
	public static void main(String[] args) {

		System.out.println("\nResults for HW5: Problem 1 Section (b) and (c)");
		CPU_DISK_NETWORK event = new CPU_DISK_NETWORK();
		SimulatorK sim = new SimulatorK(running_time, arrival_rate, service_rate);  
		event = sim.getNext();
		time_global = event.getTime();
		while (time_global < running_time) {
			type = event.getType();
			// ~~~~CPU Arrival, Death, Arrival (from Disk/Network), Monitor~~~~ 
			if (type==0) {type = 1;} 
			if (type==1) {type = 2;}
			if (type==2) {type = 3;}
			if (type==3) { event.process(sim.getRequests(), sim.getCalendar(), sim.QueueClass_CPU); }
			// ~~~~~~~~~~~~~~~~Disk Death, Monitor, Arrival~~~~~~~~~~~~~~~~
			if (type==4) {type = 5;}
			if (type==5) {type = 6;}
			if (type==6) { event.process(sim.getRequests(), sim.getCalendar(), sim.QueueClass_DISK); }
			// ~~~~~~~~~~~~Network Arrival, Death, Monitor~~~~~~~~~~~~~~~~
			if (type==7) {type = 8;}
			if (type==8) {type = 9;}
			if (type==9) { event.process(sim.getRequests(), sim.getCalendar(), sim.QueueClass_NETWORK);} 

			event = sim.getNext();
			time_global = event.getTime();
		}


		double k = 32;
		int out_lambda= 880;

		double a = getLambdaPrimeNetwork(0.9*out_lambda,k, out_lambda );
		double b =getLambdaCPU(a, out_lambda);
		double c =getLambdaDisk(b);
		double q_network = getQnetwork(a);
		double q_cpu = getQcpu(b);
		double q_disk = getQdisk(c);
		double q_all = q_network+q_cpu+q_disk;
		System.out.println("lambda_network: " + a*0.025);
		System.out.println("Running time (sec): " + running_time);
		System.out.println("K: " + k);
		System.out.println("Arrival Rate: "  + out_lambda);
		System.out.print("out_lambda: " + out_lambda +" ");
		System.out.println("lambda_net: " + a + ", lambda_cpu: " + b+ ", lambda_disk: " +c);
		System.out.println("q_network: " + q_network + ", q_cpu: " + q_cpu +", q_disk: " + q_disk);
		System.out.println("q_all: " + q_all);
		double t_q = q_all/out_lambda;
		System.out.println("T_q: " + t_q);
		//	}

	}     
}