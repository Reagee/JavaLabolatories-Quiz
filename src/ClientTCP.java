import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.*;
import java.util.Scanner;

public class ClientTCP {
	private int myID;
	
	public ClientTCP() {
		this.myID = (int) (Math.random() * 1000000);
	}
	
	public int getID() {
		return myID;
	}
	
	public void start_connection() {
			
			int port = 3506;
			
			try {
				Socket socket = new Socket(InetAddress.getByName("127.0.0.1"), port);
				/*int in;
				while ((in = System.in.read()) >= 0)
					socket.getOutputStream().write((char) in);*/
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
				Scanner sc = new Scanner(System.in);
				String str;
				socket.setTcpNoDelay(true);
				
				
				for(int i = 0; i < 7; i++)
				{
					out.println(i+1);
					out.flush();
					System.out.println(in.readLine());
					System.out.println(in.readLine());
					System.out.println(in.readLine());
					System.out.println(in.readLine());
					System.out.println(in.readLine());
					
					str = sc.nextLine();
					out.println(str);
					out.flush();
					
				}
				str = in.readLine();
				System.out.println(str);
				
				sc.close();
				socket.close();
			} catch (Exception e) {
				System.err.println(e);
			}
		
	}
}