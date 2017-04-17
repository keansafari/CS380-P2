import java.io.*;
import java.net.*;
import java.util.*;
import java.util.zip.CRC32;
import java.nio.*;
import java.lang.String;
import java.lang.*;

public class PhysicalLayerClient {

	public static void main(String[] args) {

		try(Socket socket = new Socket("codebank.xyz", 38002)) {
			System.out.println("\nConnected to Server.");
			
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
}