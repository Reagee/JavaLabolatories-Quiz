package quiz;
import java.io.IOException;
import java.net.*;

public class ServerTCP extends Thread{
	
	private ServerSocket serverSocket;
	
	public ServerSocket getServerSocket() {
		return serverSocket;
	}
	
	
	public void run() {
		int port = 3506;
		try {
			// tworzymy socket
			sleep(1000);
			serverSocket = new ServerSocket(port);
			while (true) {
				// czekamy na zg�oszenie klienta ...
				Socket socket = serverSocket.accept();
				// tworzymy w�tek dla danego po��czenia i uruchamiamy go
				(new ServerTCPThread(socket)).start();
			}
		} catch (Exception e) {
			System.err.println(e);
		}finally{
			if(serverSocket != null)
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
}