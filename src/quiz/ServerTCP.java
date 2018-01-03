package quiz;
import java.io.IOException;
import java.net.*;

public class ServerTCP extends Thread{
	
	private ServerSocket serverSocket;
	private int port;
	private int portFlag;
	
	public int getPortFlag() {
		return portFlag;
	}

	public ServerTCP(int port) {
		this.port = port;
	}

	public ServerSocket getServerSocket() {
		return serverSocket;
	}
	
	public void run() {
		try {
			serverSocket = new ServerSocket(port);
			while (true) {
				portFlag = 0;
				
				Socket socket = serverSocket.accept();
				(new ServerTCPThread(socket)).start();
			}
		} catch (Exception e) {
			portFlag = 1;
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