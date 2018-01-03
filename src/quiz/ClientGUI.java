 package quiz;
import java.io.BufferedReader;
import java.io.IOException;
import javafx.event.EventHandler;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ClientGUI extends Application {
	
	  private Label question = new Label(); 
	  private Label result = new Label();
	  private Label id = new Label();
	  private CheckBox ch1 = new CheckBox();
	  private CheckBox ch2 = new CheckBox();
	  private CheckBox ch3 = new CheckBox();
	  private CheckBox ch4 = new CheckBox();
	  private Button next = new Button("Nastêpne pytanie");
	  
	  private int i;
	  private int number = 0;
	  private Label connectionInfo = new Label("Podaj port do po³¹czenia:");
	  private CheckBox portDefault = new CheckBox("Port domyœlny (3506)");
	  private CheckBox portCustom = new CheckBox("Wpisz port:");
	  private TextField port = new TextField("");
	  private Button connect = new Button("Po³¹cz");
	  private Label warning = new Label("");
	  int portNumber;
	  int checkFlag = 0;
	  
	  private String ch1a;
	  private String ch2a;
	  private String ch3a;
	  private String ch4a;
	  
	public int getI(BufferedReader in) throws NumberFormatException, IOException {
		i = Integer.parseInt(in.readLine());
		return i;
	}
	  
	public void nextQ(BufferedReader in) throws IOException {
		ch1.setSelected(false);
		ch2.setSelected(false);
		ch3.setSelected(false);
		ch4.setSelected(false);
		question.setText(in.readLine()+". " + in.readLine());
		ch1.setText("A) " + in.readLine());
		ch2.setText("B) " + in.readLine());
		ch3.setText("C) " + in.readLine());
		ch4.setText("D) " + in.readLine());
	}
	
	public void getScore(BufferedReader in) throws IOException {
		result.setText("Twój wynik: " + in.readLine() + "/15");
	}
	
    public void start(Stage primaryStage) throws IOException {
    	
    	port.setDisable(true);
    	portCustom.setDisable(true);
    	portDefault.setSelected(true);
    	GridPane connection = new GridPane();
    	connection.setAlignment(Pos.TOP_LEFT);
    	connection.setHgap(10);
    	connection.setVgap(10);
    	connection.setPadding(new Insets(10,10,10,10));
    	connection.add(connectionInfo, 1, 1);
    	connection.add(portCustom, 1, 2);
    	connection.add(port, 2, 2);
    	connection.add(portDefault, 1, 3);
    	connection.add(warning, 1, 4);
    	connection.add(connect, 1, 5);
    
    	
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
        resultPane.add(result, 2, 3);
        resultPane.add(id, 2, 5);
        
        question.wrapTextProperty();
        gridPane.add(question, 1, 2);
        gridPane.add(ch1, 1, 3);
        gridPane.add(ch2, 1,4);
        gridPane.add(ch3, 1, 5);
        gridPane.add(ch4, 1, 6);
        gridPane.add(warning, 1, 7);
        gridPane.add(next, 1, 8);
        
        Scene connectScene = new Scene(connection,366,178);
        primaryStage.setScene(connectScene);
        primaryStage.show();
       
        portDefault.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(portDefault.isSelected())
				{
					portCustom.setDisable(true);
					port.setDisable(true);
				}
				else
				{
					portCustom.setDisable(false);
					port.setDisable(true);
				}
			}
		});
        
        portCustom.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(portCustom.isSelected())
				{
					portDefault.setDisable(true);
					port.setDisable(false);
				}
				else
				{
					portDefault.setDisable(false);
					port.setDisable(true);
				}
			}
		});
        
    	connect.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				warning.setText("");
				if(portCustom.isSelected())
				{
					portNumber = Integer.parseInt(port.getText());
					checkFlag = 1;
				}
				else if(portDefault.isSelected())
				{
					portNumber = 3506;
					checkFlag = 1;
				}
				else
				{
					warning.setText("Wybierz port !");
					warning.setTextFill(Color.RED);
				}
				if(checkFlag == 1)
				{
			        ClientTCP client = new ClientTCP(portNumber);
					client.start_connection();
				   	
					try {
						i = getI(client.getIn());
					} catch (NumberFormatException | IOException e2) {
						
						e2.printStackTrace();
					}
					try {
						nextQ(client.getIn());
					} catch (IOException e2) {
						e2.printStackTrace();
					}
					number++;
					
					result.setFont(Font.font("Arial",30));
			        id.setFont(Font.font("Arial",30));
			        id.setText("Twoje ID: " + Integer.toString(client.getID()));
			        
			        next.setOnAction(new EventHandler<ActionEvent>() {
			        	@Override
			        	public void handle(ActionEvent event) {
			        		warning.setText("");
			        		if(ch1.isSelected())
			        		{
			        			ch1a = "1";
			        		}
			        		else
			        		{
			        			ch1a = "0";
			        		}
			        		if(ch2.isSelected())
			        		{
			        			ch2a = "1";
			        		}
			        		else
			        		{
			        			ch2a = "0";
			        		}
			        		if(ch3.isSelected())
			        		{
			        			ch3a = "1";
			        		}
			        		else
			        		{
			        			ch3a = "0";
			        		}
			        		if(ch4.isSelected())
			        		{
			        			ch4a = "1";
			        		}
			        		else
			        		{
			        			ch4a = "0";
			        		}
			        		if (!ch1.isSelected() && !ch2.isSelected() && !ch3.isSelected() && !ch4.isSelected())
			        		{
			        			warning.setText("Wybierz odpowiedz !");
			        			warning.setTextFill(Color.RED);
			        		}
			        		else
			        		{
			        			client.getOut().println(ch1a+ch2a+ch3a+ch4a);
				        		client.getOut().flush();
				      
				        		if(number < i)
				        		{
				        			try {
				        				getScore(client.getIn());
				        				nextQ(client.getIn());
				        			} catch (IOException e) {
				        				e.printStackTrace();
				        			}
				        			number++;
				        		}
				        		else
				        		{      			
				        			try {
										getScore(client.getIn());
									} catch (IOException e1) {
										e1.printStackTrace();
									}
				        			Scene scene2 = new Scene(resultPane,366,256);
				        		    primaryStage.setScene(scene2);
				        			primaryStage.show();	
				        			try {
										client.getSocket().close();
									} catch (IOException e) {
										e.printStackTrace();
									}
				        		}
			        		}
			        		
			        		
			        	}
			        });
			        
			      
			    	Scene scene = new Scene(gridPane,836,256);
					primaryStage.setScene(scene);
					primaryStage.show();
			}
			}
		});
    	
       
		 
	
    	
    	
        
     
    }   
        
        
    
    
    
    
    public static void main(String[] args) {
		launch(args);	
	}
}
