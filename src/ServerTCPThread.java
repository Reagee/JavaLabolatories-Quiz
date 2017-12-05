import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
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
			FileWriter file = new FileWriter("bazaOdpowiedzi.txt", true);
			BufferedWriter wr = new BufferedWriter(file);
			FileWriter file2 = new FileWriter("wyniki.txt", true);
			BufferedWriter wr_res = new BufferedWriter(file2);
			String str;
			int i, score = 0, ans, id;
			id = Integer.parseInt(in.readLine());
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
				wr.write(id + " " + (i+1) + " " + ans + " ");
				
				if(ans == q.getCorrect())
					score++;
			}
			wr.newLine();
			wr.close();
			wr_res.write(id + " " + score + " ");
			wr_res.newLine();
			wr_res.close();
			mySocket.close();
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}
