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

public class GUI extends Application {
	
	  private Label question = new Label(); 
	  private Label result = new Label();
	  private Button btn1 = new Button();
	  private Button btn2 = new Button();
	  private Button btn3 = new Button();
	  private Button btn4 = new Button();
	  private int i;
	  private int number = 0;
	  private int score;
	  
	public int getScore(BufferedReader in) throws NumberFormatException, IOException {
		score = Integer.parseInt(in.readLine());
		return score;
	}
	
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
	}
	
    public void start(Stage primaryStage) throws IOException {
    	ClientTCP client = new ClientTCP();
    	client.start_connection();
    	i = getI(client.getIn());
    	nextQ(client.getIn(),client.getOut());
    	
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
          
        
        btn1.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent event) {
        		client.getOut().println(Integer.toString(1));
        		client.getOut().flush();
        		if(number<i)
        		{
        			try {
        				nextQ(client.getIn(),client.getOut());
        			} catch (IOException e) {
        				e.printStackTrace();
        			}
        			number++;
        		}
        		else
        		{
        			try {
						score = getScore(client.getIn());
					} catch (NumberFormatException | IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
        			System.out.println(score);
        			try {
						client.getSocket().close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        			/*try {
        			score = getScore(client.getIn());
            		result.setText(Integer.toString(score));
        			Scene scene2 = new Scene(resultPane,512,366);
        	        primaryStage.setScene(scene2);
        	        primaryStage.show();
        			} catch(IOException e) {
        				e.printStackTrace();
        			}*/
        		}
        	}
        });
        
        btn2.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent event) {
        		client.getOut().println(Integer.toString(2));
        		client.getOut().flush();
        		if(number<i)
        		{
        			try {
        				nextQ(client.getIn(),client.getOut());
        			} catch (IOException e) {
        				e.printStackTrace();
        			}
        			number++;
        		}
        		else
        		{
        			try {
						client.getSocket().close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        			try {
        				score = getScore(client.getIn());
            			result.setText(Integer.toString(score));
            			Scene scene2 = new Scene(resultPane,512,366);
            	        primaryStage.setScene(scene2);
            	        primaryStage.show();
            			} catch(IOException e) {
            				e.printStackTrace();
            			}
        		}
        	}
        });
        
        btn3.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent event) {
        		client.getOut().println(Integer.toString(3));
        		client.getOut().flush();
        		if(number<i)
        		{
        			try {
        				nextQ(client.getIn(),client.getOut());
        			} catch (IOException e) {
        				e.printStackTrace();
        			}
        			number++;
        		}
        		else
        		{
        			try {
						client.getSocket().close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        			try {
        				score = getScore(client.getIn());
            			result.setText(Integer.toString(score));
            			Scene scene2 = new Scene(resultPane,512,366);
            	        primaryStage.setScene(scene2);
            	        primaryStage.show();
            			} catch(IOException e) {
            				e.printStackTrace();
            			}
        		}
        	}
        });
        
        btn4.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent event) {
        		client.getOut().println(Integer.toString(4));
        		client.getOut().flush();
        		if(number<i)
        		{
        			try {
        				nextQ(client.getIn(),client.getOut());
        			} catch (IOException e) {
        				e.printStackTrace();
        			}
        			number++;
        		}
        		else
        		{
        			try {
						client.getSocket().close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        			
        			try {
        				score = getScore(client.getIn());
            			result.setText(Integer.toString(score));
            			Scene scene2 = new Scene(resultPane,512,366);
            	        primaryStage.setScene(scene2);
            	        primaryStage.show();
            			} catch(IOException e) {
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
