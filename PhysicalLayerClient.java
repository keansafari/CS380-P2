import java.io.*;
import java.net.*;
import java.util.*;
import java.util.zip.CRC32;
import java.nio.*;

public class PhysicalLayerClient {

	public static void main(String[] args) {

		try(Socket socket = new SOcket("codebank.xyz", 38002)) {
			System.out.println("Connected to Server.");

			//Read from Input
			InputStream is = socket.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			//Write to stream
			OutputStream out = socket.getOutputStream();
			PrintWriter pw = new PrintWriter(out);

			



		}


	}
}