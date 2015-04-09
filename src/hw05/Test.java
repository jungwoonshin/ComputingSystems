package hw05;

import java.util.Arrays;

public class Test {
	public static void main(String[] args) {
//		
		String a= "3";
//		System.out.println(shortenStrings(a));
		a= "aabccccccccaaa";
		shortenStrings02(a);
		a= "abaaaaaabbbbbbbbbba";
		System.out.println(shortenStrings02(a));
		a= "aabbcccdde";
		System.out.println(shortenStrings02(a));
		
		
		

	}
	
	private static String shortenStrings02(String a) {
		int length = a.length(); 
		int index=0;
		int count = 1;
		String target = "";
		target+=a.charAt(index);
		for(int i=0; i<length-1; i++){
			System.out.println("a.charAt(i): " +a.charAt(i));
			System.out.println("a.charAt(i+1): " + a.charAt(i+1));
			if( a.charAt(i)==a.charAt(i+1)){
				count++;
			}  if(a.charAt(i)!=(a.charAt(i+1)) || i==length-2){
				System.out.println("count: " +count);
				target +=(char)('0'+count);
				count=1;
				index+=2;
				target+=a.charAt(i+1);
//				if(i!=length-2)target+=a.charAt(i+1);
			}
			System.out.println("target: " + target + ", length: " + target.length());
		}
		
		System.out.println("target: " + target + ", length: " + target.length());
		
		return target;
	}
	

	private static char[] shortenStrings01(String a) {
		char[] ori = a.toCharArray();
		int length = ori.length; 
		int index=0;
		int count = 1;
		char[] target = new char[length];
		
		target[index] = ori[index];		
		for(int i=0; i<length-1; i++){
			if(ori[i]==ori[i+1]){
				count++;
			}  if(ori[i]!=ori[i+1] || i==length-2){
				target[index+1] =(char)('0'+count);
				count=1;
				index+=2;
				if( i!=length-2)target[index] = ori[i+1];
			}
			System.out.println("target: " + Arrays.toString(target) + ", length: " + target.length);
		}
		
		
		return target;
	}
	
	
}
