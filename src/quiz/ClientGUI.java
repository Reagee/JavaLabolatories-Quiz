package quiz;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import javafx.event.EventHandler;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ClientGUI extends Application {
	
	  private Label question = new Label(); 
	  private Label result = new Label();
	  private Button btn1 = new Button();
	  private Button btn2 = new Button();
	  private Button btn3 = new Button();
	  private Button btn4 = new Button();
	  private int i;
	  private int number = 0;

	public int getI(BufferedReader in) throws NumberFormatException, IOException {
		i = Integer.parseInt(in.readLine());
		return i;
	}
	  
	public void nextQ(BufferedReader in, PrintWriter out) throws IOException {
		question.setText(in.readLine()+". " + in.readLine());
		btn1.setText(in.readLine());
		btn2.setText(in.readLine());
		btn3.setText(in.readLine());
		btn4.setText(in.readLine());
		result.setText("Twój wynik: " + in.readLine() + "/15");
	}
	
    public void start(Stage primaryStage) throws IOException {
    	ClientTCP client = new ClientTCP();
    	client.start_connection();
    	client.getOut().println(client.getId());
    	client.getOut().flush();
    	
    	i = getI(client.getIn());
    	nextQ(client.getIn(),client.getOut());
    	number++;
    	
        primaryStage.setTitle("Quiz");
         GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.TOP_LEFT);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10,10,10,10));
        
        GridPane resultPane = new GridPane();
        resultPane.setAlignment(Pos.TOP_LEFT);
        resultPane.setHgap(10);
        resultPane.setVgap(10);
        resultPane.setPadding(new Insets(10,10,10,10));
        resultPane.add(result, 3, 3);
        result.setFont(Font.font("Arial",30));
          
        btn1.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent event) {
        		if(number<i)
        		{
        			client.getOut().println(Integer.toString(1));
            		client.getOut().flush();
        			try {
        				nextQ(client.getIn(),client.getOut());
        			} catch (IOException e) {
        				e.printStackTrace();
        			}
        			number++;
        		}
        		else
        		{      			
          			Scene scene2 = new Scene(resultPane,512,366);
        		    primaryStage.setScene(scene2);
        			primaryStage.show();	
        			try {
						client.getSocket().close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        		}
        	}
        });
        
        btn2.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent event) {
        		if(number<i)
        		{
        			client.getOut().println(Integer.toString(2));
            		client.getOut().flush();
        			try {
        				nextQ(client.getIn(),client.getOut());
        			} catch (IOException e) {
        				e.printStackTrace();
        			}
        			number++;
        		}
        		else
        		{
        			Scene scene2 = new Scene(resultPane,512,366);
        		    primaryStage.setScene(scene2);
        			primaryStage.show();	
        			try {
						client.getSocket().close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        		}
        	}
        });
        
        btn3.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent event) {
        		if(number<i)
        		{
        			client.getOut().println(Integer.toString(3));
            		client.getOut().flush();
        			try {
        				nextQ(client.getIn(),client.getOut());
        			} catch (IOException e) {
        				e.printStackTrace();
        			}
        			number++;
        		}
        		else
        		{
        			Scene scene2 = new Scene(resultPane,512,366);
        		    primaryStage.setScene(scene2);
        			primaryStage.show();	
        			try {
						client.getSocket().close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        		}
        	}
        });
        
        btn4.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent event) {
        		if(number<i)
        		{
        			client.getOut().println(Integer.toString(4));
            		client.getOut().flush();
        			try {
        				nextQ(client.getIn(),client.getOut());
        			} catch (IOException e) {
        				e.printStackTrace();
        			}
        			number++;
        		}
        		else
        		{
        			Scene scene2 = new Scene(resultPane,512,366);
        		    primaryStage.setScene(scene2);
        			primaryStage.show();	
        			try {
						client.getSocket().close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        		}
        	}
        });
      
        gridPane.add(question, 1, 2);
        gridPane.add(btn1, 1, 3);
        gridPane.add(btn2, 1,4);
        gridPane.add(btn3, 1, 5);
        gridPane.add(btn4, 1, 6);
       
        
        Scene scene = new Scene(gridPane,512,366);
        primaryStage.setScene(scene);
        primaryStage.show();
        
    }   
        
        
    
    
    
    
    public static void main(String[] args) {
		launch(args);	
	}
}
