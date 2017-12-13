package quiz;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.*;
import java.util.Scanner;

public class ClientTCP extends Thread{
	
	private int myID;
	private BufferedReader in;
	private PrintWriter out;
	private Socket socket;
	
	public ClientTCP(){
		this.myID = (int) (Math.random() * 100000);
	}
	
	public int getID() {
		return myID;
	}
	
	public BufferedReader getIn() {
		return in;
	}

	public PrintWriter getOut() {
		return out;
	}

	
	public Socket getSocket() {
		return socket;
	}

	public void start_connection() {
			
			int port = 3506;
			
			try {
				socket = new Socket(InetAddress.getByName("127.0.0.1"), port);
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
				socket.setTcpNoDelay(true);
				out.println(myID);
				out.flush();
				
			
			} catch (Exception e) {
				System.err.println(e);
			}
		
	}
	
}