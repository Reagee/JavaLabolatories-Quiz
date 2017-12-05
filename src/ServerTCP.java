import java.io.IOException;
import java.net.*;

public class ServerTCP {
	public static void main(String args[]) {
	
			int port = 3506;
						
			ServerSocket serverSocket = null;
			try {
				// tworzymy socket
				serverSocket = new ServerSocket(port);
				while (true) {
					// czekamy na zg³oszenie klienta ...
					Socket socket = serverSocket.accept();
					// tworzymy w¹tek dla danego po³¹czenia i uruchamiamy go
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