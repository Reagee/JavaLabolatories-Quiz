package quiz;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javafx.stage.Stage;

public class ServerTCPThread extends Thread {
	
	Socket mySocket;
	Stage primaryStage = null;
    
	public ServerTCPThread(Socket socket) {
		super(); // konstruktor klasy Thread
		mySocket = socket;
	}
	
	

	public void run() // program w¹tku
	{
		checkDriver("com.mysql.jdbc.Driver");
		
		Connection con = getConnection("jdbc:mysql://", "localhost", 3306, "root", "");
		Statement st = createStatement(con);
		Statement st2 = createStatement(con);
		Statement st3 = createStatement(con);
		
		getDatabase(st);
		
		String sql = "select * from questions";
		String sql2 = "select count(*) as rows from questions";
		
		ResultSet result = executeQuery(st,sql);
		ResultSet number = executeQuery(st2,sql2);
		int row = 0;
		int score = 0;
		int id;
		String ans;
		try {
			PrintWriter out = new PrintWriter(new OutputStreamWriter(mySocket.getOutputStream()));
			BufferedReader in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
			id = Integer.parseInt(in.readLine());
			
			String sql3 = "INSERT INTO student_answers (student_id) VALUES ("+id+")";
			executeUpdate(st3,sql3);
			String sql4 = "INSERT INTO results (student_id) VALUES ("+id+")";
			executeUpdate(st3,sql4);
				
			number.next();
			row = number.getInt("rows");
			out.println(row);
			out.flush();
			int i = 0;
			while(i  < row){
				
				result.next();
				
				out.println(result.getInt("id"));
				out.flush();
				out.println(result.getString("question_content"));
				out.flush();
				out.println(result.getString("answer_a"));
				out.flush();
				out.println(result.getString("answer_b"));
				out.flush();
				out.println(result.getString("answer_c"));
				out.flush();
				out.println(result.getString("answer_d"));	
				out.flush();
				
				ans = in.readLine();
				
				if(ans.equals(result.getString("correct_answer")))
				{
					score++;
				}
				
				setAnswer(i,id,st3,ans);
			
				out.println(score);
				out.flush();
				i++;
			}
			String sql5 = "UPDATE results SET score="+score+" WHERE student_id="+id+"";
			executeUpdate(st3,sql5);
			
			closeConnection(con,st3);
			closeConnection(con,st);
			closeConnection(con,st2);
			
		} catch (SQLException | IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			mySocket.close();
		} catch (Exception e) {
			System.err.println(e);
		}
	}
	
	public void setAnswer(int i,int id, Statement st,String ans) {
		String answer = null;
		
		if(ans.equals("1000"))
		{
			answer = "answer_a";
		}
		else if(ans.equals("0100"))
		{
			answer = "answer_b";
		}
		else if(ans.equals("0010"))
		{
			answer = "answer_c";
		}
		else if(ans.equals("0001"))
		{
			answer = "answer_d";
		}
		else if(!ans.equals("1000") && !ans.equals("0100") && !ans.equals("0010") && !ans.equals("0001"))
		{
			String sql5 = "UPDATE student_answers SET answer_"+(i+1)+"='Bledne pobranie readLine()' WHERE student_id ="+id+"";
			executeUpdate(st,sql5);
		}
		if(ans.equals("1000") || ans.equals("0100") || ans.equals("0010") || ans.equals("0001"))
		{
			String sql5 = "UPDATE student_answers SET answer_"+(i+1)+"=(SELECT "+answer+" FROM questions WHERE id = "+(i+1)+") WHERE student_id ="+id+"";
			executeUpdate(st,sql5);
		}
	}
	
	public static boolean checkDriver(String driver) {
		// LADOWANIE STEROWNIKA
		//System.out.print("Sprawdzanie sterownika:");
		try {
			Class.forName(driver).newInstance();
			return true;
		} catch (Exception e) {
			System.out.println("Blad przy ladowaniu sterownika bazy!");
			return false;
		}
	}
	
	public static Connection getConnection(String kindOfDatabase, String adres, int port, String userName, String password) {

		Connection conn = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", userName);
		connectionProps.put("password", password);
		try {
			conn = DriverManager.getConnection(kindOfDatabase + adres + ":" + port + "/",
					connectionProps);
		} catch (SQLException e) {
			System.out.println("B³¹d po³¹czenia z baz¹ danych! " + e.getMessage() + ": " + e.getErrorCode());
			System.exit(2);
		}
		//System.out.println("Po³¹czenie z baz¹ danych: ... OK");
		return conn;
	}
	
	
	private static Statement createStatement(Connection connection) {
		try {
			return connection.createStatement();
		} catch (SQLException e) {
			System.out.println("B³¹d createStatement! " + e.getMessage() + ": " + e.getErrorCode());
			System.exit(3);
		}
		return null;
	}

	
	private static void closeConnection(Connection connection, Statement s) {
		System.out.print("\nZamykanie polaczenia z baz¹:");
		try {
			s.close();
			connection.close();
		} catch (SQLException e) {
			System.out
					.println("Bl¹d przy zamykaniu pol¹czenia z baz¹! " + e.getMessage() + ": " + e.getErrorCode());;
			System.exit(4);
		}
		System.out.print(" zamkniêcie OK");
	}

	
	private static ResultSet executeQuery(Statement s, String sql) {
		try {
			return s.executeQuery(sql);
		} catch (SQLException e) {
			System.out.println("Zapytanie nie wykonane! " + e.getMessage() + ": " + e.getErrorCode());
		}
		return null;
	}
	
