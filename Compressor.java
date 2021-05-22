package nikolapeja6.Telekom;

import java.io.*;
import java.util.*;

import javafx.util.Pair;

public class Compressor {

	// Bits used to transfer a single letter
	private static int BITS_PER_CHAR = 5;
	
	public static void main(String[] args) {
		compress(new File("input.txt"), true, true, true);
	}

	
	private static PrintWriter out = null;
	
	public static double compress(File sourceFile, boolean bw, boolean mtf, boolean ac){
		
		// Checking for error
		if(sourceFile == null || !sourceFile.exists() || !sourceFile.canRead())
			return -1;
		
		// Opening the file and reading the data
		BufferedReader in;
		String data;
		try {
			in = new BufferedReader(new FileReader(sourceFile));
			data = in.readLine();
		} catch (Exception e) {return -1;	}
		
		// check if data has only English letters
		for(int i=0; i<data.length(); i++)
			if(!Character.isLetter(data.charAt(i)))
				return -1;
		
		long n =  data.length();
		
		try {
			out = new PrintWriter(new File("output.txt"));
			out.print("");
		} catch (FileNotFoundException e) {	}
		
		
		// Using the Burrows-Wheeler transformation
		data  = burrowsWheeler(data, bw);
		data = moveToFront(data, mtf);
		int L = arithmeticCoder(data, ac);
		
		double ret = Math.ceil(Math.log(n*BITS_PER_CHAR)/Math.log(2))/L;
		
		print("Compression ratio is " + ret, out);
		
		try {
			in.close();
		} catch (IOException e) {}
		out.close();
		
		return ret;
		
	}
	
	
	
	// function fro printing to console (for debugging) and to file (if user selected output)
	private static void print(String s, PrintWriter out){
		System.out.print(s);
		if(out != null)
			out.append(s);
	}
	
	
	// Burrows-Wheeler transformation
	private static String burrowsWheeler(String data, boolean flag){
		
		PrintWriter out = null;
		
		if(flag)
			out = Compressor.out;
		
		print("=============================================================\n", out);
		print("Burrows-Wheeler\n\n", out);
		
		// List for containing the rotations of the original string
		LinkedList<String> list = new LinkedList<String>();
		
		// Converting all characters to upper case
		data = data.toUpperCase();
		
		// Saving the original and doing a rotation
		String original = data;
		data = data.substring(1, data.length()) + data.substring(0,1);
		list.add(original);
		
		print("Original = " + original+"\n\n", out);
		
		// generating the rotations and storing them in the list
		while(!original.equals(data)){
			list.add(data);
			data = data.substring(1, data.length()) + data.substring(0,1);
		}
		
		print("The rotations:\n", out);
		
		for(String s:list){
			print(s+"\n", out);
		}
		
		
		Collections.sort(list);
		
		print("\nThe sorted strings:\n", out);
		
		// it will contain the last characters of the strings in the list
		StringBuilder sb = new StringBuilder();
		
		// getting the last characters from the strings in the list
		for(String s:list){
			print(s+"\n", out);
			sb.append(s.charAt(s.length()-1));
		}
		
		
		print("\nThe result string is: "+sb.toString()+"\n\n", out);
		
		print("=============================================================\n\n", out);
		
		
		return sb.toString();
		
		
	}
	
	private static String moveToFront(String data, boolean flag){
		
		PrintWriter out = null;
		
		if(flag)
			out = Compressor.out;
		

		print("=============================================================\n", out);
		print("Move to Front\n\n", out);
		
		print("String to sequence is: "+data+"\n\n", out);
		
		// List that will be used for the Move to Front algorithm
		ArrayList<Character> list = new ArrayList<Character>();
		
		print("Inital state of the list:\n", out);
		
		// generating the alphabet (initial state of the list)
		for(int i=0; i<26; i++){
			list.add((char)(i+'A'));
			print((char)(i+'A')+" ", out);	
		}
		
		print("\n\n", out);
		
		StringBuffer sb = new StringBuffer();
		
		for(int i=0; i<data.length(); i++){
			
			//gtting next char and its index
			char c = data.charAt(i);
			int n  = list.indexOf(c);
			
			print("Char: "+c + "\t index: "+(n+1)+"\nNew sequence:  ", out);
			
			// putting the char to the start of the list
			list.remove(n);
			list.add(0, c);
			
			// appending the new index
			sb.append((n+1)+" ");
			
			for(Character x:list)
				print(x+" ", out);
			print("\n\n", out);
		}
		
		print("\nThe result sequence is: "+sb.toString()+"\n\n", out);
		
		print("=============================================================\n\n", out);

		return sb.toString();
		
	}
	
	
	// Arithmetic coder
	private static int arithmeticCoder(String data, boolean flag){
		
		class Data{
			public String s = "";
			public double p = 0;
			public Data(String ss){s=ss;}
		};
	
		PrintWriter out = null;
		
		if(flag)
			out = Compressor.out;
		

		print("=============================================================\n", out);
		print("Arithmetic Coder\n\n", out);
		
		print("Message: "+data+"\n\n", out);
		
		// Getting the values from the data String
		String [] d = data.split(" ");
		
		Data P[] = new Data[26];
		int n = d.length;
		
		for(int i=0; i<P.length; i++)
			P[i] = new Data((i+1)+"");
		
		for(int i=0; i<d.length; i++)
			P[Integer.parseInt(d[i])-1].p++;
		
		for(int i=0; i<P.length; i++)
			P[i].p/=n;
		
		Arrays.sort(P,  (a, b) -> a.p<b.p?1:a.p==b.p?0:-1);
		
		for(Data dd:P)
			if(dd.p != 0){
				print("P("+dd.s+") = "+String.format("%.2f", dd.p)+"; ", out);
			}
		
	
		print("\n\n", out);
		
		
		double lower=0, upper = 1;
		for(String s:d){
			double delta = upper-lower;
			double sum = 0;
			int i;
			for(i=0; i<P.length; i++){
				if(P[i].s.equals(s))
					break;
				sum+=P[i].p;
			}
			
			print(s+":\n", out);
			print("I = ("+String.format("%.10f", lower)+", "+String.format("%.10f", upper)+")\n\n", out);
				
			lower += delta*sum;
			upper = lower+delta*P[i].p;
		}
		
		print("\nThe resulting interval is I = ("+String.format("%.15f", lower)+", "+String.format("%.15f", upper)+")\n", out);
		
		double val = (upper+lower)/2;
		print("The selected valuse is: "+val+"\n",out);
		
		// Number of bits for the message
		int k;
		if(upper-lower == 0)
			k=-1;
		else
			k = (int)((Math.log(1/(upper-lower))/Math.log(2))+1);
		print("The number of bits for the message is: "+k+"\n\n", out);
		
		print("The result message (in binary form) is: " + doubleToBinary(val, k)+ "\n\n", out);
		
		print("=============================================================\n\n", out);
		
		
		return k;
				
	}
	
	private static String doubleToBinary(double d, int k){
		
		StringBuilder sb = new StringBuilder("0.");
		
		for(int i=0; (k!=-1?i<k:true) && d!=0; i++){
			double r = d * 2;   
			if( r >= 1 ) {     
				sb.append("1");     
				d = r - 1;      
			}else{             
				sb.append("0");    
				d = r;         
			}
		}
		
		return sb.toString();
	}
	
	


}
