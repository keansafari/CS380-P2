import java.io.*;
import java.net.*;
import java.util.*;
import java.nio.*;
import java.lang.String;
import java.lang.*;
import java.text.*;

public class PhysicalLayerClient {


	public static void main(String[] args) {

		try(Socket socket = new Socket("codebank.xyz", 38002)) {
			System.out.println("\nConnected to Server.");

			//Create Conversion map since is connection successful
			HashMap convmap = create4b5bconversionmap();
			
			//Get Baseline - read 64 bits and average them
			double averageBaseline = getBaseline(socket);
			System.out.println("Baseline established from preamble: " + averageBaseline);
			


			//Get and Print 32 bytes
			//byte[] nzri = getNZRI();
			

			//Write to stream
			OutputStream out = socket.getOutputStream();
			PrintWriter pw = new PrintWriter(out);
			
		} catch (Exception e) { e.printStackTrace(System.out); }


	}


	//Gets average baseline from 64 bits
	public static double getBaseline(Socket socket) {
		try {
			InputStream is = socket.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			int[] baseline = new int[64];
			double averageBaseline = 0;
			for(int i = 0; i < 64; i++) {
				baseline[i] = is.read();
				averageBaseline += baseline[i];
			}
			averageBaseline = averageBaseline / 64;
			DecimalFormat decimal = new DecimalFormat("#.##");
			averageBaseline = Double.valueOf(decimal.format(averageBaseline));
			return averageBaseline;


		} catch (Exception e) { e.printStackTrace(System.out); }
		return 0;
	}

	//Work on this
	/*
	public byte[] getNZRI() {
		byte[] nzri = new byte[32];
		for (int i = 0; i < 32; i++) {
			
		}
		
	}*/


	private static HashMap create4b5bconversionmap() {
		HashMap<String, String> map = new HashMap<String, String>() {{
			put("11110", "0000"); 
			put("10010", "1000");
	        put("01001", "0001"); 
	        put("10011", "1001");
	        put("10100", "0010"); 
	        put("10110", "1010");
	        put("10101", "0011"); 
	        put("10111", "1011");
	        put("01010", "0100"); 
	        put("11010", "1100");
	        put("01011", "0101"); 
	        put("11011", "1101");
	        put("01110", "0110"); 
	        put("11100", "1110");
	        put("01111", "0111"); 
	        put("11101", "1111");
	    }};
	    return map;
	}
}