	private static int executeUpdate(Statement s, String sql) {
		try {
			return s.executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println("Zapytanie nie wykonane! " + e.getMessage() + ": " + e.getErrorCode());
		}
		return -1;
	}
	
	
	public void getDatabase(Statement st) {
		if (executeUpdate(st,"CREATE DATABASE IF NOT EXISTS MG_JAVA_1105;") == 0)
			System.out.println("Baza stworzona");
		else
			System.out.println("Nie znaleziono bazy");
		
		// próba wybrania bazy
		if (executeUpdate(st, "USE MG_JAVA_1105;") == 0)
			System.out.println("Baza wybrana");
		else {
			System.out.println("Baza nie istnieje! Tworzymy bazê: MG_JAVA_1105");
			if (executeUpdate(st, "create Database MG_JAVA_1105 CHARACTER SET utf8 COLLATE utf8_polish_ci;") == 1)
				System.out.println("Baza utworzona");
			else
				System.out.println("Baza nieutworzona!");
			if (executeUpdate(st, "USE MG_JAVA_1105;") == 0)
				System.out.println("Baza wybrana");
			else
				System.out.println("Baza niewybrana!");
		}
		
		if (executeUpdate(st,
				"CREATE TABLE `questions` (" + 
				"  `id` int(11) NOT NULL," + 
				"  `question_content` varchar(150) COLLATE utf8_polish_ci NOT NULL," + 
				"  `answer_a` varchar(50) COLLATE utf8_polish_ci NOT NULL," + 
				"  `answer_b` varchar(50) COLLATE utf8_polish_ci NOT NULL," + 
				"  `answer_c` varchar(50) COLLATE utf8_polish_ci NOT NULL," + 
				"  `answer_d` varchar(50) COLLATE utf8_polish_ci NOT NULL," + 
				"  `correct_answer` varchar(4) NOT NULL" + 
				") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;") == 0)
			System.out.println("Tabela 'Pytania' utworzona");
		else
			System.out.println("Tabela 'Pytania' nie utworzona!");
		
		if (executeUpdate(st,"ALTER TABLE questions ADD PRIMARY KEY (id);") == 0)
			System.out.println("Dodano klucz dla tabeli 'Pytania'");
		else
			System.out.println("B³¹d w dodawaniu klucza dla tabeli 'Pytania'");	
		
		if (executeUpdate(st,
				"CREATE TABLE `student_answers` (" + 
				"  `student_id` int(20) NOT NULL," + 
				"  `answer_1` varchar(50) COLLATE utf8_polish_ci DEFAULT 'Brak odpowiedzi'," + 
				"  `answer_2` varchar(50) COLLATE utf8_polish_ci DEFAULT 'Brak odpowiedzi'," + 
				"  `answer_3` varchar(50) COLLATE utf8_polish_ci DEFAULT 'Brak odpowiedzi'," + 
				"  `answer_4` varchar(50) COLLATE utf8_polish_ci DEFAULT 'Brak odpowiedzi'," + 
				"  `answer_5` varchar(50) COLLATE utf8_polish_ci DEFAULT 'Brak odpowiedzi'," + 
				"  `answer_6` varchar(50) COLLATE utf8_polish_ci DEFAULT 'Brak odpowiedzi'," + 
				"  `answer_7` varchar(50) COLLATE utf8_polish_ci DEFAULT 'Brak odpowiedzi'," + 
				"  `answer_8` varchar(50) COLLATE utf8_polish_ci DEFAULT 'Brak odpowiedzi'," + 
				"  `answer_9` varchar(50) COLLATE utf8_polish_ci DEFAULT 'Brak odpowiedzi'," + 
				"  `answer_10` varchar(50) COLLATE utf8_polish_ci DEFAULT 'Brak odpowiedzi'," + 
				"  `answer_11` varchar(50) COLLATE utf8_polish_ci DEFAULT 'Brak odpowiedzi'," + 
				"  `answer_12` varchar(50) COLLATE utf8_polish_ci DEFAULT 'Brak odpowiedzi'," + 
				"  `answer_13` varchar(50) COLLATE utf8_polish_ci DEFAULT 'Brak odpowiedzi'," + 
				"  `answer_14` varchar(50) COLLATE utf8_polish_ci DEFAULT 'Brak odpowiedzi'," + 
				"  `answer_15` varchar(50) COLLATE utf8_polish_ci DEFAULT 'Brak odpowiedzi')" +  
				"ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;") == 0)
			System.out.println("Tabela 'Odpowiedzi' utworzona");
		else
			System.out.println("Tabela 'Odpowiedzi' nie utworzona!");
		
		if (executeUpdate(st,"ALTER TABLE `student_answers` ADD PRIMARY KEY (`student_id`);") == 0)
			System.out.println("Dodano klucz dla tabeli 'Odpowiedzi'");
		else
			System.out.println("B³¹d w dodawaniu klucza dla tabeli 'Odpowiedzi'");
		
		if (executeUpdate(st,
				"CREATE TABLE `results` (`student_id` int(20) NOT NULL,`score` int(2) DEFAULT NULL) " + 
				"ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;") == 0)
			System.out.println("Tabela 'Wyniki' utworzona");
		else
			System.out.println("Tabela 'Wyniki' nie utworzona!");
		
		if (executeUpdate(st,"ALTER TABLE `results` ADD PRIMARY KEY (`student_id`);") == 0)
			System.out.println("Dodano klucz dla tabeli 'Wyniki'");
		else
			System.out.println("B³¹d w dodawaniu klucza dla tabeli 'Wyniki'");
		
		String insertQ = "INSERT INTO `questions` (`id`, `question_content`, `answer_a`, `answer_b`, `answer_c`, `answer_d`, `correct_answer`) VALUES" + 
				"(1, 'Co oznacza skrót USB?', 'Universal Satelite Bus', 'Universal System Bus', 'Universal Serial Bus', 'Universal Softvare Bus', '0010')," + 
				"(2, 'W jakiej firmie Steve Jobs podpatrzyl graficzny interfejs uzytkownika z myszka?', 'Xerox', 'Microsoft', 'Amiga', 'IBM', '1000')," + 
				"(3, 'Która z tych architektur NIE ma zastosowania w budowie komputerów?', 'Architektura Oksfordzka', 'Architektura Princeton', 'Architektura Johna Von Neumana', 'Architektura Harwardzka', '1000')," + 
				"(4, 'Skad pochodzi znak pisarski @ (malpa)', 'z jezyka arabskiego', 'z jezyka greckiego', 'z jezyka lacinskiego', 'stworzono ten symbol na potrzeby e-mailii', '0010')," + 
				"(5, 'Pierwszy pakiet programów analizujacych bezpieczenstwo systemów informatycznych COPS napisal amerykanski programista i byly hacker:', 'Richard Matthew Stallman', 'Agent Steal', 'Eric Steven Raymond', 'Dan Farmer', '0001')," + 
				"(6, 'Kto wymyslil komputer Macintosh ?', 'Bill Gates', 'Steve Jobs', 'Jef Raskin', 'Stephan Gary Wozniak', '0010')," + 
				"(7, 'Rodzajem drukarki uzywanej zazwyczaj w kasach i drukarkach fiskalnych jest:', 'Drukarka produkcyjna', 'Drukarka termotransferowa', 'Drukarka termiczna', 'Drukarka wierszowa', '0010')," + 
				"(8, 'W którym roku zostala wyslana pierwsza wiadomosc e-mail?', '1970', '1973', '1975', '1971', '0001')," + 
				"(9, 'Ekspertem bezpieczenstwa komputerowego w finansowanym przez rzad centrum komputerowym w San Diego oraz konsultantem FBI jest byly haker:', 'Gary McKinnon', 'Kevin David Mitnick', 'Anthony Chris Zboralski', 'Tsutomu Shimomura', '0001')," + 
				"(10, 'Popularna wyszukiwarka Google.com nazywala sie wczesniej:', 'AdWords', 'PageRank', 'BackRub', 'Alphabet', '0010')," + 
				"(11, 'Co to jest \"zip\" ?', 'Rodzaj kodeka muzycznego', 'Rozszerzenie obrazka', 'Format kompresji plików', 'Rozszerzenie dzwieku', '0010')," + 
				"(12, 'Jak nazywa sie podstawowy edytor graficzny Windows?', 'Word', 'Paint', 'Photoshop', 'Gimp!', '0100')," + 
				"(13, 'Do czego sluzy hiperlacze?', 'Do otwierania stron www', 'Do czytania dokumentów', 'Do przyspieszania internetu', 'Do wysylania filmów', '1000')," + 
				"(14, 'Na którym serwerze mozna odtworzyc wideo strumieniowane?', 'o2', 'YouTube', 'Onet', 'Wp', '0100')," + 
				"(15, 'Najpopularniejszy format plików muzycznych to:', 'bmp', 'doc', 'mp3', 'avi', '0010');";
		executeUpdate(st, insertQ);
	}

}
