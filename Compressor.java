package nikolapeja6.Telekom;

import java.io.*;
import java.util.*;

public class Compressor {

	public static void main(String[] args) {
		 burrowsWheeler("BANANA", false);
		 moveToFront("NNBAAA", false);
	}

	
	public static void compress(File sourceFile, boolean bw, boolean mtf, boolean ac){
		
		// Checking for error
		if(sourceFile == null || !sourceFile.exists() || !sourceFile.canRead())
			return;
		
		// Opening the file and reading the data
		BufferedReader in;
		String data;
		try {
			in = new BufferedReader(new FileReader(sourceFile));
			data = in.readLine();
		} catch (Exception e) {return;	}
		
		// Using the Burrows-Wheeler transformation
		data  = burrowsWheeler(data, bw);
		
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
			try {
				out = new PrintWriter(new File("output.txt"));
				out.print("");
			} catch (FileNotFoundException e) {	}
		
		
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
		
		if(out != null)
			out.close();
		
		return sb.toString();
		
		
	}
	
	private static String moveToFront(String data, boolean flag){
		
		PrintWriter out = null;
		
		if(flag)
			try {
				out = new PrintWriter(new File("output.txt"));
			} catch (FileNotFoundException e) {	}
		

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
		
		if(out != null)
			out.close();
		
		return sb.toString();
		
	}
	
	


}
