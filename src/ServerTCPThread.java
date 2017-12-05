import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ServerTCPThread extends Thread {
	Socket mySocket;

	public ServerTCPThread(Socket socket) {
		super(); // konstruktor klasy Thread
		mySocket = socket;
	}

	public void run() // program w¹tku
	{
		try {
			String filename = "bazaPytan.txt";
			List questions = null;
			FileInputStream fis = null;
			ObjectInputStream input = null;
			try {
				fis = new FileInputStream(filename);
				input = new ObjectInputStream(fis);
				questions = (ArrayList) input.readObject();
				input.close();
			}
			catch (IOException ex) {
				ex.printStackTrace();
			}
			catch (ClassNotFoundException ex) {
				ex.printStackTrace();
			}
			
			BufferedReader in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
			PrintWriter out = new PrintWriter(new OutputStreamWriter(mySocket.getOutputStream()));
			String str;
			int i, score = 0, ans;
			for(i = 0; i < 7; i++)
			{
				str = in.readLine();
				Question q = (Question)questions.get(i);
				out.println(q.getContent());
				out.flush();
				out.println(q.getAnswerA());
				out.flush();
				out.println(q.getAnswerB());
				out.flush();
				out.println(q.getAnswerC());
				out.flush();
				out.println(q.getAnswerD());
				out.flush();
				
				str = in.readLine();
				ans = Integer.parseInt(str);
				
				if(ans == q.getCorrect())
					score++;
			}
			out.println("Twój wynik " + score);
			out.flush();
			mySocket.close();
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}
