import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;


public class SaveQuestions {
	public static void main(String[] args) {
		String filename = "bazaPytan.txt";
		
		Question q1 = new Question(1, "Jaki jest rozmiar zmiennej typu long?", "8 bit", "16 bit", "32 bit", "64 bit", 4);
		Question q2 = new Question(2, "Jaka jest domyœlna wartoœæ typu char?", "'\u0000'", "0", "null", "64 bit", 1);
		Question q3 = new Question(3, "Jaki blok jest wymagany po bloku try?", "finally", "do", "catch", "while", 3);
		Question q4 = new Question(4, "JWybierz odpowiedni typ dla wartoœci 5.5", "int", "double", "boolean", "String", 2);
		Question q5 = new Question(5, "Co nie jest s³owem kluczowym Javy?", "static", "try", "Integer", "new", 3);
		Question q6 = new Question(6, "Który zapis jest poprawny?", "public void main(String[] args", "public static void main(string[] args", "public void main()", "¿aden z powy¿szych", 4);
		Question q7 = new Question(7, "Wybierz odpowiedni typ dla pola isSwimmer", "double", "boolean", "string", "int", 2);
		
		
		
		
		List list = new ArrayList();
		list.add(q1);
		list.add(q2);
		list.add(q3);
		list.add(q4);
		list.add(q5);
		list.add(q6);
		list.add(q7);
		
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		
		try {
			fos = new FileOutputStream(filename);
			out = new ObjectOutputStream(fos);
			out.writeObject(list);
			out.close();
			System.out.println("Zapisano do pliku " + filename);
		}
		catch(IOException ex) {
			ex.printStackTrace();
		}

	}
}
