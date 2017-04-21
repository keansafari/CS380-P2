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
			


			//Recieve the Bytes -- stores into a list
			LinkedList<Integer> initialSignal = recievedByteSignal(socket);

			//Creates binary list of the signals parallel to the signal recieved
			//from the server
			LinkedList<Integer> fiveBitSignal = encodeNRZI(initialSignal, socket);

			//Creates a new list with the decoded five bit using the conversion table given
			LinkedList<Integer> fiveBitDecode = messageDecode(fiveBitSignal, convmap);
			
			//~mergeBits
			LinkedList<Integer> bitCombine = combineBits(fiveBitDecode);

			//Write to stream -> sends message back
			relayMessage(bitCombine, socket);

			socket.close();
			System.out.println("Disconnected From Server");

			
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

	//Given 4b/5b conversion table
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

	public static LinkedList<Integer> messageDecode(LinkedList<Integer> fiveSig, HashMap convmap) {
		LinkedList<Integer> message = new LinkedList<>();
		for (int i = 0; i < fiveSig.size()-4; i+=5) {
			LinkedList<Integer> list = new LinkedList<>();
			
			//Reads 5 bits at a time and adds it to a new temporary list
			list.add(fiveSig.get(i));
			list.add(fiveSig.get(i+1));
			list.add(fiveSig.get(i+2));
			list.add(fiveSig.get(i+3));
			list.add(fiveSig.get(i+4));

			//Sends 4B message to recieve appropriate 5B conversion
			
			//message.add(convmap.get5B(list));
			int old = get5B(list, convmap);
			

			int fiveBit = convmap.get(old);
			message.add(fiveBit);
			


		}
		return message;
	} 

	public static int get5B(LinkedList<Integer> oldMessage, HashMap table) {
	
		String key = stringConversion(oldMessage);
		return Integer.parseInt(table.get(key),2);

	}

	public static LinkedList<Integer> recievedByteSignal(Socket socket) {
		try {
			InputStream is = socket.getInputStream();
			LinkedList<Integer> bytes = new LinkedList<>();
			int recievedInput;
			for (int i = 0; i < 320; i++) {
				recievedInput = is.read();
				bytes.add(recievedInput);
			}
			return bytes;
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		return null;
	}

	public static LinkedList<Integer> encodeNRZI(LinkedList<Integer> initialSignal, Socket socket) {
		LinkedList<Integer> decodedMessage = new LinkedList<>();
		int temp = 0;
		
		//For loop to create binary signals for the intput recieved
		for (int i = 0; i < initialSignal.size(); i++) {
			if (initialSignal.get(i) == temp)
				decodedMessage.add(0);
			else
				decodedMessage.add(1);
		}
	
		return decodedMessage;
	}

	public static LinkedList<Integer> combineBits(LinkedList<Integer> oldBitMessage) {
		LinkedList<Integer> newMessage = new LinkedList<>();
		for (int i = 0; i < oldBitMessage.size(); i++) {
			int upperBit = oldBitMessage.get(i);
			int lowerBit = oldBitMessage.get(i+1);
			upperBit = upperBit * 16 + lowerBit;
			newMessage.add(upperBit);
		}
		return newMessage;
	}

	public static void relayMessage(LinkedList<Integer> bits, Socket socket) {
		try {
			int size = bits.size();
			OutputStream os = socket.getOutputStream();
			byte[] toServer = new byte[message.size()];
			System.out.print("Recieved Bytes:  ");

			for (int i = 0; i < size; i++) {
				int bit = message.get(i);
				System.out.print(Integer.toHexString(bit).toUpperCase());
				toServer[i] = (byte) bit;
			}
			System.out.println("\n");
			os.write(toServer);
		} catch (Exception e) {
			e.printStackTrace(); 
		}
	}


	private static String stringConversion(LinkedList<Integer> curSig) {
		String convert = "";
		for (Integer i : curSig) {
			convert += i;
			convert += " ";
		}
		return convert;
	}




}