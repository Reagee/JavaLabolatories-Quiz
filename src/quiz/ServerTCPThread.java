package quiz;
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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
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
		
		// próba wybrania bazy
		executeUpdate(st, "USE lab_java;");
		String sql = "select * from questions";
		String sql2 = "select count(*) as rows from questions";
		ResultSet result = executeQuery(st,sql);
		ResultSet number = executeQuery(st2,sql2);
		int row = 0;
		int score = 0;
		int ans;
		try {
			PrintWriter out = new PrintWriter(new OutputStreamWriter(mySocket.getOutputStream()));
			BufferedReader in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
			number.next();
			row = number.getInt("rows");
			out.println(number.getInt("rows"));
			out.flush();
			int i = 0;
			while(i<row) {
				
				
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
				
				ans = Integer.parseInt(in.readLine());
				if(ans == result.getInt("correct_answer"))
				{
					score++;
				}
				i++;	
			}
			if(i==15) {
				out.println(score);
				out.flush();
			}
		} catch (SQLException | IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			mySocket.close();
		} catch (Exception e) {
			System.err.println(e);
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
	
	
	private static void printDataFromQuery(ResultSet r) {
		ResultSetMetaData rsmd;
		try {
			rsmd = r.getMetaData();
			int numcols = rsmd.getColumnCount(); // pobieranie liczby kolumn
			// wyswietlanie nazw kolumn:
			for (int i = 1; i <= numcols; i++) {
				System.out.print("\t" + rsmd.getColumnLabel(i) + "\t|");
			}
			System.out
					.print("\n____________________________________________________________________________\n");
			/**
			 * r.next() - przejœcie do kolejnego rekordu (wiersza) otrzymanych wyników
			 */
			// wyswietlanie kolejnych rekordow:
			while (r.next()) {
				for (int i = 1; i <= numcols; i++) {
					Object obj = r.getObject(i);
					if (obj != null)
						System.out.print("\t" + obj.toString() + "\t|");
					else
						System.out.print("\t");
				}
				System.out.println();
			}
		} catch (SQLException e) {
			System.out.println("Bl¹d odczytu z bazy! " + e.getMessage() + ": " + e.getErrorCode());
		}
	}
	/**
	 * Metoda pobiera dane na podstawie nazwy kolumny
	 */
	public static void sqlGetDataByName(ResultSet r) {
		System.out.println("Pobieranie danych z wykorzystaniem nazw kolumn");
		try {
			ResultSetMetaData rsmd = r.getMetaData();
			int numcols = rsmd.getColumnCount();
			// Tytul tabeli z etykietami kolumn zestawow wynikow
			for (int i = 1; i <= numcols; i++) {
				System.out.print(rsmd.getColumnLabel(i) + "\t|\t");
			}
			System.out
			.print("\n____________________________________________________________________________\n");
			while (r.next()) {
				int size = r.getMetaData().getColumnCount();
				for(int i = 1; i <= size; i++){
					switch(r.getMetaData().getColumnTypeName(i)){
					case "INT":
						System.out.print(r.getInt(r.getMetaData().getColumnName(i)) + "\t|\t");
						break;
					case "DATE":
						System.out.print(r.getDate(r.getMetaData().getColumnName(i)) + "\t|\t");
						break;
					case "VARCHAR":
						System.out.print(r.getString(r.getMetaData().getColumnName(i)) + "\t|\t");
						break;
					default:
						System.out.print(r.getMetaData().getColumnTypeName(i));
					}
				}
				System.out.println();
			}
		} catch (SQLException e) {
			System.out.println("Bl¹d odczytu z bazy! " + e.getMessage() + ": " + e.getErrorCode());
		}
	}

}
