package hw05;

public class Problem1b {
	
	public static void main(String[] args) {
		double k = 32;
		
//		for(int i=500;i<1000;i++){
			int out_lambda= 880;
			System.out.println("K: " + k);
			System.out.println("Arrival Rate: "  + out_lambda);
			
			System.out.print("out_lambda: " + out_lambda +" ");
			double a = getLambdaPrimeNetwork(0.9*out_lambda,k, out_lambda );
			double b =getLambdaCPU(a, out_lambda);
			double c =getLambdaDisk(b);
			
			double q_network = getQnetwork(a);
			double q_cpu = getQcpu(b);
			double q_disk = getQdisk(c);
			double q_all = q_network+q_cpu+q_disk;
		
		
		System.out.println("lambda_net: " + a + ", lambda_cpu: " + b+ ", lambda_disk: " +c);
		System.out.println("q_network: " + q_network + ", q_cpu: " + q_cpu +", q_disk: " + q_disk);
		System.out.println("q_all: " + q_all);
		
		double t_q = q_all/out_lambda;
		System.out.println("T_q: " + t_q);
//		}
	}

	
	
	private static double getQdisk(double c) {
		double ro_disk =c*0.1;
		return ro_disk/(1-ro_disk);
	}

	private static double getQcpu(double b) {
		double ro_cpu = b*0.02;
		ro_cpu=ro_cpu/2;
		// TODO Auto-generated method stub
		return ro_cpu/(1-ro_cpu);
	}

	private static double getQnetwork(double a) {
		double ro_network = a* 0.025;
		return ro_network/(1-ro_network);
	}

	private static double getLambdaCPU(double lambdaPrimeNetwork, double incoming_lambda) {
		double lambda_cpu=0.0;
		lambda_cpu = (lambdaPrimeNetwork +incoming_lambda)* 100/95;
		return lambda_cpu;
	}
	private static double getLambdaDisk(double lambda_cpu){
		return 0.1*lambda_cpu;
	}

	private static double getLambdaPrimeNetwork(double lambda_network, double k) {
		double lambda_prime_network;
		double ro_network = lambda_network*0.025;
		double rejection_rate = (1- ((1-ro_network)* Math.pow(ro_network,k))/(1-Math.pow(ro_network, k+1.0)));
		System.out.println("\nrejection rate: " +rejection_rate );
		lambda_prime_network = lambda_network * (1- ((1-ro_network)* Math.pow(ro_network,k))/(1-Math.pow(ro_network, k+1.0)));
//		System.out.println("lambda_prime_network: "+ lambda_prime_network);
		return lambda_prime_network;
	}
	private static double getLambdaPrimeNetwork(double lambda_network, double k, int i) {
		double lambda_prime_network;
		double ro_network = lambda_network*0.025;
		double rejection_rate = (1- ((1-ro_network)* Math.pow(ro_network,k))/(1-Math.pow(ro_network, k+1.0)));
		System.out.println("\nrejection rate: " +rejection_rate );

//		if(Math.abs(0.05 -rejection_rate)<0.0001){
//			System.out.println("i: " + i + ", rejection rate: " +rejection_rate );
//		}
		lambda_prime_network = lambda_network * (1- ((1-ro_network)* Math.pow(ro_network,k))/(1-Math.pow(ro_network, k+1.0)));
//		System.out.println("lambda_prime_network: "+ lambda_prime_network);
		return lambda_prime_network;
	}
	
	

}
